package br.com.vcruz.MoonProject.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity {
  @CreatedDate
  @Column(nullable = false)
  private final LocalDateTime createdDate = LocalDateTime.now();

  @LastModifiedDate
  private LocalDateTime lastModifiedDate;
}
