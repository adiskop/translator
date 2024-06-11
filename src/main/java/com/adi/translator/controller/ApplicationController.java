package com.adi.translator.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.adi.translator.model.request.CreateApplicationRequest;
import com.adi.translator.persistence.entity.Application;
import com.adi.translator.service.ApplicationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/application", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class ApplicationController {
  private final ApplicationService applicationService;

  @GetMapping
  @Operation(description = "Get all applications")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = Application.class,
        responseContainer = "list")
  })
  public List<Application> getAllApplications() {
    return applicationService.getAllApplications();
  }

  @PostMapping
  @Operation(description = "Create new application")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Application.class)})
  public Application createApplication(@Valid @RequestBody CreateApplicationRequest application) {
    return applicationService.createApplication(application);
  }

  @GetMapping(value = "/{applicationId}")
  @Operation(description = "Get application by ID")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Application.class)})
  public Application getAllApplications(@PathVariable Long applicationId) {
    return applicationService.getApplicationById(applicationId);
  }

  @PostMapping(value = "/{applicationId}/deploy")
  @Operation(description = "Deploy application")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Application.class)})
  public void deployApplication(@PathVariable Long applicationId) {
    applicationService.deployApplication(applicationId);
  }

  @GetMapping(value = "/{applicationId}/download")
  @Operation(description = "Download application")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Byte.class)})
  public ResponseEntity<byte[]> downloadApplication(
      @PathVariable Long applicationId, HttpServletResponse response) {
    System.out.println("First print");
    byte[] fileContent = applicationService.downloadApplication(applicationId, response);
//    System.out.println("Second print");
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
//    System.out.println("Something fishy but lets see");
    return new ResponseEntity<>(fileContent, HttpStatus.OK);
  }
}
