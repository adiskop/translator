package com.adi.translator.persistence.entity;

import jakarta.persistence.*;
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
@Table(name = "app_translation")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class AppTranslation extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "application_id")
  private Application application;

  @ManyToOne
  @JoinColumn(name = "translation_id")
  private Translation translation;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    AppTranslation that = (AppTranslation) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
