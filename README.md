# CDD Java Adapter (Spring Boot Starter)

> **Java adapter for the CDD (Cyberattack-Driven Development) framework. Audit your Spring Boot application's security posture natively.**

## Overview
This adapter wraps the high-performance Rust core to provide security auditing capabilities directly within the Java Virtual Machine (JVM). It is optimized for **Spring Boot 3.x** and supports automatic service discovery.



## Features
* **Native Execution**: Manages the lifecycle of Rust binaries (extraction, execution, and cleanup).
* **Auto-Scan**: Automatically detects the local `server.port` to audit the running application on startup.
* **Cross-Platform**: Includes pre-compiled binaries for Windows, Linux, and macOS.
* **Developer Friendly**: Uses SLF4J logging and Spring Bean injection for seamless integration.

## Installation (Maven)
Once published to your local repository or GitHub Packages, add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.cdd-framework</groupId>
    <artifactId>cdd-java</artifactId>
    <version>0.4.0-alpha.2</version>
</dependency>
```

## Usage
By default, the framework includes a CommandLineRunner that triggers a scan upon startup against your own instance. You can also inject the engine manually:

```
Java

@Autowired
private CDDEngine cddEngine;

public void secureCheck() {
    // Audit a specific target
    cddEngine.executeAudit("http://localhost:8080"); //
}
```

## Internal Structure
- src/main/resources/bin/: Contains the native alpha.2 binaries.
- DefaultCDDEngine.java: Handles the secure bridge between Java and Rust.

Part of the CDD-Framework organization.