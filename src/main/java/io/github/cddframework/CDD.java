package io.github.cddframework;

public class CDD {
    
    // Singleton instance of the CDD engine
    private static final CDDEngine engine = new DefaultCDDEngine();

    // Private constructor to prevent instantiation
    public static ScanBuilder scan(String url) {
        return new ScanBuilder(url, engine);
    }

}
