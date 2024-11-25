package com.spassu.cadastro_livros.service;

import com.spassu.cadastro_livros.model.Livro;
import com.spassu.cadastro_livros.repository.LivroRepository;
import net.sf.jasperreports.engine.JRException;
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

class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private LivroService livroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvar_DeveSalvarLivroComSucesso() {
        Livro livro = new Livro();
        livro.setIsbn("123-456-789");
        livro.setValor(100);

        when(livroRepository.existsByIsbnIgnoreCase(anyString())).thenReturn(false);
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        Livro resultado = livroService.salvar(livro);

        assertNotNull(resultado);
        assertEquals("123-456-789", resultado.getIsbn());
        verify(livroRepository, times(1)).save(livro);
    }

    @Test
    void testSalvar_DeveLancarExcecaoAoSalvarLivroComIsbnDuplicado() {
        Livro livro = new Livro();
        livro.setIsbn("123-456-789");

        when(livroRepository.existsByIsbnIgnoreCase(anyString())).thenReturn(true);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> livroService.salvar(livro));

        assertEquals("Já existe um livro cadastrado com o isbn informado.", excecao.getMessage());
        verify(livroRepository, never()).save(any());
    }

    @Test
    void testSalvar_DeveLancarExcecaoAoSalvarLivroComIsbnDuplicadoJaCadastrado() {
        Livro livro = new Livro();
        livro.setCod(1);
        livro.setIsbn("123-456-789");

        when(livroRepository.existsByIsbnIgnoreCaseAndIdNot(anyString(), anyInt())).thenReturn(true);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> livroService.salvar(livro));

        assertEquals("Já existe um livro cadastrado com o isbn informado.", excecao.getMessage());
        verify(livroRepository, never()).save(any());
    }

    @Test
    void testListarTodos_DeveListarTodosOsLivros() {
        List<Livro> livros = Arrays.asList(new Livro(), new Livro());

        when(livroRepository.findAll()).thenReturn(livros);

        List<Livro> resultado = livroService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(livroRepository, times(1)).findAll();
    }

    @Test
    void testDeletar_DeveBuscarLivroPorCodigo() {
        Livro livro = new Livro();
        livro.setCod(1);

        when(livroRepository.findById(1)).thenReturn(Optional.of(livro));

        Livro resultado = livroService.buscarPorCod(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getCod());
        verify(livroRepository, times(1)).findById(1);
    }

    @Test
    void testDeletar_DeveLancarExcecaoAoBuscarLivroInexistente() {
        when(livroRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> livroService.buscarPorCod(1));

        assertEquals("Livro não encontrado", excecao.getMessage());
        verify(livroRepository, times(1)).findById(1);
    }

    @Test
    void testDeletar_DeveExcluirLivroComSucesso() {
        doNothing().when(livroRepository).deleteById(1);

        livroService.deletar(1);

        verify(livroRepository, times(1)).deleteById(1);
    }

    @Test
    void testGerarListagemLivros_DeveGerarListagemDeLivros() throws JRException {
        Livro livro1 = new Livro();
        livro1.setValor(100);
        Livro livro2 = new Livro();
        livro2.setValor(200);

        List<Livro> livros = Arrays.asList(livro1, livro2);

        when(livroRepository.findAll()).thenReturn(livros);

        byte[] resultado = livroService.gerarListagemLivros();

        assertNotNull(resultado);
        verify(livroRepository, times(1)).findAll();
    }

    @Test
    void testGerarListagemLivros_DeveLancarExcecaoAoGerarListagemSemLivros() {
        when(livroRepository.findAll()).thenReturn(Collections.emptyList());

        IllegalStateException excecao = assertThrows(IllegalStateException.class, () -> livroService.gerarListagemLivros());

        assertEquals("A lista de livros está vazia!", excecao.getMessage());
        verify(livroRepository, times(1)).findAll();
    }

    @Test
    void testGerarListagemLivros_DeveLancarExcecaoAoGerarListagemSemLivrosReturnNull() {
        when(livroRepository.findAll()).thenReturn(null);

        IllegalStateException excecao = assertThrows(IllegalStateException.class, () -> livroService.gerarListagemLivros());

        assertEquals("A lista de livros está vazia!", excecao.getMessage());
        verify(livroRepository, times(1)).findAll();
    }
}
