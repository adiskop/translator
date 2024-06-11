package com.adi.translator.service;

import com.adi.translator.model.exceptions.BadRequestException;
import com.adi.translator.model.exceptions.NotFoundException;
import com.adi.translator.model.request.CreateApplicationRequest;
import com.adi.translator.persistence.entity.Application;
import com.adi.translator.persistence.repository.ApplicationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {
  private final ApplicationRepository applicationRepository;

  public Application createApplication(CreateApplicationRequest request) {
    ensureAppNameIsUnique(request.getName());
    Application application = buildApplicationFromRequest(request);
    return applicationRepository.save(application);
  }

  public List<Application> getAllApplications() {
    return applicationRepository.findAll();
  }

  public Application getApplicationById(Long applicationId) {
    return applicationRepository
        .findById(applicationId)
        .orElseThrow(() -> new NotFoundException("Application does not exist"));
  }

  private void ensureAppNameIsUnique(String name) {
    if (applicationRepository.existsByNameEqualsIgnoreCase(name)) {
      throw new BadRequestException("Another app with same name already exists");
    }
  }

  private Application buildApplicationFromRequest(CreateApplicationRequest request) {
    return Application.builder().name(request.getName()).build();
  }
}
