package org.backend.prova.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vinculo")
public class VinculosModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
}
