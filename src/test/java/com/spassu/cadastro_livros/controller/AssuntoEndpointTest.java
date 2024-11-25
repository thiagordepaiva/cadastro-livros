package com.spassu.cadastro_livros.controller;

import com.spassu.cadastro_livros.model.Assunto;
import com.spassu.cadastro_livros.service.AssuntoService;
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

class AssuntoEndpointTest {

    @InjectMocks
    private AssuntoEndpoint assuntoEndpoint;

    @Mock
    private AssuntoService assuntoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarAssunto_DeveRetornarAssuntoCriado() {
        Assunto assunto = new Assunto(1, "Tecnologia");
        when(assuntoService.salvar(assunto)).thenReturn(assunto);

        ResponseEntity<Assunto> response = assuntoEndpoint.criarAssunto(assunto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assunto, response.getBody());
        verify(assuntoService, times(1)).salvar(assunto);
    }

    @Test
    void testListarAssuntos_DeveRetornarListaDeAssuntos() {
        List<Assunto> assuntos = Arrays.asList(
                new Assunto(1, "Tecnologia"),
                new Assunto(2, "Ciências")
        );
        when(assuntoService.listarTodos()).thenReturn(assuntos);

        ResponseEntity<List<Assunto>> response = assuntoEndpoint.listarAssuntos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assuntos, response.getBody());
        verify(assuntoService, times(1)).listarTodos();
    }

    @Test
    void testBuscarPorCod_DeveRetornarAssuntoQuandoCodExistir() {
        int cod = 1;
        Assunto assunto = new Assunto(cod, "Tecnologia");
        when(assuntoService.buscarPorCod(cod)).thenReturn(assunto);

        ResponseEntity<Assunto> response = assuntoEndpoint.buscarPorCod(cod);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(assunto, response.getBody());
        verify(assuntoService, times(1)).buscarPorCod(cod);
    }

    @Test
    void testBuscarPorCod_DeveRetornarErroQuandoCodNaoExistir() {
        int cod = 99;
        when(assuntoService.buscarPorCod(cod)).thenThrow(new RuntimeException("Assunto não encontrado"));

        try {
            assuntoEndpoint.buscarPorCod(cod);
        } catch (RuntimeException e) {
            assertEquals("Assunto não encontrado", e.getMessage());
        }

        verify(assuntoService, times(1)).buscarPorCod(cod);
    }

    @Test
    void testDeletarAssunto_DeveChamarServicoParaDeletar() {
        int cod = 1;
        doNothing().when(assuntoService).deletar(cod);

        ResponseEntity<Void> response = assuntoEndpoint.deletarAssunto(cod);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(assuntoService, times(1)).deletar(cod);
    }
}
