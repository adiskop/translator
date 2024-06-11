package com.adi.translator.persistence.repository;

import com.adi.translator.persistence.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRepository extends JpaRepository<Translation, Long> {}
