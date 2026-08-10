-- V3: precomputed card co-occurrence matrix.

CREATE MATERIALIZED VIEW card_cooccurrence AS
SELECT d1.card_id                 AS card_a,
       d2.card_id                 AS card_b,
       COUNT(DISTINCT d1.deck_id) AS co_count
FROM deck_cards d1
JOIN deck_cards d2 ON d2.deck_id = d1.deck_id
WHERE d1.card_id <> d2.card_id
GROUP BY d1.card_id, d2.card_id;

CREATE INDEX idx_card_cooccurrence_card_a ON card_cooccurrence (card_a);
