package io.github.cddframework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.jackson.databind.ObjectMapper;

public class ScanBuilder {

    private final String url;
    private final CDDEngine engine;
    // private final List<String> scopes = new ArrayList<>();
    private final List<ScanScope> scopes = new ArrayList<>();
    private final List<String> ignoredRules = new ArrayList<>();
    private OnFailure failurePolicy = OnFailure.CONTINUE;

    public ScanBuilder(String url, CDDEngine engine) {
        this.url = url;
        this.engine = engine;
    }

    public ScanBuilder withScope(ScanScope scope) {
        this.scopes.add(scope);
        return this;
    }

    public ScanBuilder ignore(String ruleId){
        this.ignoredRules.add(ruleId);
        return this;
    }

    public ScanBuilder onFailure(OnFailure policy){
        this.failurePolicy = policy;
        return this;
    }

    // Method to start the scan
    public void run() {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put("target_url", url);
            config.put("scopes", scopes);
            config.put("ignored_rules", ignoredRules);
            config.put("failure_policy", failurePolicy.name());

            String jsonConfig = new ObjectMapper().writeValueAsString(config);
            engine.executeAudit(jsonConfig, scopes, ignoredRules, failurePolicy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize Ratel configuration", e);
        }
    }
    
}

