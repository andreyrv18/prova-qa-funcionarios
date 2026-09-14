package org.backend.prova.service;

import org.backend.prova.dto.DepartamentosDTO;
import org.backend.prova.exception.RecursoNaoEncontradoException;
import org.backend.prova.exception.RegistroDuplicadoException;
import org.backend.prova.model.DepartamentosModel;
import org.backend.prova.repository.DepartamentosRepository;
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
class DepartamentoServiceTest {

    @Mock private DepartamentosRepository DepartamentoRepository;

    @InjectMocks private DepartamentosService DepartamentoService;

    @Test
    void salvarDeveLancarRegraDeNegocioExceptionQuandoCodigoDuplicado() {
        DepartamentosModel modelExistente = new DepartamentosModel();
        modelExistente.setCodigoDoDepartamento("DEV");
        modelExistente.setDescricaoDoDepartamento("Desenvolvedor");

        DepartamentosDTO dto = new DepartamentosDTO(modelExistente);
        when(DepartamentoRepository.existsByCodigoDoDepartamento("DEV")).thenReturn(true);

        RegistroDuplicadoException exception =
                assertThrows(RegistroDuplicadoException.class, () -> DepartamentoService.salvar(dto));

        assertEquals("Já existe um Departamento com este código", exception.getMessage());

        verify(DepartamentoRepository, never()).save(any());
    }

    @Test
    void listarTodosDeveLancarRegraDeNegocioException() {}

    @Test
    void buscarPorCodigoDeveRetornarDepartamentosDTOQuandoExistir() {
        String codigoExistente = "SuporteN2";

        DepartamentosModel model = new DepartamentosModel();
        model.setId(2L);
        model.setCodigoDoDepartamento(codigoExistente);
        model.setDescricaoDoDepartamento("Tecnico Suporte N2");
        when(DepartamentoRepository.findByCodigoDoDepartamento(codigoExistente)).thenReturn(Optional.of(model));

        DepartamentosDTO resultado = DepartamentoService.buscarDepartamentoPorCodigo(codigoExistente);

        assertNotNull(resultado);
        assertEquals("SuporteN2", resultado.getCodigoDoDepartamento());
        assertEquals("Tecnico Suporte N2", resultado.getDescricaoDoDepartamento());

        verify(DepartamentoRepository, times(1)).findByCodigoDoDepartamento(codigoExistente);
    }

    @Test
    void buscarPorCodigoDeveLancarRecursoNaoEncontradoExceptionQuandoNaoExiste() {
        String codigoInexistente = "Prod97";

        when(DepartamentoRepository.findByCodigoDoDepartamento(codigoInexistente)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception =
                assertThrows(
                        RecursoNaoEncontradoException.class,
                        () -> DepartamentoService.buscarDepartamentoPorCodigo(codigoInexistente));

        assertEquals("Não existe um Departamento com este código", exception.getMessage());
    }

    @Test
    void listarTodosDeveRetornarListaVaziaQuandoNaoHouverDepartamentos() {
        when(DepartamentoRepository.findAll()).thenReturn(Collections.emptyList());

        List<DepartamentosDTO> resultado = DepartamentoService.listaTodosDepartamentos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(DepartamentoRepository, times(1)).findAll();
    }

    @Test
    void listarPaginadoDeveRetornarPageDeDTOs() {
        Pageable pageable = PageRequest.of(0, 10);

        DepartamentosModel model = new DepartamentosModel();
        model.setId(1L);
        model.setCodigoDoDepartamento("DEV");
        model.setDescricaoDoDepartamento("Desenvolvedor");

        Page<DepartamentosModel> pageModel = new PageImpl<>(List.of(model), pageable, 1);

        when(DepartamentoRepository.findAll(pageable)).thenReturn(pageModel);

        Page<DepartamentosDTO> resultado = DepartamentoService.listarPaginado(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("DEV", resultado.getContent().getFirst().getCodigoDoDepartamento());

        verify(DepartamentoRepository).findAll(pageable);
    }

    @Test
    void pesquisarPorDescricaoDeveChamarRepositorioComFiltroCorreto() {
        String termoDeBusca = "env";

        DepartamentosModel model = new DepartamentosModel();
        model.setId(1L);
        model.setCodigoDoDepartamento("DEV");
        model.setDescricaoDoDepartamento("Desenvolvedor");

        when(DepartamentoRepository.findByDescricaoDoDepartamentoContainingIgnoreCase(anyString()))
                .thenReturn(List.of(model));

        List<DepartamentosDTO> resultado = DepartamentoService.pesquisarPorDescricao(termoDeBusca);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("DEV", resultado.getFirst().getCodigoDoDepartamento());

        verify(DepartamentoRepository).findByDescricaoDoDepartamentoContainingIgnoreCase("env");
    }
}
