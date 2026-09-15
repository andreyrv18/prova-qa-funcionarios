package org.backend.prova.service;

import org.backend.prova.dto.VinculosDTO;
import org.backend.prova.exception.RecursoNaoEncontradoException;
import org.backend.prova.exception.RegraDeNegocioException;
import org.backend.prova.model.CargosModel;
import org.backend.prova.model.DepartamentosModel;
import org.backend.prova.model.FuncionariosModel;
import org.backend.prova.model.VinculosModel;
import org.backend.prova.repository.CargosRepository;
import org.backend.prova.repository.DepartamentosRepository;
import org.backend.prova.repository.FuncionariosRepository;
import org.backend.prova.repository.VinculosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VinculosService {

    private final VinculosRepository vinculosRepository;
    private final FuncionariosRepository funcionariosRepository;
    private final CargosRepository cargosRepository;
    private final DepartamentosRepository departamentosRepository;
    private static final Logger log = LoggerFactory.getLogger(VinculosService.class);

    public VinculosService(VinculosRepository vinculosRepository,
                           FuncionariosRepository funcionariosRepository,
                           CargosRepository cargosRepository,
                           DepartamentosRepository departamentosRepository) {
        this.vinculosRepository = vinculosRepository;
        this.funcionariosRepository = funcionariosRepository;
        this.cargosRepository = cargosRepository;
        this.departamentosRepository = departamentosRepository;
    }

    @Transactional(readOnly = true)
    public List<VinculosDTO> listarPorFuncionario(Long funcionarioId) {
        return vinculosRepository.findByFuncionarioId(funcionarioId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VinculosDTO salvar(Long funcionarioId, VinculosDTO dto) throws RegraDeNegocioException {
        FuncionariosModel funcionario = funcionariosRepository.findById(funcionarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Funcionário com ID " + funcionarioId + " não encontrado"));

        CargosModel cargo = cargosRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cargo com ID " + dto.getCargoId() + " não encontrado"));

        DepartamentosModel departamento = departamentosRepository.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Departamento com ID " + dto.getDepartamentoId() + " não encontrado"));

        // Verifica se matrícula já existe
        if (vinculosRepository.existsByMatricula(dto.getMatricula())) {
            throw new RegraDeNegocioException("Já existe um vínculo com esta matrícula");
        }

        VinculosModel vinculo = new VinculosModel(
                dto.getEmpresa(),
                dto.getMatricula(),
                cargo,
                departamento,
                funcionario
        );

        VinculosModel salvo = vinculosRepository.save(vinculo);
        log.info("Vínculo criado para funcionário ID {}: {}", funcionarioId, salvo.getMatricula());
        return toDTO(salvo);
    }

    @Transactional
    public VinculosDTO atualizar(Long id, VinculosDTO dto) throws RegraDeNegocioException {
        VinculosModel vinculo = vinculosRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Vínculo com ID " + id + " não encontrado"));

        CargosModel cargo = cargosRepository.findById(dto.getCargoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cargo com ID " + dto.getCargoId() + " não encontrado"));

        DepartamentosModel departamento = departamentosRepository.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Departamento com ID " + dto.getDepartamentoId() + " não encontrado"));

        // Verifica se matrícula foi alterada e se já existe
        if (!vinculo.getMatricula().equals(dto.getMatricula())) {
            if (vinculosRepository.existsByMatricula(dto.getMatricula())) {
                throw new RegraDeNegocioException("Já existe um vínculo com esta matrícula");
            }
        }

        vinculo.setEmpresa(dto.getEmpresa());
        vinculo.setMatricula(dto.getMatricula());
        vinculo.setCargo(cargo);
        vinculo.setDepartamento(departamento);

        VinculosModel atualizado = vinculosRepository.save(vinculo);
        log.info("Vínculo atualizado: ID {}", id);
        return toDTO(atualizado);
    }

    @Transactional
    public void deletar(Long id) throws RegraDeNegocioException {
        VinculosModel vinculo = vinculosRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Vínculo com ID " + id + " não encontrado"));

        Long funcionarioId = vinculo.getFuncionario().getId();
        long totalVinculos = vinculosRepository.countByFuncionarioId(funcionarioId);

        // Regra: não pode excluir o último vínculo
        if (totalVinculos <= 1) {
            throw new RegraDeNegocioException(
                    "Não é permitido excluir o último vínculo de um funcionário ativo");
        }

        vinculosRepository.deleteById(id);
        log.info("Vínculo excluído (soft delete): ID {}", id);
    }

    public VinculosDTO toDTO(VinculosModel model) {
        return new VinculosDTO(
                model.getId(),
                model.getEmpresa(),
                model.getMatricula(),
                model.getCargo().getId(),
                model.getCargo().getDescricaoDoCargo(),
                model.getDepartamento().getId(),
                model.getDepartamento().getDescricaoDoDepartamento()
        );
    }
}
