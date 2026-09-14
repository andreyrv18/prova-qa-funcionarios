package org.backend.prova.repository;

import org.backend.prova.model.CargosModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CargosRepository extends JpaRepository<CargosModel, Long> {
  boolean existsByCodigoDoCargo(String codigoDoCargo);

  Optional<CargosModel> findByCodigoDoCargo(String codigoDoCargo);

  List<CargosModel> findByDescricaoDoCargoContainingIgnoreCase(String descricao);
}
