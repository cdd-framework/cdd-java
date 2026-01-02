package io.github.cddframework;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefaultCDDEngine implements CDDEngine {

    private final Path workingDir;
    private final String binaryName;
    private Path binaryPath;

    public DefaultCDDEngine() {
        // Uses the user directory to store the extracted binary
        this.workingDir = Paths.get(System.getProperty("user.home"), ".ratel");
        this.binaryName = determineBinaryName();
        
        try {
            initializeEnvironment();
        } catch (IOException e) {
            throw new RuntimeException("Ratel CLI initialization failed", e);
        }
    }

    private void initializeEnvironment() throws IOException {
        Files.createDirectories(workingDir.resolve("bin"));
        this.binaryPath = workingDir.resolve("bin").resolve(binaryName);

        // Extraction of the binary from the JAR to ~/.ratel/bin/
        // This ensures that the version embedded in the JAR is used
        installResource("/bin/" + binaryName, binaryPath);
        
        log.info("Ratel CLI ready at: {}", binaryPath);
    }

    // Command: ratel-cli init
    public void initProject() {
        runRatelCommand("init");
    }

    // Command: ratel-cli run tests/ratel/security.ratel
    @Override
    public void executeAudit(String targetUrl) {
        // Note: targetUrl is ignored here because the URL is defined in the .ratel file
        // We could use it to override the config if ratel-cli supported it (e.g., --target X)
        log.info("Executing audit using local scenario...");
        runRatelCommand("run", "src/test/resources/ratel/security.ratel"); 
    }

    private void runRatelCommand(String... args) {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command().add(binaryPath.toString());
            for (String arg : args) {
                pb.command().add(arg);
            }
            
            // Redirection of the binary output to the Java console
            pb.inheritIO(); 
            
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                log.error("Ratel command failed with exit code: {}", exitCode);
                // We do not throw an exception to allow the user to see the binary logs
            }
        } catch (Exception e) {
            log.error("Failed to execute Ratel command", e);
            throw new RuntimeException("Audit execution failed", e);
        }
    }

    private void installResource(String resourcePath, Path targetPath) throws IOException {
        // Extraction with overwrite to update the binary if the JAR version changes
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Binary not found in JAR resources: " + resourcePath);
            }
            Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
            targetPath.toFile().setExecutable(true);
        }
    }

    private String determineBinaryName() {
        String os = System.getProperty("os.name").toLowerCase();
        // Names aligned with the standard installation
        if (os.contains("win")) return "ratel.exe";
        if (os.contains("mac")) return "ratel";
        return "ratel";
    }
}