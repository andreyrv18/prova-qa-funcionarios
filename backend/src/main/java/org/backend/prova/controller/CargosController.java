package org.backend.prova.controller;

import org.backend.prova.dto.CargosDTO;
import org.backend.prova.exception.RecursoNaoEncontradoException;
import org.backend.prova.exception.RegraDeNegocioException;
import org.backend.prova.model.CargosModel;
import org.backend.prova.repository.CargosRepository;
import org.backend.prova.service.CargosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/cargos")
public class CargosController {

  private final CargosService cargosService;
    private final CargosRepository cargosRepository;

    public CargosController(CargosService cargosService, CargosRepository cargosRepository) {
    this.cargosService = cargosService;
        this.cargosRepository = cargosRepository;
    }

  @GetMapping("/listar")
  public ResponseEntity<List<CargosDTO>> getAllCargos() throws RecursoNaoEncontradoException {
    List<CargosDTO> listaTodosCargos = cargosService.listaTodosCargos();

    return ResponseEntity.ok().body(listaTodosCargos);
  }

  @PostMapping("/criar")
  public ResponseEntity<CargosDTO> criarCargo(@RequestBody CargosDTO cargo)
      throws RegraDeNegocioException {
    CargosDTO cargoSalvo = cargosService.salvar(cargo);

    return ResponseEntity.status(HttpStatus.CREATED).body(cargoSalvo);
  }

  @GetMapping("/{codigo}")
  public ResponseEntity<CargosDTO> buscarCargoPorCodigo(@PathVariable String codigo)
      throws RegraDeNegocioException {
    CargosDTO buscarCargoPorCodigo = cargosService.buscarCargoPorCodigo(codigo);

    return ResponseEntity.ok().body(buscarCargoPorCodigo);
  }

  @GetMapping("/paginado")
  public ResponseEntity<Page<CargosDTO>> listarPaginado(Pageable pageable)
      throws RecursoNaoEncontradoException {
    Page<CargosDTO> listaPaginado = cargosService.listarPaginado(pageable);
    return ResponseEntity.ok().body(listaPaginado);
  }

  @GetMapping("/pesquisar")
  public ResponseEntity<?> pesquisaCargo(@RequestParam(required = false) String descricaoDoCargo) {

    List<CargosDTO> resultado = cargosService.pesquisarPorDescricao(descricaoDoCargo);
    return ResponseEntity.ok(resultado);
  }

  @PutMapping("/{codigoDoCargo}")
  public ResponseEntity<?> editarCargo(
      @PathVariable String codigoDoCargo, @RequestBody CargosDTO cargosDTO) {
    CargosModel editarCargo = cargosService.atualizarCargoPeloCodigo(codigoDoCargo, cargosDTO);

    CargosDTO respostaDTO = new CargosDTO(editarCargo);
    return ResponseEntity.ok().body(respostaDTO);
  }

  @DeleteMapping("/{codigoDoCargo}")
  public ResponseEntity<Void> deletarCargo(@PathVariable String codigoDoCargo) {
    cargosService.deletar(codigoDoCargo);

    return ResponseEntity.noContent().build();
  }

    @GetMapping("/relatorio")
    public ResponseEntity<byte[]> gerarRelatorio(@RequestParam(required = false) String filtro) {
        List<CargosModel> cargos = cargosRepository.filtrarTodos(filtro);

        StringBuilder csv = new StringBuilder();
        csv.append("\uFEFF");
        csv.append("Código;Descrição\n");

        for (CargosModel c : cargos) {
            csv.append(c.getCodigoDoCargo()).append(";")
                    .append(c.getDescricaoDoCargo()).append("\n");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "relatorio_cargos.csv");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

        return ResponseEntity.ok().headers(headers).body(csv.toString().getBytes(StandardCharsets.UTF_8));
    }
}
