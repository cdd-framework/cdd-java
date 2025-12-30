# CDD Framework - Java Adapter

The Cloud Detection & Defense (CDD) Java adapter allows you to integrate security audits directly into your JVM-based applications. It leverages a high-performance **Rust core** via native binaries.

## 1. Getting Started & Installation

### Dependency
Add the following dependency to your `pom.xml` (hosted on GitHub Packages):

```xml
<dependency>
    <groupId>io.github.cddframework</groupId>
    <artifactId>cdd-java</artifactId>
    <version>0.4.0-alpha.3</version>
</dependency>
```

## Installation & Lifecycle
The framework is designed to be Zero-Config:
- Auto-Extraction: Upon the first execution, the library automatically detects your OS (Windows, Linux, or macOS) and extracts the required Rust binaries and Ratel rules into ~/.cdd/.
- Auto-Configuration: If you are using Spring Boot, the CDDEngine bean is automatically registered and ready for injection.

## Basic Execution
To run a standard security audit on a target URL:
```Java
@Autowired
private CDDEngine engine;

public void runSecurityCheck() {
    engine.executeAudit("http://localhost:8080");
}
```

## 2. Ratel: The Fluent API Evolution
In version 0.4.0-alpha.3, we introduced Ratel, a common language for security instructions shared across our Java, Python, and Node.js adapters.

Ratel moves security from a passive check to an offensive defense strategy using a "concentric circles" approach.
## New Core Components

## New Core Components

| Component | Purpose |
| :--- | :--- |
| `CDD.java` | The static entry point for the Fluent API. It provides a clean, unified way to start a scan. |
| `ScanBuilder.java` | A builder pattern implementation that allows you to configure your scan strategy (e.g., adding scopes or ignoring rules). |
| `ScanScope.java` | An Enum defining the audit perimeter: `KERNEL` (15 core hygiene tests) and `TERRITORY` (15 infrastructure exposure tests). |
| `OnFailure.java` | Defines the policy when a vulnerability is found: `CONTINUE`, `LOG_ONLY`, or `FAIL_BUILD` (perfect for CI/CD). |

## Advanced Usage with Ratel
Ratel allows you to express your security intentions with human-readable code:
```java
import io.github.cddframework.*;

CDD.scan("http://localhost:8080")
   .withScope(ScanScope.KERNEL)
   .withScope(ScanScope.TERRITORY)
   .ignore("FAVICON_FINGERPRINT")
   .onFailure(OnFailure.FAIL_BUILD)
   .run();
```

Part of the CDD-Framework organization.