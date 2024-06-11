package com.adi.translator.service;

import com.adi.translator.model.exceptions.NotFoundException;
import com.adi.translator.model.request.CreateTranslationRequest;
import com.adi.translator.persistence.entity.AppTranslation;
import com.adi.translator.persistence.entity.Application;
import com.adi.translator.persistence.entity.Translation;
import com.adi.translator.persistence.repository.AppTranslationRepository;
import com.adi.translator.persistence.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslationService {
  private final TranslationRepository translationRepository;
  private final AppTranslationRepository appTranslationRepository;
  private final ApplicationService applicationService;

  public Translation createTranslation(Long applicationId, CreateTranslationRequest request) {
    Application application = applicationService.getApplicationById(applicationId);
    Translation translation = buildTranslation(request, application);
    translation = translationRepository.save(translation);
    AppTranslation appTranslation =
        AppTranslation.builder().application(application).translation(translation).build();
    appTranslationRepository.save(appTranslation);
    return translationRepository.save(translation);
  }

  public Translation getTranslationById(Long translationId) {
    return translationRepository
        .findById(translationId)
        .orElseThrow(() -> new NotFoundException("Translation does not exist"));
  }

  private Translation buildTranslation(CreateTranslationRequest request, Application application) {
    return Translation.builder()
        .key(request.getTranslationKey())
        .value(request.getTranslationValue())
        .build();
  }
}
