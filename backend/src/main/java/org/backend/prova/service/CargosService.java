package org.backend.prova.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.backend.prova.exception.RecursoNaoEncontradoException;
import org.backend.prova.exception.RegraDeNegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.backend.prova.dto.CargosDTO;
import org.backend.prova.model.CargosModel;
import org.backend.prova.repository.CargosRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CargosService {
  private final CargosRepository cargosRepository;
  private static final Logger log = LoggerFactory.getLogger(CargosService.class);

  public CargosService(CargosRepository cargosRepository) {
    this.cargosRepository = cargosRepository;
  }

  public List<CargosDTO> listaTodosCargos() throws RecursoNaoEncontradoException {
    List<CargosModel> listaTodosCargos = cargosRepository.findAll();

    List<CargosDTO> listaDto = listaTodosCargos.stream().map(CargosDTO::new).toList();

    return listaDto;
  }

  public CargosDTO buscarCargoPorCodigo(String codigoCargo) throws RecursoNaoEncontradoException {
    Optional<CargosModel> optionalCargo = cargosRepository.findByCodigoDoCargo(codigoCargo);

    if (optionalCargo.isEmpty()) {
      log.warn("Usuario tentou buscar um cargo com código inexistente {}", codigoCargo);
      throw new RecursoNaoEncontradoException("Não existe um cargo com este código");
    }

    return new CargosDTO(optionalCargo.get());
  }

  public List<CargosDTO> pesquisarPorDescricao(String descricao) {

    if (descricao == null || descricao.trim().isEmpty()) {
      return Collections.emptyList();
    }
    List<CargosModel> models =
        cargosRepository.findByDescricaoDoCargoContainingIgnoreCase(descricao);

    return models.stream().map(CargosDTO::new).toList();
  }

  public CargosDTO salvar(CargosDTO cargosDTO) throws RegraDeNegocioException {

    boolean cargoExist = cargosRepository.existsByCodigoDoCargo(cargosDTO.getCodigoDoCargo());

    if (cargoExist) {
      log.warn(
          "Usuario tentou cadastrar um cargo com código duplicado {}",
          cargosDTO.getCodigoDoCargo());
      throw new RegraDeNegocioException("Já existe um cargo com este código");
    }

    CargosModel modelParaSalvar = new CargosModel();
    modelParaSalvar.setCodigoDoCargo(cargosDTO.getCodigoDoCargo());
    modelParaSalvar.setDescricaoDoCargo(cargosDTO.getDescricaoDoCargo());

    CargosModel modelSalvo = cargosRepository.save(modelParaSalvar);

    log.info("Novo cargo Criado com código: {}", cargosDTO.getCodigoDoCargo());

    return new CargosDTO(modelSalvo);
  }

  public Page<CargosDTO> listarPaginado(Pageable pageable) {
    Page<CargosModel> pageModel = cargosRepository.findAll(pageable);
    return pageModel.map(CargosDTO::new);
  }
}
