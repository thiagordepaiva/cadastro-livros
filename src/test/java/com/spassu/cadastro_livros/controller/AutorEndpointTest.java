package com.spassu.cadastro_livros.controller;

import com.spassu.cadastro_livros.model.Autor;
import com.spassu.cadastro_livros.service.AutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AutorEndpointTest {

    @InjectMocks
    private AutorEndpoint autorEndpoint;

    @Mock
    private AutorService autorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarAutor_DeveRetornarAutorCriado() {
        Autor autor = new Autor(1, "João Silva");
        when(autorService.salvar(autor)).thenReturn(autor);

        ResponseEntity<Autor> response = autorEndpoint.criarAutor(autor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(autor, response.getBody());
        verify(autorService, times(1)).salvar(autor);
    }

    @Test
    void testListarAutors_DeveRetornarListaDeAutors() {
        List<Autor> autores = Arrays.asList(
                new Autor(1, "João Silva"),
                new Autor(2, "Maria Oliveira")
        );
        when(autorService.listarTodos()).thenReturn(autores);

        ResponseEntity<List<Autor>> response = autorEndpoint.listarAutors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(autores, response.getBody());
        verify(autorService, times(1)).listarTodos();
    }

    @Test
    void testBuscarPorCod_DeveRetornarAutorQuandoCodExistir() {
        int cod = 1;
        Autor autor = new Autor(cod, "João Silva");
        when(autorService.buscarPorCod(cod)).thenReturn(autor);

        ResponseEntity<Autor> response = autorEndpoint.buscarPorCod(cod);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(autor, response.getBody());
        verify(autorService, times(1)).buscarPorCod(cod);
    }

    @Test
    void testBuscarPorCod_DeveRetornarErroQuandoCodNaoExistir() {
        int cod = 99;
        when(autorService.buscarPorCod(cod)).thenThrow(new RuntimeException("Autor não encontrado"));

        try {
            autorEndpoint.buscarPorCod(cod);
        } catch (RuntimeException e) {
            assertEquals("Autor não encontrado", e.getMessage());
        }

        verify(autorService, times(1)).buscarPorCod(cod);
    }

    @Test
    void testDeletarAutor_DeveChamarServicoParaDeletar() {
        int cod = 1;
        doNothing().when(autorService).deletar(cod);

        ResponseEntity<Void> response = autorEndpoint.deletarAutor(cod);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(autorService, times(1)).deletar(cod);
    }
}
