package io.github.cddframework;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefaultCDDEngine implements CDDEngine {

    @Override
public void executeAudit(String targetUrl) {
        Path tempBinary = null;
        try {
            String resourceName = determineBinaryName();
            tempBinary = extractExecutable(resourceName);

            log.info("Launching CDD Attack on: {}", targetUrl);

            Process process = new ProcessBuilder(tempBinary.toAbsolutePath().toString(), targetUrl)
                    .inheritIO()
                    .start();

            if (!process.waitFor(2, TimeUnit.MINUTES)) {
                process.destroyForcibly();
                log.error("Audit timed out.");
            }
        } catch (Exception e) {
            log.error("CDD Engine Execution Error", e);
        } finally {
            cleanup(tempBinary);
        }
    }

    private String determineBinaryName() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) return "cdd-core-win.exe";
        if (os.contains("mac")) return "cdd-core-macos";
        return "cdd-core-linux";
    }

    private Path extractExecutable(String name) throws IOException {
        Path tempPath = Files.createTempFile("cdd-", name);
        try (InputStream is = getClass().getResourceAsStream("/bin/" + name)) {
            if (is == null) throw new IOException("Binary not found: " + name);
            Files.copy(is, tempPath, StandardCopyOption.REPLACE_EXISTING);
        }
        tempPath.toFile().setExecutable(true);
        return tempPath;
    }

    private void cleanup(Path path) {
        if (path != null) {
            try { Files.deleteIfExists(path); } 
            catch (IOException e) { log.warn("Temporary file cleanup failed: {}", path); }
        }
    }

}
