package com.mtg.metagame.repository;

import com.mtg.metagame.api.v1.dto.CardRecommendation;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class RecommendationRepository {

    private static final String RECOMMEND_SQL = """
        WITH my_deck AS (
            SELECT DISTINCT dc.card_id
            FROM deck_cards dc
            JOIN decks d ON d.id = dc.deck_id
            WHERE d.name = :deckName
        )
        SELECT c.name AS card_name, SUM(cc.co_count)::bigint AS score
        FROM card_cooccurrence cc
        JOIN cards c ON c.id = cc.card_b
        WHERE cc.card_a IN (SELECT card_id FROM my_deck)
          AND cc.card_b NOT IN (SELECT card_id FROM my_deck)
        GROUP BY c.id, c.name
        ORDER BY score DESC, c.name
        LIMIT :limit
        """;

    private final JdbcClient jdbc;

    RecommendationRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<CardRecommendation> findRecommendations(String deckName, int limit) {
        return jdbc.sql(RECOMMEND_SQL)
                .param("deckName", deckName)
                .param("limit", limit)
                .query((rs, rowNum) -> new CardRecommendation(rs.getString("card_name"), rs.getLong("score")))
                .list();
    }
}
