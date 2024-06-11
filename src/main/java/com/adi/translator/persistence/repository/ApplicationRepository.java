package com.adi.translator.persistence.repository;

import com.adi.translator.persistence.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
  boolean existsByNameEqualsIgnoreCase(String name);
}
