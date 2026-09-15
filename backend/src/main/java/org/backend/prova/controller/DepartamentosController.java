package org.backend.prova.controller;

import org.backend.prova.dto.DepartamentosDTO;
import org.backend.prova.exception.RecursoNaoEncontradoException;
import org.backend.prova.exception.RegraDeNegocioException;
import org.backend.prova.model.DepartamentosModel;
import org.backend.prova.repository.DepartamentosRepository;
import org.backend.prova.service.DepartamentosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/departamentos")
public class DepartamentosController {

  private final DepartamentosService departamentosService;
  private final DepartamentosRepository departamentosRepository;

  public DepartamentosController(
      DepartamentosService departamentosService, DepartamentosRepository departamentosRepository) {
    this.departamentosService = departamentosService;
    this.departamentosRepository = departamentosRepository;
  }

  @GetMapping("/listar")
  public ResponseEntity<List<DepartamentosDTO>> getAllDepartamentos()
      throws RecursoNaoEncontradoException {
    List<DepartamentosDTO> listaTodosDepartamentos = departamentosService.listaTodosDepartamentos();

    return ResponseEntity.ok().body(listaTodosDepartamentos);
  }

  @PostMapping("/criar")
  public ResponseEntity<DepartamentosDTO> criarDepartamento(
      @RequestBody DepartamentosDTO departamento) throws RegraDeNegocioException {
    DepartamentosDTO departamentoSalvo = departamentosService.salvar(departamento);

    return ResponseEntity.status(HttpStatus.CREATED).body(departamentoSalvo);
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<DepartamentosDTO> buscarDepartamentoPorCodigo(@PathVariable String codigo)
      throws RegraDeNegocioException {
    DepartamentosDTO buscarDepartamentoPorCodigo =
        departamentosService.buscarDepartamentoPorCodigo(codigo);

    return ResponseEntity.ok().body(buscarDepartamentoPorCodigo);
  }

  @GetMapping("/paginado")
  public ResponseEntity<Page<DepartamentosDTO>> listarPaginado(Pageable pageable)
      throws RecursoNaoEncontradoException {
    Page<DepartamentosDTO> listaPaginado = departamentosService.listarPaginado(pageable);
    return ResponseEntity.ok().body(listaPaginado);
  }

  @GetMapping("/pesquisar")
  public ResponseEntity<?> pesquisaDepartamento(
      @RequestParam(required = false) String descricaoDoDepartamento) {

    List<DepartamentosDTO> resultado =
        departamentosService.pesquisarPorDescricao(descricaoDoDepartamento);
    return ResponseEntity.ok(resultado);
  }

  @PutMapping("/{codigoDoDepartamento}")
  public ResponseEntity<?> editarDepartamento(
      @PathVariable String codigoDoDepartamento, @RequestBody DepartamentosDTO departamentosDTO) {
    DepartamentosModel editarDepartamento =
        departamentosService.atualizarDepartamentoPeloCodigo(
            codigoDoDepartamento, departamentosDTO);

    DepartamentosDTO respostaDTO = new DepartamentosDTO(editarDepartamento);
    return ResponseEntity.ok().body(respostaDTO);
  }

  @DeleteMapping("/{codigoDoDepartamento}")
  public ResponseEntity<Void> deletarDepartamento(@PathVariable String codigoDoDepartamento) {
    departamentosService.deletar(codigoDoDepartamento);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/relatorio")
  public ResponseEntity<byte[]> gerarRelatorio(@RequestParam(required = false) String filtro) {
    List<DepartamentosModel> departamentos = departamentosRepository.filtrarTodos(filtro);

    StringBuilder csv = new StringBuilder();
    csv.append("\uFEFF");
    csv.append("Código;Descrição\n");

    for (DepartamentosModel d : departamentos) {
      csv.append(d.getCodigoDoDepartamento())
          .append(";")
          .append(d.getDescricaoDoDepartamento())
          .append("\n");
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentDispositionFormData("attachment", "relatorio_departamentos.csv");
    headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

    return ResponseEntity.ok()
        .headers(headers)
        .body(csv.toString().getBytes(StandardCharsets.UTF_8));
  }
}
