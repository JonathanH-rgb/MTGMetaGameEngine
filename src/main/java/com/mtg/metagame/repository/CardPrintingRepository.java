package com.mtg.metagame.repository;

import com.mtg.metagame.domain.CardPrinting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardPrintingRepository extends JpaRepository<CardPrinting, Integer> {
}
