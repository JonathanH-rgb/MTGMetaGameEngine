package com.mtg.metagame.service;

import com.mtg.metagame.api.v1.dto.CardRecommendation;
import com.mtg.metagame.repository.DeckRepository;
import com.mtg.metagame.repository.RecommendationRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);
    private static final int DEFAULT_LIMIT = 10;

    private final RecommendationRepository repository;
    private final DeckRepository deckRepository;

    RecommendationService(RecommendationRepository repository, DeckRepository deckRepository) {
        this.repository = repository;
        this.deckRepository = deckRepository;
    }

    public List<CardRecommendation> recommendForDeck(String deckName) {
        if (!deckRepository.existsByName(deckName)) {
            throw new DeckNotFoundException(deckName);
        }
        List<CardRecommendation> recommendations = repository.findRecommendations(deckName, DEFAULT_LIMIT);
        log.info("Recommendations for deck '{}': {} cards", deckName, recommendations.size());
        return recommendations;
    }
}
