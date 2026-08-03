CREATE TABLE formats (
    id   smallint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code text     NOT NULL UNIQUE,
    name text     NOT NULL
);

CREATE TABLE cards (
    id             integer  GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    oracle_id      uuid     NOT NULL UNIQUE,
    name           text     NOT NULL,
    mana_value     smallint NOT NULL DEFAULT 0 CHECK (mana_value >= 0),
    type_line      text,
    color_identity text[]   NOT NULL DEFAULT '{}'
                            CHECK (color_identity <@ ARRAY['W','U','B','R','G'])
);

CREATE TABLE card_printings (
    id               integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    card_id          integer NOT NULL REFERENCES cards (id),
    scryfall_id      uuid    NOT NULL UNIQUE,
    set_code         text    NOT NULL,
    collector_number text    NOT NULL,
    rarity           text    NOT NULL
                             CHECK (rarity IN ('common', 'uncommon', 'rare', 'mythic', 'special', 'bonus'))
);

CREATE TABLE prices (
    id               bigint        GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    card_printing_id integer       NOT NULL REFERENCES card_printings (id),
    price            numeric(10, 2) NOT NULL CHECK (price >= 0),
    captured_at      timestamptz   NOT NULL
);

CREATE TABLE decks (
    id         bigint   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       text     NOT NULL,
    format_id  smallint NOT NULL REFERENCES formats (id),
    archetype  text,
    source     text,
    source_url text,
    played_on  date,
    placement  smallint
);

CREATE TABLE deck_cards (
    deck_id  bigint   NOT NULL REFERENCES decks (id),
    card_id  integer  NOT NULL REFERENCES cards (id),
    board    text     NOT NULL CHECK (board IN ('MAIN', 'SIDE')),
    quantity smallint NOT NULL DEFAULT 1 CHECK (quantity > 0),
    PRIMARY KEY (deck_id, card_id, board)
);
