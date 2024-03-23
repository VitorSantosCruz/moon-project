package br.com.vcruz.MoonProject.action;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

import br.com.vcruz.MoonProject.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "actions")
public class Action {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @CreatedDate
  @Column(nullable = false)
  private Date createdDate = new Date();

  private String type;

  private String details;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
}
