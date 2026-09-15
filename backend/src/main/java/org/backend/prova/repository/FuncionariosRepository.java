package org.backend.prova.repository;

import org.backend.prova.model.FuncionariosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FuncionariosRepository extends JpaRepository<FuncionariosModel, Long> {

    Optional<FuncionariosModel> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    @Query("SELECT DISTINCT f FROM FuncionariosModel f " +
            "LEFT JOIN FETCH f.vinculos v " +
            "LEFT JOIN FETCH v.cargo " +
            "LEFT JOIN FETCH v.departamento")
    List<FuncionariosModel> findAllWithVinculos();

    @Query("SELECT f FROM FuncionariosModel f " +
            "LEFT JOIN FETCH f.vinculos v " +
            "LEFT JOIN FETCH v.cargo " +
            "LEFT JOIN FETCH v.departamento " +
            "WHERE f.cpf = :cpf")
    Optional<FuncionariosModel> findByCpfWithVinculos(@Param("cpf") String cpf);

    @Query("SELECT DISTINCT f FROM FuncionariosModel f " +
            "LEFT JOIN FETCH f.vinculos v " +
            "LEFT JOIN FETCH v.cargo c " +
            "LEFT JOIN FETCH v.departamento d " +
            "WHERE (:nome IS NULL OR LOWER(f.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:cpf IS NULL OR f.cpf LIKE CONCAT('%', :cpf, '%')) " +
            "AND (:matricula IS NULL OR LOWER(v.matricula) LIKE LOWER(CONCAT('%', :matricula, '%'))) " +
            "AND (:empresa IS NULL OR LOWER(v.empresa) LIKE LOWER(CONCAT('%', :empresa, '%'))) " +
            "AND (:cargo IS NULL OR LOWER(c.descricaoDoCargo) LIKE LOWER(CONCAT('%', :cargo, '%'))) " +
            "AND (:departamento IS NULL OR LOWER(d.descricaoDoDepartamento) LIKE LOWER(CONCAT('%', :departamento, '%')))")
    List<FuncionariosModel> filtrarTodos(
            @Param("nome") String nome,
            @Param("cpf") String cpf,
            @Param("matricula") String matricula,
            @Param("empresa") String empresa,
            @Param("cargo") String cargo,
            @Param("departamento") String departamento);
}
