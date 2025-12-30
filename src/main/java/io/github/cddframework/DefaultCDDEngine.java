package io.github.cddframework;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

/**
 * Main implementation of the CDD engine
 * Manages the automatic installation of the Rust binary and Ratel rules at startup.
 */
@Slf4j
@Service
public class DefaultCDDEngine implements CDDEngine {

    private final Path workingDir;
    private final String binaryName;

    public DefaultCDDEngine() {

        this.workingDir = Paths.get(System.getProperty("user.home"), ".cdd");
        this.binaryName = determineBinaryName();
        
        try {
            initializeEnvironment();
        } catch (IOException e) {
            log.error("Failed to initialize Ratel environment", e);
            throw new RuntimeException("Ratel initialization failed", e);
        }
    }

    /**
     * Prepares the file system: creates the folders and extracts the necessary resources.
     */
    private void initializeEnvironment() throws IOException {
        Files.createDirectories(workingDir.resolve("bin"));
        Files.createDirectories(workingDir.resolve("ratel"));

        // Installation of the Rust binary
        installResource("/bin/" + binaryName, workingDir.resolve("bin").resolve(binaryName), true);
        
        // Installation of the Ratel repositories (JSON)
        installResource("/ratel/kernel.json", workingDir.resolve("ratel/kernel.json"), false);
        installResource("/ratel/territory.json", workingDir.resolve("ratel/territory.json"), false);
        
        log.info("Ratel environment initialized at: {}", workingDir);
    }

    @Override
    public void executeAudit(String targetUrl) {
        Path binaryPath = workingDir.resolve("bin").resolve(binaryName);
        
        try {
            log.info("Ratel is hunting on: {}", targetUrl);

            // Execution of the Rust binary. Logs are inherited to show real-time output.
            Process process = new ProcessBuilder(binaryPath.toString(), targetUrl)
                    .inheritIO()
                    .start();

            if (!process.waitFor(5, TimeUnit.MINUTES)) {
                process.destroyForcibly();
                log.error("Audit timed out after 5 minutes.");
            }
        } catch (Exception e) {
            log.error("CDD Engine Execution Error", e);
        }
    }

    @Override
    public void executeAudit(String targetUrl, List<ScanScope> scopes, List<String> ignoredRules, OnFailure failurePolicy) {
        try {
            Map<String, Object> payload = Map.of(
                "target_url", targetUrl,
                "scopes", scopes,
                "ignored_rules", ignoredRules,
                "failure_policy", failurePolicy.name()
            );
            String jsonArg = new ObjectMapper().writeValueAsString(payload);
            Path binaryPath = workingDir.resolve("bin").resolve(binaryName);
            Process process = new ProcessBuilder(binaryPath.toString(), jsonArg)
                    .inheritIO()
                    .start();

            process.waitFor(5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("Ratel Execution Error", e);
        }
    }


    /**
     * Extracts a resource (binary / json file) from the JAR to the local file system.
     */
    private void installResource(String resourcePath, Path targetPath, boolean isExecutable) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                log.warn("Resource not found in JAR: {}", resourcePath);
                return;
            }
            Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
            if (isExecutable) {
                targetPath.toFile().setExecutable(true);
            }
        }
    }

    private String determineBinaryName() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return "cdd-core-win.exe";
        if (os.contains("mac")) return "cdd-core-macos";
        return "cdd-core-linux";
    }
}