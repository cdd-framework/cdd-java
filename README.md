# CDD Framework - Java Adapter (v0.6.0-alpha.0)

The Cloud Detection & Defense (CDD) Java adapter allows you to integrate cyber-security audits directly into your JVM-based applications.

It acts as a **smart wrapper** around the **Ratel Core** (Rust), ensuring high-performance execution without requiring your users to install external binaries manually.

## Installation

Add the dependency to your `pom.xml` (hosted on GitHub Packages or Maven Central):

```xml
<dependency>
    <groupId>io.github.cddframework</groupId>
    <artifactId>cdd-java</artifactId>
    <version>0.6.0-alpha.0</version>
</dependency>
```

## How it Works
The framework follows a "Binary Pivot" architecture:

Auto-Extraction: Upon first execution, the library detects the OS (Windows, Linux, macOS) and extracts the embedded ratel-cli binary to ~/.ratel/bin/.

Standard Layout: It enforces Maven/Gradle standards by placing security scenarios in src/test/resources/ratel/.

Isolation: The audit runs in a separate process, ensuring your JVM memory remains unpolluted.

## Usage
The workflow is divided into two phases: Initialization and Execution.

### 1. Initialization
Run this once to generate the default security scenario. It automatically detects if you are in a Maven/Gradle project.

```Java

import io.github.cddframework.CDD;

public class SetupAudit {
    public static void main(String[] args) {
        // Generates src/test/resources/ratel/security.ratel
        CDD.init(); 
        System.out.println("Security scenario created.");
    }
}
```

### 2. Configuration (The Ratel DSL)
Instead of writing Java code to configure the scan, you edit the generated .ratel file. This separates configuration from code, allowing security experts to tweak rules without recompiling the Java app.

File: src/test/resources/ratel/security.ratel

```Code snippet

SCENARIO "Production Access Audit"
TARGET "http://localhost:8080"

// Define the perimeter
WITH SCOPE KERNEL
WITH SCOPE TERRITORY

// Custom exclusions
IGNORE "FAVICON_FINGERPRINT"
```

### 3. Execution (Runtime or Tests)
You can trigger the audit anywhere: in a standard main method, a Spring Boot CommandLineRunner, or a JUnit test.

Example with Spring Boot:

```Java

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner securityCheck() {
        return args -> {
            System.out.println("🚀 Starting Security Audit...");
            
            // Executes the binary using the local .ratel file
            CDD.run(); 
        };
    }
}
```

## Update the package

Update the pom.xml with the new version

Run command
```java
./mvn clean package
./mvn clean install
```

## Deploy the package

```
./mvn deploy
```

## Integration with CI/CD
Since cdd-java wraps the CLI, exit codes are propagated. If the audit fails (vulnerabilities found), CDD.run() will log the error, and you can catch the state to fail your build pipeline if necessary.

Part of the CDD-Framework organization