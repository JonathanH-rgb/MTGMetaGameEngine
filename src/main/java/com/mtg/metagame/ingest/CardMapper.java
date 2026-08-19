package com.mtg.metagame.ingest;

import com.mtg.metagame.domain.Card;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class CardMapper {

    private CardMapper() {
    }

    static Optional<Card> toCard(ScryfallCard source) {
        if (source.oracleId() == null) {
            return Optional.empty();
        }
        long rounded = source.cmc() == null ? 0 : Math.round(source.cmc());
        short manaValue = (short) Math.max(0, Math.min(Short.MAX_VALUE, rounded));
        List<String> colors = source.colorIdentity() == null ? new ArrayList<>() : source.colorIdentity();
        return Optional.of(new Card(source.oracleId(), source.name(), manaValue, source.typeLine(), colors));
    }
}
