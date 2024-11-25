package com.spassu.cadastro_livros.controller;

import com.spassu.cadastro_livros.model.Livro;
import com.spassu.cadastro_livros.service.LivroService;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivroEndpointTest {

    @InjectMocks
    private LivroEndpoint livroEndpoint;

    @Mock
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarLivro_DeveRetornarLivroCriado() {
        Livro livro = new Livro(1);
        when(livroService.salvar(livro)).thenReturn(livro);

        ResponseEntity<Livro> response = livroEndpoint.criarLivro(livro);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(livro, response.getBody());
        verify(livroService, times(1)).salvar(livro);
    }

    @Test
    void testListarLivros_DeveRetornarListaDeLivros() {
        List<Livro> livros = Arrays.asList(
                new Livro(1),
                new Livro(2)
        );
        when(livroService.listarTodos()).thenReturn(livros);

        ResponseEntity<List<Livro>> response = livroEndpoint.listarLivros();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(livros, response.getBody());
        verify(livroService, times(1)).listarTodos();
    }

    @Test
    void testBuscarPorCod_DeveRetornarLivroQuandoCodExistir() {
        int cod = 1;
        Livro livro = new Livro(cod);
        when(livroService.buscarPorCod(cod)).thenReturn(livro);

        ResponseEntity<Livro> response = livroEndpoint.buscarPorCod(cod);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(livro, response.getBody());
        verify(livroService, times(1)).buscarPorCod(cod);
    }

    @Test
    void testBuscarPorCod_DeveRetornarErroQuandoCodNaoExistir() {
        int cod = 99;
        when(livroService.buscarPorCod(cod)).thenThrow(new RuntimeException("Livro não encontrado"));

        Exception exception = assertThrows(RuntimeException.class, () -> livroEndpoint.buscarPorCod(cod));
        assertEquals("Livro não encontrado", exception.getMessage());
        verify(livroService, times(1)).buscarPorCod(cod);
    }

    @Test
    void testDeletarLivro_DeveChamarServicoParaDeletar() {
        int cod = 1;
        doNothing().when(livroService).deletar(cod);

        ResponseEntity<Void> response = livroEndpoint.deletarLivro(cod);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(livroService, times(1)).deletar(cod);
    }

    @Test
    void testGerarRelatorio_DeveRetornarPdfQuandoSucesso() throws JRException {
        byte[] pdfContent = "PDF Content".getBytes();
        when(livroService.gerarListagemLivros()).thenReturn(pdfContent);

        ResponseEntity<byte[]> response = livroEndpoint.gerarRelatorio();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(pdfContent, response.getBody());
        assertTrue(response.getHeaders().containsKey(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("inline; filename=relatorio-listagem-livros.pdf", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        verify(livroService, times(1)).gerarListagemLivros();
    }

    @Test
    void testGerarRelatorio_DeveLancarExcecaoQuandoErroOcorrer() throws JRException {
        when(livroService.gerarListagemLivros()).thenThrow(new RuntimeException("Erro ao gerar relatório"));

        Exception exception = assertThrows(IllegalStateException.class, () -> livroEndpoint.gerarRelatorio());
        assertTrue(exception.getMessage().contains("Erro ao gerar relatório de listagem de livros"));
        verify(livroService, times(1)).gerarListagemLivros();
    }
}
