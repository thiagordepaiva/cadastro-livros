package com.spassu.cadastro_livros.controller;

import com.spassu.cadastro_livros.model.Livro;
import com.spassu.cadastro_livros.service.LivroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroEndpoint {

    private final LivroService livroService;

    private static final Logger log = LoggerFactory.getLogger(LivroEndpoint.class);

    public LivroEndpoint(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<Livro> criarLivro(@RequestBody Livro livro) {
        return ResponseEntity.ok(livroService.salvar(livro));
    }

    @GetMapping
    public ResponseEntity<List<Livro>> listarLivros() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @GetMapping("/{cod}")
    public ResponseEntity<Livro> buscarPorCod(@PathVariable Integer cod) {
        return ResponseEntity.ok(livroService.buscarPorCod(cod));
    }

    @DeleteMapping("/{cod}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Integer cod) {
        livroService.deletar(cod);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/listagem", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarRelatorio() {
        try {
            byte[] pdf = livroService.gerarListagemLivros();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=relatorio-listagem-livros.pdf");

            return ResponseEntity.ok().headers(headers).body(pdf);
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de listagem de livros", e);

            throw new IllegalStateException("Erro ao gerar relatório de listagem de livros, " +
                    "tente novamente mais tarde ou entre em contato com o administrador do sistema!");
        }
    }
}
