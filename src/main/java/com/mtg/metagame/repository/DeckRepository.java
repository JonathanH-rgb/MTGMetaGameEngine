package com.mtg.metagame.repository;

import com.mtg.metagame.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Long> {

    boolean existsByName(String name);
}
