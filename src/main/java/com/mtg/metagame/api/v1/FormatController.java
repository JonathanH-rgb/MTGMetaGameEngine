package com.mtg.metagame.api.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtg.metagame.api.v1.dto.FormatResponse;

@RestController
@RequestMapping("/api/v1/formats")
public class FormatController {

  // No path argument: the collection lives at exactly /api/v1/formats (no trailing
  // slash). Spring Boot 3+ doesn't match a trailing slash by default, so "/" here
  // would force clients to call /api/v1/formats/ or get a 404.
  @GetMapping
  public ResponseEntity<List<FormatResponse>> getFormats() {
    List<FormatResponse> formats = List.of(
        new FormatResponse("standard", "Standard"),
        new FormatResponse("commander", "Commander"));
    return ResponseEntity.ok(formats);
  }

}
