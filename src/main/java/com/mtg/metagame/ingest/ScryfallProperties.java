package com.mtg.metagame.ingest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "scryfall")
public class ScryfallProperties {

    private String baseUrl = "https://api.scryfall.com";
    private String userAgent = "mtg-metagame-engine/0.1";
    private String bulkType = "oracle_cards";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getBulkType() {
        return bulkType;
    }

    public void setBulkType(String bulkType) {
        this.bulkType = bulkType;
    }
}
