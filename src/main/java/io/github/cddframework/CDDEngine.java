package io.github.cddframework;

public interface CDDEngine {
    /**
     * Executes an audit on the given target URL.
     * @param targetUrl the URL to audit
     */
    void executeAudit(String targetUrl);
}