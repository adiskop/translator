package com.adi.translator.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "application")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Application extends BaseEntity {
  @Column(nullable = false)
  private String name;

  private LocalDateTime lastDeploymentDate;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "app_translation",
      joinColumns = @JoinColumn(name = "application_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "translation_id", referencedColumnName = "id"))
  private List<Translation> translations;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Application that = (Application) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
