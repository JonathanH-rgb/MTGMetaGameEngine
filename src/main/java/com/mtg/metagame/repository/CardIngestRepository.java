package com.mtg.metagame.repository;

import com.mtg.metagame.domain.Card;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CardIngestRepository {

    private static final String UPSERT_SQL = """
        INSERT INTO cards (oracle_id, name, mana_value, type_line, color_identity)
        VALUES (?, ?, ?, ?, ?)
        ON CONFLICT (oracle_id) DO UPDATE SET
            name = EXCLUDED.name,
            mana_value = EXCLUDED.mana_value,
            type_line = EXCLUDED.type_line,
            color_identity = EXCLUDED.color_identity
        """;

    private final JdbcTemplate jdbc;

    CardIngestRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public int upsertBatch(List<Card> cards) {
        int[] updated = jdbc.batchUpdate(UPSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Card card = cards.get(i);
                ps.setObject(1, card.getOracleId());
                ps.setString(2, card.getName());
                ps.setShort(3, card.getManaValue());
                ps.setString(4, card.getTypeLine());
                Array colors = ps.getConnection().createArrayOf("text", card.getColorIdentity().toArray());
                ps.setArray(5, colors);
            }

            @Override
            public int getBatchSize() {
                return cards.size();
            }
        });
        return updated.length;
    }
}
