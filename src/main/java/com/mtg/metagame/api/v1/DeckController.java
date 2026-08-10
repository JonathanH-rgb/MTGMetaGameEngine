package com.mtg.metagame.api.v1;

import com.mtg.metagame.api.v1.dto.CardRecommendation;
import com.mtg.metagame.service.RecommendationService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/decks")
public class DeckController {

    private final RecommendationService recommendationService;

    DeckController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<CardRecommendation>> getRecommendations(@RequestParam("deck") String deck) {
        return ResponseEntity.ok(recommendationService.recommendForDeck(deck));
    }
}
