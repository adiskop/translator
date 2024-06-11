package com.adi.translator.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.adi.translator.model.request.CreateApplicationRequest;
import com.adi.translator.persistence.entity.Application;
import com.adi.translator.service.ApplicationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
  public Application createApplication(@RequestBody CreateApplicationRequest application) {
    return applicationService.createApplication(application);
  }

  @GetMapping(value = "/{applicationId}")
  @Operation(description = "Get application by ID")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Application.class)})
  public Application getAllApplications(@PathVariable Long applicationId) {
    return applicationService.getApplicationById(applicationId);
  }
}
