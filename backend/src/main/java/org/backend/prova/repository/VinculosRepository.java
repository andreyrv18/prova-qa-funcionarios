package org.backend.prova.repository;

import org.backend.prova.model.VinculosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VinculosRepository extends JpaRepository<VinculosModel, Long> {

    List<VinculosModel> findByFuncionarioId(Long funcionarioId);

    boolean existsByMatricula(String matricula);

    @Query("SELECT COUNT(v) FROM VinculosModel v WHERE v.funcionario.id = :funcionarioId")
    long countByFuncionarioId(@Param("funcionarioId") Long funcionarioId);
}
