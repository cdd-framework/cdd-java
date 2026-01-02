package io.github.cddframework;

/**
 * Defines the core interface for executing CDD audits
 * To be updated to support more complex scan configurations in the future (Ratel config).
 */
public interface CDDEngine {
    /**
     * Executes an audit on the given target URL.
     * @param targetUrl the URL to audit
     */
    void executeAudit(String targetUrl);

}