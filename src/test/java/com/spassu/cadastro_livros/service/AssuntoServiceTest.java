package com.spassu.cadastro_livros.service;

import com.spassu.cadastro_livros.model.Assunto;
import com.spassu.cadastro_livros.model.Livro;
import com.spassu.cadastro_livros.model.LivroAssunto;
import com.spassu.cadastro_livros.repository.AssuntoRepository;
import com.spassu.cadastro_livros.repository.LivroAssuntoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssuntoServiceTest {

    @Mock
    private AssuntoRepository assuntoRepository;

    @Mock
    private LivroAssuntoRepository livroAssuntoRepository;

    @InjectMocks
    private AssuntoService assuntoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvar_DeveSalvarAssuntoComSucesso() {
        Assunto assunto = new Assunto("Ficção Científica");

        when(assuntoRepository.existsByDescricaoIgnoreCase(anyString())).thenReturn(false);
        when(assuntoRepository.save(any(Assunto.class))).thenReturn(assunto);

        Assunto resultado = assuntoService.salvar(assunto);

        assertNotNull(resultado);
        assertEquals("Ficção Científica", resultado.getDescricao());
        verify(assuntoRepository, times(1)).save(assunto);
    }

    @Test
    void testSalvar_DeveLancarExcecaoQuandoSalvarAssuntoDuplicado() {
        Assunto assunto = new Assunto("Ficção Científica");

        when(assuntoRepository.existsByDescricaoIgnoreCase(anyString())).thenReturn(true);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> assuntoService.salvar(assunto));

        assertEquals("Já existe um assunto cadastrado com a descrição informada.", excecao.getMessage());
        verify(assuntoRepository, never()).save(any());
    }

    @Test
    void testSalvar_DeveLancarExcecaoQuandoSalvarAssuntoDuplicadoJaCadastrado() {
        Assunto assunto = new Assunto(1, "Ficção Científica");

        when(assuntoRepository.existsByDescricaoIgnoreCaseAndIdNot(anyString(), anyInt())).thenReturn(true);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> assuntoService.salvar(assunto));

        assertEquals("Já existe um assunto cadastrado com a descrição informada.", excecao.getMessage());
        verify(assuntoRepository, never()).save(any());
    }

    @Test
    void testListarTodos_DeveListarTodosOsAssuntos() {
        List<Assunto> listaAssuntos = Arrays.asList(
                new Assunto("Ficção Científica"),
                new Assunto("Fantasia")
        );

        when(assuntoRepository.findAll()).thenReturn(listaAssuntos);

        List<Assunto> resultado = assuntoService.listarTodos();

        assertEquals(2, resultado.size());
        verify(assuntoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorCod_DeveBuscarAssuntoPorCodigo() {
        Assunto assunto = new Assunto("Ficção Científica");
        assunto.setCod(1);

        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assunto));

        Assunto resultado = assuntoService.buscarPorCod(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getCod());
        assertEquals("Ficção Científica", resultado.getDescricao());
        verify(assuntoRepository, times(1)).findById(1);
    }

    @Test
    void testBuscarPorCod_DeveLancarExcecaoAoBuscarAssuntoNaoExistente() {
        when(assuntoRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> assuntoService.buscarPorCod(1));

        assertEquals("Assunto não encontrado.", excecao.getMessage());
        verify(assuntoRepository, times(1)).findById(1);
    }

    @Test
    void testDeletar_DeveExcluirAssuntoSemVinculos() {
        Assunto assunto = new Assunto("Ficção Científica");
        assunto.setCod(1);

        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assunto));
        when(livroAssuntoRepository.findByAssunto(any(Assunto.class))).thenReturn(Collections.emptyList());

        assuntoService.deletar(1);

        verify(assuntoRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeletar_DeveLancarExcecaoAoExcluirAssuntoComVinculos() {
        Assunto assunto = new Assunto("Ficção Científica");
        assunto.setCod(1);

        Livro livro1 = new Livro();
        livro1.setCod(101);

        Livro livro2 = new Livro();
        livro2.setCod(102);

        LivroAssunto livroAssunto1 = new LivroAssunto(livro1, assunto);
        LivroAssunto livroAssunto2 = new LivroAssunto(livro2, assunto);

        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assunto));
        when(livroAssuntoRepository.findByAssunto(assunto)).thenReturn(Arrays.asList(livroAssunto1, livroAssunto2));

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> assuntoService.deletar(1));

        assertTrue(excecao.getMessage().contains("Não é possível excluir o Assunto pois ele está vinculado a um ou mais livros."));
        verify(assuntoRepository, never()).deleteById(any());
    }

    @Test
    void testDeletar_DeveLancarExcecaoAoExcluirAssuntoComVinculosEm3OuMaisLivros() {
        Assunto assunto = new Assunto("Ficção Científica");
        assunto.setCod(1);

        Livro livro1 = new Livro();
        livro1.setCod(101);

        Livro livro2 = new Livro();
        livro2.setCod(102);

        Livro livro3 = new Livro();
        livro2.setCod(103);

        Livro livro4 = new Livro();
        livro2.setCod(104);

        LivroAssunto livroAssunto1 = new LivroAssunto(livro1, assunto);
        LivroAssunto livroAssunto2 = new LivroAssunto(livro2, assunto);
        LivroAssunto livroAssunto3 = new LivroAssunto(livro3, assunto);
        LivroAssunto livroAssunto4 = new LivroAssunto(livro4, assunto);

        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assunto));
        when(livroAssuntoRepository.findByAssunto(assunto)).thenReturn(Arrays.asList(
                livroAssunto1,
                livroAssunto2,
                livroAssunto3,
                livroAssunto4
        ));

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> assuntoService.deletar(1));

        assertTrue(excecao.getMessage().contains("Não é possível excluir o Assunto pois ele está vinculado a um ou mais livros. " +
                "Código(s) do(s) livro(s):"));
        verify(assuntoRepository, never()).deleteById(any());
    }
}
