import { useState, useEffect, useCallback } from 'react';
import { getCatalogList, getCatalogEntry } from '../api/catalogApi';

/**
 * Hook to load and manage a document catalog filtered by type.
 *
 * Usage:
 *   const { entries, selectedId, loading, loadEntry } = useCatalog('FTLX', onContent, onJsonData);
 */
export function useCatalog(currentType, onContent, onJsonData) {
  const [entries, setEntries] = useState([]);
  const [selectedId, setSelectedId] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadEntry = useCallback(async (id) => {
    const entryId = id ?? selectedId;
    if (!entryId) return;
    setSelectedId(entryId);
    const response = await getCatalogEntry(entryId);
    if (response.success) {
      onContent?.(response.result.docOutput);
      if (response.result.jsonData) {
        onJsonData?.(response.result.jsonData);
      }
    }
  }, [selectedId, onContent, onJsonData]);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    getCatalogList(currentType).then((response) => {
      if (cancelled) return;
      if (response.success && response.result?.length > 0) {
        setEntries(response.result);
        const firstId = response.result[0].key;
        setSelectedId(firstId);
        // auto-load first entry
        getCatalogEntry(firstId).then((entryResp) => {
          if (cancelled) return;
          if (entryResp.success) {
            onContent?.(entryResp.result.docOutput);
            if (entryResp.result.jsonData) {
              onJsonData?.(entryResp.result.jsonData);
            }
          }
          setLoading(false);
        });
      } else {
        setEntries([]);
        setLoading(false);
      }
    });
    return () => { cancelled = true; };
  }, [currentType]); // eslint-disable-line react-hooks/exhaustive-deps

  const handleSelect = useCallback((e) => {
    loadEntry(e.target.value);
  }, [loadEntry]);

  return { entries, selectedId, loading, loadEntry, handleSelect };
}

export default useCatalog;
