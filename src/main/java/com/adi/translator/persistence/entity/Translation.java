package com.adi.translator.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "application")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Application extends BaseEntity{
  @Column(nullable = false)
  private String name;
  private LocalDateTime lastDeploymentDate;
  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Translation> translations;
}
