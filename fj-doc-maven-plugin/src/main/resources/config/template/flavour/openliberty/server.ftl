<server description="Fugerit Open Liberty server">
    <featureManager>
        <feature>mpHealth-4.0</feature>
        <feature>restfulWS-3.1</feature>
        <feature>mpOpenAPI-3.1</feature>
    </featureManager>
    <httpEndpoint host="*" httpPort="${r"${default.http.port}"}" httpsPort="${r"${default.https.port}"}" id="defaultHttpEndpoint" />
    <webApplication location="${context.artifactId}.war" contextRoot="/" />
</server>
