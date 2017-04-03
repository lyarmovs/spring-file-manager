package com.lyarmovs.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties, externally controlled in application.properties
 */
@Component
@ConfigurationProperties(prefix="document")
public class DocumentProperties {
    //External file storage directory
    private String storageDir;
    //Scheduler default delay between the executions
    private String fixedDelay;
    //From email address
    private String from;
    //To email address
    private String to;

    public String getStorageDir() {
        return storageDir;
    }

    public void setStorageDir(String storageDir) {
        this.storageDir = storageDir;
    }

    public String getFixedDelay() {
        return fixedDelay;
    }

    public void setFixedDelay(String fixedDelay) {
        this.fixedDelay = fixedDelay;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "DocumentProperties{\n" +
                "storageDir='" + storageDir + '\'' + "\n" +
                ", fixedDelay='" + fixedDelay + '\'' + "\n" +
                ", from='" + from + '\'' + "\n" +
                ", to='" + to + '\'' + "\n" +
                '}';
    }
}
