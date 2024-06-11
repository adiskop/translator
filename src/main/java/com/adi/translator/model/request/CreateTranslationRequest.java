package com.adi.translator.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class CreateTranslationRequest {
  @NotBlank(message = "translationKey is required")
  @NotNull(message = "translationKey is required")
  private String translationKey;

  @NotNull(message = "translationValue is required")
  @NotBlank(message = "translationValue is required")
  private String translationValue;
}
