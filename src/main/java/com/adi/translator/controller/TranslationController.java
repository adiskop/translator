package com.adi.translator.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.adi.translator.model.request.CreateTranslationRequest;
import com.adi.translator.persistence.entity.Translation;
import com.adi.translator.service.TranslationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/translation", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class TranslationController {
  private final TranslationService translationService;

  @PostMapping(value = "/{applicationId}", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Create new translation")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Translation.class)})
  public Translation createApplication(
      @Valid @RequestBody CreateTranslationRequest request, @PathVariable Long applicationId) {
    return translationService.createTranslation(applicationId, request);
  }

  @GetMapping(value = "/{translationId}")
  @Operation(description = "Get translation by ID")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Translation.class)})
  public Translation getTranslation(@PathVariable Long translationId) {
    return translationService.getTranslationById(translationId);
  }
}
