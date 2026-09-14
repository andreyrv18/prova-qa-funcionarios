package org.backend.prova.controller;

import org.backend.prova.dto.CargosDTO;
import org.backend.prova.exception.RecursoNaoEncontradoException;
import org.backend.prova.exception.RegistroDuplicadoException;
import org.backend.prova.exception.RegraDeNegocioException;
import org.backend.prova.model.CargosModel;
import org.backend.prova.repository.CargosRepository;
import org.backend.prova.service.CargosService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargoServiceTest {

  @Mock private CargosRepository cargoRepository;

  @InjectMocks private CargosService cargoService;

  @Test
  void salvarDeveLancarRegraDeNegocioExceptionQuandoCodigoDuplicado() {
    CargosModel modelExistente = new CargosModel();
    modelExistente.setCodigoDoCargo("DEV");
    modelExistente.setDescricaoDoCargo("Desenvolvedor");

    CargosDTO dto = new CargosDTO(modelExistente);
    when(cargoRepository.existsByCodigoDoCargo("DEV")).thenReturn(true);

      RegistroDuplicadoException exception =
        assertThrows(RegistroDuplicadoException.class, () -> cargoService.salvar(dto));

    assertEquals("Já existe um cargo com este código", exception.getMessage());

    verify(cargoRepository, never()).save(any());
  }

  @Test
  void listarTodosDeveLancarRegraDeNegocioException() {}

  @Test
  void buscarPorCodigoDeveRetornarCargosDTOQuandoExistir() {
    String codigoExistente = "SuporteN2";

    CargosModel model = new CargosModel();
    model.setId(2L);
    model.setCodigoDoCargo(codigoExistente);
    model.setDescricaoDoCargo("Tecnico Suporte N2");
    when(cargoRepository.findByCodigoDoCargo(codigoExistente)).thenReturn(Optional.of(model));

    CargosDTO resultado = cargoService.buscarCargoPorCodigo(codigoExistente);

    assertNotNull(resultado);
    assertEquals("SuporteN2", resultado.getCodigoDoCargo());
    assertEquals("Tecnico Suporte N2", resultado.getDescricaoDoCargo());

    verify(cargoRepository, times(1)).findByCodigoDoCargo(codigoExistente);
  }

  @Test
  void buscarPorCodigoDeveLancarRecursoNaoEncontradoExceptionQuandoNaoExiste() {
    String codigoInexistente = "Prod97";

    when(cargoRepository.findByCodigoDoCargo(codigoInexistente)).thenReturn(Optional.empty());

    RecursoNaoEncontradoException exception =
        assertThrows(
            RecursoNaoEncontradoException.class,
            () -> cargoService.buscarCargoPorCodigo(codigoInexistente));

    assertEquals("Não existe um cargo com este código", exception.getMessage());
  }

  @Test
  void listarTodosDeveRetornarListaVaziaQuandoNaoHouverCargos() {
    when(cargoRepository.findAll()).thenReturn(Collections.emptyList());

    List<CargosDTO> resultado = cargoService.listaTodosCargos();

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
    verify(cargoRepository, times(1)).findAll();
  }

  @Test
  void listarPaginadoDeveRetornarPageDeDTOs() {
    Pageable pageable = PageRequest.of(0, 10);

    CargosModel model = new CargosModel();
    model.setId(1L);
    model.setCodigoDoCargo("DEV");
    model.setDescricaoDoCargo("Desenvolvedor");

    Page<CargosModel> pageModel = new PageImpl<>(List.of(model), pageable, 1);

    when(cargoRepository.findAll(pageable)).thenReturn(pageModel);

    Page<CargosDTO> resultado = cargoService.listarPaginado(pageable);

    assertNotNull(resultado);
    assertEquals(1, resultado.getTotalElements());
    assertEquals("DEV", resultado.getContent().getFirst().getCodigoDoCargo());

    verify(cargoRepository).findAll(pageable);
  }

  @Test
  void pesquisarPorDescricaoDeveChamarRepositorioComFiltroCorreto() {
    String termoDeBusca = "env";

    CargosModel model = new CargosModel();
    model.setId(1L);
    model.setCodigoDoCargo("DEV");
    model.setDescricaoDoCargo("Desenvolvedor");

    when(cargoRepository.findByDescricaoDoCargoContainingIgnoreCase(anyString()))
        .thenReturn(List.of(model));

    List<CargosDTO> resultado = cargoService.pesquisarPorDescricao(termoDeBusca);

    assertNotNull(resultado);
    assertEquals(1, resultado.size());
    assertEquals("DEV", resultado.getFirst().getCodigoDoCargo());

    verify(cargoRepository).findByDescricaoDoCargoContainingIgnoreCase("env");
  }
}
