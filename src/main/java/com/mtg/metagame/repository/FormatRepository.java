package com.mtg.metagame.repository;

import com.mtg.metagame.domain.Format;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormatRepository extends JpaRepository<Format, Short> {
}
