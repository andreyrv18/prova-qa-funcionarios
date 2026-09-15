package org.backend.prova.controller;

import org.backend.prova.dto.FuncionariosDTO;
import org.backend.prova.dto.VinculosDTO;
import org.backend.prova.exception.RecursoNaoEncontradoException;
import org.backend.prova.exception.RegraDeNegocioException;
import org.backend.prova.service.FuncionariosService;
import org.backend.prova.service.VinculosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionariosController {

    private final FuncionariosService funcionariosService;
    private final VinculosService vinculosService;

    public FuncionariosController(
            FuncionariosService funcionariosService,
            VinculosService vinculosService) {
        this.funcionariosService = funcionariosService;
        this.vinculosService = vinculosService;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<FuncionariosDTO>> listarTodos() {
        return ResponseEntity.ok(funcionariosService.listarTodos());
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<FuncionariosDTO>> listarPaginado(Pageable pageable)
            throws RecursoNaoEncontradoException {
        Page<FuncionariosDTO> listaPaginado = funcionariosService.listarPaginado(pageable);
        return ResponseEntity.ok().body(listaPaginado);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<FuncionariosDTO> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(funcionariosService.buscarPorCpf(cpf));
    }

    @PostMapping("/criar")
    public ResponseEntity<FuncionariosDTO> criar(@RequestBody FuncionariosDTO dto)
            throws RegraDeNegocioException {
        FuncionariosDTO criado = funcionariosService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<FuncionariosDTO> atualizar(
            @PathVariable String cpf, @RequestBody FuncionariosDTO dto) throws RegraDeNegocioException {
        return ResponseEntity.ok(funcionariosService.atualizar(cpf, dto));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletar(@PathVariable String cpf) {
        funcionariosService.deletar(cpf);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{funcionarioId}/vinculos")
    public ResponseEntity<List<VinculosDTO>> listarVinculos(@PathVariable Long funcionarioId) {
        return ResponseEntity.ok(vinculosService.listarPorFuncionario(funcionarioId));
    }

    @PostMapping("/{funcionarioId}/vinculos")
    public ResponseEntity<VinculosDTO> criarVinculo(
            @PathVariable Long funcionarioId, @RequestBody VinculosDTO dto)
            throws RegraDeNegocioException {
        VinculosDTO criado = vinculosService.salvar(funcionarioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{funcionarioId}/vinculos/{vinculoId}")
    public ResponseEntity<VinculosDTO> atualizarVinculo(
            @PathVariable Long funcionarioId, @PathVariable Long vinculoId, @RequestBody VinculosDTO dto)
            throws RegraDeNegocioException {
        return ResponseEntity.ok(vinculosService.atualizar(vinculoId, dto));
    }

    @DeleteMapping("/{funcionarioId}/vinculos/{vinculoId}")
    public ResponseEntity<Void> deletarVinculo(
            @PathVariable Long funcionarioId, @PathVariable Long vinculoId)
            throws RegraDeNegocioException {
        vinculosService.deletar(vinculoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/relatorio")
    public ResponseEntity<byte[]> gerarRelatorio(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String matricula,
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) String departamento) {

        byte[] csvData =
                funcionariosService.gerarRelatorioCsv(nome, cpf, matricula, empresa, cargo, departamento);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "relatorio_funcionarios.csv");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

        return ResponseEntity.ok().headers(headers).body(csvData);
    }
}
