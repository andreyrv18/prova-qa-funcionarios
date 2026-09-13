package org.backend.prova.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@Table(name = "departamentos")
public class DepartamentosModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private @Nullable Long id;
}
