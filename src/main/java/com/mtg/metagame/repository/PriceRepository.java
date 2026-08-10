package com.mtg.metagame.repository;

import com.mtg.metagame.domain.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Long> {
}
