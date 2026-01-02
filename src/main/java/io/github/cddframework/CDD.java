package io.github.cddframework;

public class CDD {

    private static final DefaultCDDEngine engine = new DefaultCDDEngine();

    /**
     * Initialize Ratel structure in the current Java project.
     * Generates tests/ratel/security.ratel
     */
    public static void init() {
        engine.initProject();
    }

    /**
     * Run the security audit using the local .ratel file.
     */
    public static void run() {
        // URL is defined in the .ratel file, no need to pass it here
        // Unless we want to override the TARGET variable via future CLI arguments
        engine.executeAudit(null);
    }
}