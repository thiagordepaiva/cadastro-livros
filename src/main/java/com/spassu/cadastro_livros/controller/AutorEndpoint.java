package com.spassu.cadastro_livros.controller;

import com.spassu.cadastro_livros.model.Autor;
import com.spassu.cadastro_livros.service.AutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorEndpoint {

    private final AutorService autorService;

    public AutorEndpoint(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    public ResponseEntity<Autor> criarAutor(@RequestBody Autor autor) {
        return ResponseEntity.ok(autorService.salvar(autor));
    }

    @GetMapping
    public ResponseEntity<List<Autor>> listarAutors() {
        return ResponseEntity.ok(autorService.listarTodos());
    }

    @GetMapping("/{cod}")
    public ResponseEntity<Autor> buscarPorCod(@PathVariable Integer cod) {
        return ResponseEntity.ok(autorService.buscarPorCod(cod));
    }

    @DeleteMapping("/{cod}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Integer cod) {
        autorService.deletar(cod);
        return ResponseEntity.noContent().build();
    }
}
