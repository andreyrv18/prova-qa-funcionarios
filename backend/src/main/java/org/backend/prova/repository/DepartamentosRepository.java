package org.backend.prova.repository;

import org.backend.prova.model.DepartamentosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartamentosRepository extends JpaRepository<DepartamentosModel, Long> {
    boolean existsByCodigoDoDepartamento(String codigoDoDepartamento);

    Optional<DepartamentosModel> findByCodigoDoDepartamento(String codigoDoDepartamento);

    List<DepartamentosModel> findByDescricaoDoDepartamentoContainingIgnoreCase(String descricao);

    @Query("SELECT d FROM DepartamentosModel d WHERE :filtro IS NULL OR " +
            "LOWER(d.descricaoDoDepartamento) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(d.codigoDoDepartamento) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<DepartamentosModel> filtrarTodos(@Param("filtro") String filtro);
}
