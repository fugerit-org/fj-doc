#!/bin/bash
# ---------------------------------------------------------------------------
# aot-warmup.sh
#
# Starts the application in the background with -XX:AOTCacheOutput so that
# the JVM can record compiled code into the cache file on exit.
# While the app is alive, representative HTTP requests are fired against all
# major hot paths (catalog, metadata, document generation in several formats).
# A graceful SIGTERM then flushes the AOT cache to disk.
#
# Usage (from Dockerfile RUN step, WORKDIR=/deployments):
#   bash /deployments/aot-warmup.sh
# ---------------------------------------------------------------------------
set -e

BASE_URL="http://localhost:8080/fj-doc-playground/api"
MAX_RETRIES=30   # × 2 s = 60 s max wait
RETRY_DELAY=2

# ---------------------------------------------------------------------------
# 1. Start the app in background with AOT cache output
# ---------------------------------------------------------------------------
echo "=== [AOT] Starting app in background ==="
java -XX:AOTCacheOutput=app.aot \
     -Xlog:aot*=debug:file=/tmp/aot.log \
     -XX:+UseCompactObjectHeaders \
     -jar quarkus-run.jar > /tmp/app.log 2>&1 &
APP_PID=$!
echo "    App PID: $APP_PID"

# ---------------------------------------------------------------------------
# 2. Wait for readiness (poll /meta/version — no health extension required)
# ---------------------------------------------------------------------------
echo "=== [AOT] Waiting for readiness ==="
READY=0
for i in $(seq 1 "$MAX_RETRIES"); do
    if curl -sf "$BASE_URL/meta/version" > /dev/null 2>&1; then
        READY=1
        echo "    ✓ App ready after attempt $i ($((i * RETRY_DELAY)) s)"
        break
    fi
    echo "    Waiting… attempt $i / $MAX_RETRIES"
    sleep "$RETRY_DELAY"
done

# ---------------------------------------------------------------------------
# 3. Representative workload
# ---------------------------------------------------------------------------
if [ "$READY" != "1" ]; then
    echo "    ⚠ App did not become ready in time – skipping warmup requests"
else
    echo "=== [AOT] Running representative warmup workload ==="

    # --- Lightweight read-only endpoints (metadata, catalog, validation) ---
    echo "  → GET /meta/version"
    curl -sf "$BASE_URL/meta/version"           > /dev/null

    echo "  → GET /meta/info"
    curl -sf "$BASE_URL/meta/info"              > /dev/null

    echo "  → GET /catalog/list"
    curl -sf "$BASE_URL/catalog/list"           > /dev/null

    echo "  → GET /catalog/list/type/xml"
    curl -sf "$BASE_URL/catalog/list/type/xml"  > /dev/null || true

    echo "  → GET /catalog/list/type/json"
    curl -sf "$BASE_URL/catalog/list/type/json" > /dev/null || true

    echo "  → GET /val/supported_extensions"
    curl -sf "$BASE_URL/val/supported_extensions" > /dev/null

    # --- Core hot path: document generation in multiple formats ---
    # A minimal Venus XML document used as input for all generation calls.
    # Written to a file once; each curl call swaps only the outputFormat.
    cat > /tmp/warmup_doc.xml << 'XMLEOF'
<?xml version="1.0" encoding="utf-8"?>
<doc xmlns="http://javacoredoc.fugerit.org"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://javacoredoc.fugerit.org https://www.fugerit.org/data/java/doc/xsd/doc-2-0.xsd">
  <metadata>
    <info name="margins">10;10;10;30</info>
    <info name="doc-title">AOT Warmup</info>
    <info name="doc-author">docker-build</info>
  </metadata>
  <body>
    <para>AOT cache warmup document – exercises the main generation pipeline.</para>
    <table columns="2" colwidths="50;50" width="100" padding="2">
      <row><cell><para style="bold">Key</para></cell><cell><para style="bold">Value</para></cell></row>
      <row><cell><para>stage</para></cell><cell><para>AOT build</para></cell></row>
      <row><cell><para>purpose</para></cell><cell><para>cache warmup</para></cell></row>
    </table>
  </body>
</doc>
XMLEOF

    # Helper: build the JSON payload for a given outputFormat
    make_payload() {
        local fmt="$1"
        # Embed the XML as a JSON string value (escape double-quotes and newlines)
        local escaped_xml
        escaped_xml=$(sed 's/\\/\\\\/g; s/"/\\"/g' /tmp/warmup_doc.xml | tr '\n' ' ')
        printf '{"inputFormat":"XML","outputFormat":"%s","docContent":"%s"}' "$fmt" "$escaped_xml"
    }

    # XML → HTML  (lightest: pure text rendering, no external fonts)
    echo "  → POST /generate/document  (XML → HTML)"
    make_payload "HTML" > /tmp/warmup_payload.json
    curl -sf -X POST "$BASE_URL/generate/document" \
         -H 'Content-Type: application/json' \
         -H 'Accept: application/json'       \
         -d @/tmp/warmup_payload.json > /dev/null

    # XML → XLSX  (exercises Apache POI code path)
    echo "  → POST /generate/document  (XML → XLSX)"
    make_payload "XLSX" > /tmp/warmup_payload.json
    curl -sf -X POST "$BASE_URL/generate/document" \
         -H 'Content-Type: application/json' \
         -H 'Accept: application/json'       \
         -d @/tmp/warmup_payload.json > /dev/null || true

    # XML → PDF   (exercises FOP/OpenPDF and font loading – the heaviest path)
    echo "  → POST /generate/document  (XML → PDF)"
    make_payload "PDF" > /tmp/warmup_payload.json
    curl -sf -X POST "$BASE_URL/generate/document" \
         -H 'Content-Type: application/json' \
         -H 'Accept: application/json'       \
         -d @/tmp/warmup_payload.json > /dev/null || true

    # XML → validate  (exercises the validation code path)
    echo "  → POST /generate/validate  (XML)"
    make_payload "HTML" > /tmp/warmup_payload.json
    curl -sf -X POST "$BASE_URL/generate/validate" \
         -H 'Content-Type: application/json' \
         -H 'Accept: application/json'       \
         -d @/tmp/warmup_payload.json > /dev/null || true

    echo "    ✓ Warmup workload completed"
fi

# ---------------------------------------------------------------------------
# 4. Graceful shutdown → JVM writes the AOT cache on exit
# ---------------------------------------------------------------------------
echo "=== [AOT] Sending SIGTERM → JVM will flush AOT cache on exit ==="
kill -TERM "$APP_PID"
wait "$APP_PID" || true
echo "    ✓ App shut down"

# ---------------------------------------------------------------------------
# 5. Logs & cache verification
# ---------------------------------------------------------------------------
echo "=== [AOT] App startup/runtime log (last 50 lines) ==="
tail -50 /tmp/app.log

if [ -f /tmp/aot.log ]; then
    echo "=== [AOT] AOT debug log (last 50 lines) ==="
    tail -50 /tmp/aot.log
fi

echo "=== [AOT] Checking for cache file ==="
if [ -f /deployments/app.aot ]; then
    echo "    ✓ AOT cache generated successfully"
    ls -lh /deployments/app.aot
else
    echo "    ⚠ WARNING: AOT cache not generated – creating placeholder"
    touch /deployments/app.aot
fi

