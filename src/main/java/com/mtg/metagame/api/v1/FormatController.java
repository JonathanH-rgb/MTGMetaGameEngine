package com.mtg.metagame.api.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtg.metagame.api.v1.dto.FormatResponse;

@RestController
@RequestMapping("api/v1/formats")
public class FormatController {

  @GetMapping("/")
  public ResponseEntity<List<FormatResponse>> getFormats() {
    List<FormatResponse> formats = List.of(new FormatResponse("STD", "STANDAR"),
        new FormatResponse("CMD", "COMMANDER"));
    return ResponseEntity.ok(formats);

  }

}
