-- Formats (lookup)
INSERT INTO formats (code, name) VALUES
    ('MODERN',   'Modern'),
    ('STANDARD', 'Standard'),
    ('PIONEER',  'Pioneer');

INSERT INTO cards (oracle_id, name, mana_value, type_line, color_identity) VALUES
    (gen_random_uuid(), 'Lightning Bolt',              1, 'Instant',                              ARRAY['R']),
    (gen_random_uuid(), 'Monastery Swiftspear',        1, 'Creature — Human Monk',                ARRAY['R']),
    (gen_random_uuid(), 'Soul-Scar Mage',              1, 'Creature — Human Wizard',              ARRAY['R']),
    (gen_random_uuid(), 'Goblin Guide',                1, 'Creature — Goblin Scout',              ARRAY['R']),
    (gen_random_uuid(), 'Lava Spike',                  1, 'Sorcery — Arcane',                     ARRAY['R']),
    (gen_random_uuid(), 'Skewer the Critics',          1, 'Sorcery',                              ARRAY['R']),
    (gen_random_uuid(), 'Play with Fire',              1, 'Instant',                              ARRAY['R']),
    (gen_random_uuid(), 'Eidolon of the Great Revel',  2, 'Enchantment Creature — Spirit',        ARRAY['R']),
    (gen_random_uuid(), 'Boros Charm',                 2, 'Instant',                              ARRAY['W','R']),
    (gen_random_uuid(), 'Lightning Helix',             2, 'Instant',                              ARRAY['W','R']),
    (gen_random_uuid(), 'Consider',                    1, 'Instant',                              ARRAY['U']),
    (gen_random_uuid(), 'Sprite Dragon',               2, 'Creature — Dragon',                    ARRAY['U','R']);

INSERT INTO card_printings (card_id, scryfall_id, set_code, collector_number, rarity)
SELECT c.id, gen_random_uuid(), 'SEED', c.id::text, 'common'
FROM cards c;

INSERT INTO prices (card_printing_id, price, captured_at)
SELECT cp.id, v.price, now()
FROM card_printings cp
JOIN cards c  ON c.id = cp.card_id
JOIN (VALUES
    ('Lightning Bolt',             2.50),
    ('Monastery Swiftspear',       1.00),
    ('Soul-Scar Mage',             3.00),
    ('Goblin Guide',               8.00),
    ('Lava Spike',                 1.50),
    ('Skewer the Critics',         0.50),
    ('Play with Fire',             0.50),
    ('Eidolon of the Great Revel', 6.00),
    ('Boros Charm',                4.00),
    ('Lightning Helix',            1.00),
    ('Consider',                   0.75),
    ('Sprite Dragon',              2.00)
) AS v(name, price) ON v.name = c.name;

-- Decks (all Modern)
INSERT INTO decks (name, format_id, archetype, source, played_on)
SELECT v.name, f.id, v.archetype, 'seed', DATE '2026-07-01'
FROM (VALUES
    ('Mono-Red Aggro',   'Burn'),
    ('Boros Burn',       'Burn'),
    ('Izzet Prowess',    'Prowess'),
    ('Mono-Red Prowess', 'Prowess')
) AS v(name, archetype)
CROSS JOIN formats f
WHERE f.code = 'MODERN';

INSERT INTO deck_cards (deck_id, card_id, board, quantity)
SELECT d.id, c.id, v.board, v.quantity
FROM (VALUES
    -- Mono-Red Aggro
    ('Mono-Red Aggro',   'Lightning Bolt',             'MAIN', 4),
    ('Mono-Red Aggro',   'Monastery Swiftspear',       'MAIN', 4),
    ('Mono-Red Aggro',   'Soul-Scar Mage',             'MAIN', 4),
    ('Mono-Red Aggro',   'Goblin Guide',               'MAIN', 4),
    ('Mono-Red Aggro',   'Lava Spike',                 'MAIN', 4),
    ('Mono-Red Aggro',   'Skewer the Critics',         'MAIN', 4),
    ('Mono-Red Aggro',   'Play with Fire',             'MAIN', 4),
    ('Mono-Red Aggro',   'Eidolon of the Great Revel', 'MAIN', 3),
    ('Mono-Red Aggro',   'Eidolon of the Great Revel', 'SIDE', 1),
    -- Boros Burn
    ('Boros Burn',       'Lightning Bolt',             'MAIN', 4),
    ('Boros Burn',       'Monastery Swiftspear',       'MAIN', 4),
    ('Boros Burn',       'Lava Spike',                 'MAIN', 4),
    ('Boros Burn',       'Skewer the Critics',         'MAIN', 4),
    ('Boros Burn',       'Play with Fire',             'MAIN', 4),
    ('Boros Burn',       'Eidolon of the Great Revel', 'MAIN', 4),
    ('Boros Burn',       'Boros Charm',                'MAIN', 4),
    ('Boros Burn',       'Lightning Helix',            'MAIN', 4),
    -- Izzet Prowess
    ('Izzet Prowess',    'Lightning Bolt',             'MAIN', 4),
    ('Izzet Prowess',    'Monastery Swiftspear',       'MAIN', 4),
    ('Izzet Prowess',    'Soul-Scar Mage',             'MAIN', 4),
    ('Izzet Prowess',    'Play with Fire',             'MAIN', 4),
    ('Izzet Prowess',    'Sprite Dragon',              'MAIN', 4),
    ('Izzet Prowess',    'Consider',                   'MAIN', 4),
    -- Mono-Red Prowess
    ('Mono-Red Prowess', 'Lightning Bolt',             'MAIN', 4),
    ('Mono-Red Prowess', 'Monastery Swiftspear',       'MAIN', 4),
    ('Mono-Red Prowess', 'Soul-Scar Mage',             'MAIN', 4),
    ('Mono-Red Prowess', 'Goblin Guide',               'MAIN', 4),
    ('Mono-Red Prowess', 'Skewer the Critics',         'MAIN', 4),
    ('Mono-Red Prowess', 'Play with Fire',             'MAIN', 4)
) AS v(deck_name, card_name, board, quantity)
JOIN decks d ON d.name = v.deck_name
JOIN cards c ON c.name = v.card_name;
