package com.adi.translator.service;

import com.adi.translator.model.exceptions.BadRequestException;
import com.adi.translator.model.exceptions.NotFoundException;
import com.adi.translator.model.request.CreateApplicationRequest;
import com.adi.translator.persistence.entity.Application;
import com.adi.translator.persistence.entity.Translation;
import com.adi.translator.persistence.repository.ApplicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {
  private final ApplicationRepository applicationRepository;
  private final ObjectMapper objectMapper;

  @Value("${translator.output.directory:app-translations}")
  private String outputDirectory;

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

  public void deployApplication(Long applicationId) {
    Application application = getApplicationById(applicationId);
    List<Translation> translations = application.getTranslations();
    Map<String, String> translationMap =
        translations.stream().collect(Collectors.toMap(Translation::getKey, Translation::getValue));

    String sanitizedAppName = application.getName().replace(" ", "-").toLowerCase();
    File file = Paths.get(outputDirectory, sanitizedAppName + ".json").toFile();
    boolean isDirCreated = file.getParentFile().mkdirs();
    log.info(
        "{}",
        isDirCreated
            ? "created new directory successfully"
            : "directory already exists. skipping directory creation");
    try {
      objectMapper.writeValue(file, translationMap);
      log.info(
          "Successfully deployed application {} to {}",
          application.getName(),
          file.getAbsolutePath());
      application.setLastDeploymentDate(LocalDateTime.now());
      applicationRepository.save(application);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public byte[] downloadApplication(Long applicationId, HttpServletResponse response) {
    log.info("Preparing to download application");
    Application application = getApplicationById(applicationId);
    List<Translation> translations = application.getTranslations();

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Translations");

    int rowNum = 0;
    Row headerRow = sheet.createRow(rowNum++);
    headerRow.createCell(0).setCellValue("Key");
    headerRow.createCell(1).setCellValue("Value");

    for (Translation translation : translations) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(translation.getKey());
      row.createCell(1).setCellValue(translation.getValue());
    }

    String sanitizedAppName = application.getName().replace(" ", "-").toLowerCase();
    System.out.println(sanitizedAppName);
    response.setHeader("Content-Disposition", "attachment; filename=" + sanitizedAppName + ".xlsx");
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    try  {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      workbook.write(out);
      workbook.close();
      return out.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("Failed to create Excel file", e);
    }
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
