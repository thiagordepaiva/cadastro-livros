package com.spassu.cadastro_livros.controller;

import com.spassu.cadastro_livros.model.Assunto;
import com.spassu.cadastro_livros.service.AssuntoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assuntos")
public class AssuntoEndpoint {

    private final AssuntoService assuntoService;

    public AssuntoEndpoint(AssuntoService assuntoService) {
        this.assuntoService = assuntoService;
    }

    @PostMapping
    public ResponseEntity<Assunto> criarAssunto(@RequestBody Assunto assunto) {
        return ResponseEntity.ok(assuntoService.salvar(assunto));
    }

    @GetMapping
    public ResponseEntity<List<Assunto>> listarAssuntos() {
        return ResponseEntity.ok(assuntoService.listarTodos());
    }

    @GetMapping("/{cod}")
    public ResponseEntity<Assunto> buscarPorCod(@PathVariable Integer cod) {
        return ResponseEntity.ok(assuntoService.buscarPorCod(cod));
    }

    @DeleteMapping("/{cod}")
    public ResponseEntity<Void> deletarAssunto(@PathVariable Integer cod) {
        assuntoService.deletar(cod);
        return ResponseEntity.noContent().build();
    }
}
