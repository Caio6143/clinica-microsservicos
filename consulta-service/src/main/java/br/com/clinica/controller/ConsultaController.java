package br.com.clinica.controller;

import br.com.clinica.model.Consulta;
import br.com.clinica.service.ConsultaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = service;
    }

    // GET /consultas - Lista todos os agendamentos
    @GetMapping
    public List<Consulta> listar() {
        return service.listarTodas();
    }

    // GET /consultas/{id} - Busca uma consulta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Consulta> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /consultas - Cria um novo agendamento (Valida se o paciente existe na porta 8081)
    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody Consulta consulta) {
        try {
            Consulta novaConsulta = service.salvar(consulta);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaConsulta);
        } catch (IllegalArgumentException e) {
            // Se o paciente não existir no microsserviço 1, captura o erro e retorna HTTP 400
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /consultas/{id} - Atualiza uma consulta existente
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody Consulta consulta) {
        try {
            return ResponseEntity.ok(service.atualizar(id, consulta));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // DELETE /consultas/{id} - Remove uma consulta do sistema
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}