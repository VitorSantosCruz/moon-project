package br.com.vcruz.MoonProject.action;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import br.com.vcruz.MoonProject.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "actions")
public class Action {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedDate
  @Column(nullable = false)
  private final LocalDateTime createdDate = LocalDateTime.now();

  private String type;

  private String resource;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = true)
  private User user;
}
