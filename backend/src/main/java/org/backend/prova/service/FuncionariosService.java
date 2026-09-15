package org.backend.prova.service;

import org.backend.prova.dto.FuncionariosDTO;
import org.backend.prova.dto.VinculosDTO;
import org.backend.prova.exception.RecursoNaoEncontradoException;
import org.backend.prova.exception.RegraDeNegocioException;
import org.backend.prova.model.FuncionariosModel;
import org.backend.prova.repository.FuncionariosRepository;
import org.backend.prova.util.CpfValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionariosService {

    private final FuncionariosRepository funcionariosRepository;
    private final VinculosService vinculosService;
    private static final Logger log = LoggerFactory.getLogger(FuncionariosService.class);

    public FuncionariosService(
            FuncionariosRepository funcionariosRepository, VinculosService vinculosService) {
        this.funcionariosRepository = funcionariosRepository;
        this.vinculosService = vinculosService;
    }

    @Transactional(readOnly = true)
    public List<FuncionariosDTO> listarTodos() {
        return funcionariosRepository.findAllWithVinculos().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuncionariosDTO buscarPorCpf(String cpf) {
        FuncionariosModel funcionario =
                funcionariosRepository
                        .findByCpfWithVinculos(cpf)
                        .orElseThrow(
                                () ->
                                        new RecursoNaoEncontradoException(
                                                "Funcionário com CPF " + cpf + " não encontrado"));
        return toDTO(funcionario);
    }

    @Transactional
    public FuncionariosDTO salvar(FuncionariosDTO dto) throws RegraDeNegocioException {
        if (!CpfValidator.isValid(dto.getCpf())) {
            throw new RegraDeNegocioException("CPF inválido");
        }

        if (funcionariosRepository.existsByCpf(dto.getCpf())) {
            throw new RegraDeNegocioException("Já existe um funcionário com este CPF");
        }

        FuncionariosModel funcionario = new FuncionariosModel(dto.getNome(), dto.getCpf());
        FuncionariosModel salvo = funcionariosRepository.save(funcionario);

        log.info("Funcionário criado: {} - CPF: {}", salvo.getNome(), salvo.getCpf());
        return toDTO(salvo);
    }

    @Transactional
    public FuncionariosDTO atualizar(String cpf, FuncionariosDTO dto) throws RegraDeNegocioException {
        FuncionariosModel funcionario =
                funcionariosRepository
                        .findByCpfWithVinculos(cpf)
                        .orElseThrow(
                                () ->
                                        new RecursoNaoEncontradoException(
                                                "Funcionário com CPF " + cpf + " não encontrado"));

        if (!funcionario.getCpf().equals(dto.getCpf())) {
            if (!CpfValidator.isValid(dto.getCpf())) {
                throw new RegraDeNegocioException("CPF inválido");
            }
            if (funcionariosRepository.existsByCpf(dto.getCpf())) {
                throw new RegraDeNegocioException("Já existe um funcionário com este CPF");
            }
        }

        funcionario.setNome(dto.getNome());
        funcionario.setCpf(dto.getCpf());

        FuncionariosModel atualizado = funcionariosRepository.save(funcionario);
        log.info("Funcionário atualizado: CPF {}", cpf);
        return toDTO(atualizado);
    }

    @Transactional
    public void deletar(String cpf) {
        FuncionariosModel funcionario =
                funcionariosRepository
                        .findByCpf(cpf)
                        .orElseThrow(
                                () ->
                                        new RecursoNaoEncontradoException(
                                                "Funcionário com CPF " + cpf + " não encontrado"));

        funcionariosRepository.deleteById(funcionario.getId());
        log.info("Funcionário excluído: CPF {}", cpf);
    }

    @Transactional(readOnly = true)
    public byte[] gerarRelatorioCsv(
            String nome,
            String cpf,
            String matricula,
            String empresa,
            String cargo,
            String departamento) {

        List<FuncionariosModel> funcionarios =
                funcionariosRepository.filtrarTodos(nome, cpf, matricula, empresa, cargo, departamento);

        StringBuilder csv = new StringBuilder();
        csv.append("\uFEFF"); // BOM UTF-8
        csv.append("Nome;CPF;Empresa;Matrícula;Cargo;Departamento\n");

        for (FuncionariosModel f : funcionarios) {
            if (f.getVinculos() != null && !f.getVinculos().isEmpty()) {
                f.getVinculos().forEach(v -> {
                    csv.append(sanitizar(f.getNome())).append(";")
                            .append(sanitizar(f.getCpf())).append(";")
                            .append(sanitizar(v.getEmpresa())).append(";")
                            .append(sanitizar(v.getMatricula())).append(";")
                            .append(sanitizar(v.getCargo() != null ? v.getCargo().getDescricaoDoCargo() : "")).append(";")
                            .append(sanitizar(v.getDepartamento() != null ? v.getDepartamento().getDescricaoDoDepartamento() : "")).append("\n");
                });
            } else {
                csv.append(sanitizar(f.getNome())).append(";")
                        .append(sanitizar(f.getCpf())).append(";;;;\n");
            }
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String sanitizar(String valor) {
        return valor != null ? valor.replace(";", ",") : "";
    }

    public Page<FuncionariosDTO> listarPaginado(Pageable pageable) {
        Page<FuncionariosModel> pageModel = funcionariosRepository.findAll(pageable);
        return pageModel.map(this::toDTO);
    }

    private FuncionariosDTO toDTO(FuncionariosModel model) {
        List<VinculosDTO> vinculosDTO =
                model.getVinculos().stream().map(vinculosService::toDTO).collect(Collectors.toList());

        return new FuncionariosDTO(model.getId(), model.getNome(), model.getCpf(), vinculosDTO);
    }
}
