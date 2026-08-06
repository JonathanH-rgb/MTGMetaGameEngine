package com.mtg.metagame.repository;

import com.mtg.metagame.domain.DeckCard;
import com.mtg.metagame.domain.DeckCardId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckCardRepository extends JpaRepository<DeckCard, DeckCardId> {
}
