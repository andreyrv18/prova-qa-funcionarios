package org.backend.prova.repository;

import org.backend.prova.model.CargosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CargosRepository extends JpaRepository<CargosModel, Long> {
    boolean existsByCodigoDoCargo(String codigoDoCargo);

    Optional<CargosModel> findByCodigoDoCargo(String codigoDoCargo);

    List<CargosModel> findByDescricaoDoCargoContainingIgnoreCase(String descricao);

    @Query("SELECT c FROM CargosModel c WHERE :filtro IS NULL OR " +
            "LOWER(c.descricaoDoCargo) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.codigoDoCargo) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<CargosModel> filtrarTodos(@Param("filtro") String filtro);
}
