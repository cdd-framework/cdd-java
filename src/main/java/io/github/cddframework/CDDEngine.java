package io.github.cddframework;

import java.util.List;

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

    /**
     * Performs an advanced audit with a complete Ratel configuration.
     * This method will be called by the .run() method of the ScanBuilder.
     * 
     * @param targetUrl the URL to audit
     * @param scopes the scan scopes to apply
     * @param ignoredRules the list of rules to ignore
     * @param failurePolicy the policy to apply on failure
     */
    void executeAudit(
        String targetUrl,
        List<ScanScope> scopes,
        List<String> ignoredRules,
        OnFailure failurePolicy
    );
}