package com.adi.translator.persistence.repository;

import com.adi.translator.persistence.entity.AppTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppTranslationRepository extends JpaRepository<AppTranslation, Long> {}
