package com.mtg.metagame.ingest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ScryfallCard(
        @JsonProperty("oracle_id") UUID oracleId,
        @JsonProperty("name") String name,
        @JsonProperty("cmc") Double cmc,
        @JsonProperty("type_line") String typeLine,
        @JsonProperty("color_identity") List<String> colorIdentity) {
}
