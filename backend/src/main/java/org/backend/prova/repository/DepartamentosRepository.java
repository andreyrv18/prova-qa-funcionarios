package org.backend.prova.repository;

import org.backend.prova.model.DepartamentosModel;
import org.backend.prova.model.DepartamentosModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartamentosRepository extends JpaRepository<DepartamentosModel, Long> { boolean existsByCodigoDoDepartamento(String codigoDoDepartamento);

    Optional<DepartamentosModel> findByCodigoDoDepartamento(String codigoDoDepartamento);

    List<DepartamentosModel> findByDescricaoDoDepartamentoContainingIgnoreCase(String descricao);
}
