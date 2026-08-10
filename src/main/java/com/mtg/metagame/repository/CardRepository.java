package com.mtg.metagame.repository;

import com.mtg.metagame.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {
}
