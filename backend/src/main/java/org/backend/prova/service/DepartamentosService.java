package org.backend.prova.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.backend.prova.exception.RecursoNaoEncontradoException;
import org.backend.prova.exception.RegistroDuplicadoException;
import org.backend.prova.exception.RegraDeNegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.backend.prova.dto.DepartamentosDTO;
import org.backend.prova.model.DepartamentosModel;
import org.backend.prova.repository.DepartamentosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DepartamentosService {
    private final DepartamentosRepository DepartamentosRepository;
    private static final Logger log = LoggerFactory.getLogger(DepartamentosService.class);

    public DepartamentosService(DepartamentosRepository DepartamentosRepository) {
        this.DepartamentosRepository = DepartamentosRepository;
    }

    public List<DepartamentosDTO> listaTodosDepartamentos() throws RecursoNaoEncontradoException {
        List<DepartamentosModel> listaTodosDepartamentos = DepartamentosRepository.findAll();

        return listaTodosDepartamentos.stream().map(DepartamentosDTO::new).toList();
    }

    public DepartamentosDTO buscarDepartamentoPorCodigo(String codigoDepartamento) throws RecursoNaoEncontradoException {
        Optional<DepartamentosModel> optionalDepartamento = DepartamentosRepository.findByCodigoDoDepartamento(codigoDepartamento);

        if (optionalDepartamento.isEmpty()) {
            log.warn("Usuario tentou buscar um Departamento com código inexistente {}", codigoDepartamento);
            throw new RecursoNaoEncontradoException("Não existe um Departamento com este código");
        }

        return new DepartamentosDTO(optionalDepartamento.get());
    }

    public List<DepartamentosDTO> pesquisarPorDescricao(String descricao) {

        if (descricao == null || descricao.trim().isEmpty()) {
            return Collections.emptyList();
        }
        List<DepartamentosModel> models =
                DepartamentosRepository.findByDescricaoDoDepartamentoContainingIgnoreCase(descricao);

        return models.stream().map(DepartamentosDTO::new).toList();
    }

    public DepartamentosDTO salvar(DepartamentosDTO DepartamentosDTO) throws RegraDeNegocioException {

        boolean DepartamentoExist = DepartamentosRepository.existsByCodigoDoDepartamento(DepartamentosDTO.getCodigoDoDepartamento());

        if (DepartamentoExist) {
            log.warn(
                    "Usuario tentou cadastrar um Departamento com código duplicado {}",
                    DepartamentosDTO.getCodigoDoDepartamento());
            throw new RegistroDuplicadoException("Já existe um Departamento com este código");
        }

        DepartamentosModel modelParaSalvar = new DepartamentosModel();
        modelParaSalvar.setCodigoDoDepartamento(DepartamentosDTO.getCodigoDoDepartamento());
        modelParaSalvar.setDescricaoDoDepartamento(DepartamentosDTO.getDescricaoDoDepartamento());

        DepartamentosModel modelSalvo = DepartamentosRepository.save(modelParaSalvar);

        log.info("Novo Departamento Criado com código: {}", DepartamentosDTO.getCodigoDoDepartamento());

        return new DepartamentosDTO(modelSalvo);
    }

    public Page<DepartamentosDTO> listarPaginado(Pageable pageable) {
        Page<DepartamentosModel> pageModel = DepartamentosRepository.findAll(pageable);
        return pageModel.map(DepartamentosDTO::new);
    }

    public DepartamentosModel atualizarDepartamentoPeloCodigo(String codigoDoDepartamento, DepartamentosDTO DepartamentosDTO) {
        Optional<DepartamentosModel> DepartamentoExistente = DepartamentosRepository.findByCodigoDoDepartamento(codigoDoDepartamento);
        if (DepartamentoExistente.isEmpty()) {
            throw new RecursoNaoEncontradoException(
                    "Departamento não encontrado com o código: " + codigoDoDepartamento);
        }
        DepartamentoExistente.get().setCodigoDoDepartamento(DepartamentosDTO.getCodigoDoDepartamento());
        DepartamentoExistente.get().setDescricaoDoDepartamento(DepartamentosDTO.getDescricaoDoDepartamento());

        return DepartamentosRepository.save(DepartamentoExistente.get());
    }

    public void deletar(String codigoDoDepartamento) {
        DepartamentosModel DepartamentosModel =
                DepartamentosRepository
                        .findByCodigoDoDepartamento(codigoDoDepartamento)
                        .orElseThrow(
                                () ->
                                        new RecursoNaoEncontradoException(
                                                "Departamento com codigo "
                                                        + codigoDoDepartamento
                                                        + " não encontrado ou já foi excluído."));

        DepartamentosRepository.deleteById(DepartamentosModel.getId());

        log.info("Departamento com ID {} marcado como excluído (Soft Delete).", codigoDoDepartamento);
    }
}
