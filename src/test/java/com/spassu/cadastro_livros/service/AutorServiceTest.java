package com.spassu.cadastro_livros.service;

import com.spassu.cadastro_livros.model.Autor;
import com.spassu.cadastro_livros.model.Livro;
import com.spassu.cadastro_livros.model.LivroAutor;
import com.spassu.cadastro_livros.repository.AutorRepository;
import com.spassu.cadastro_livros.repository.LivroAutorRepository;
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

class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private LivroAutorRepository livroAutorRepository;

    @InjectMocks
    private AutorService autorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvar_DeveSalvarAutorComSucesso() {
        Autor autor = new Autor("George R R Martin");

        when(autorRepository.existsByNomeIgnoreCase(anyString())).thenReturn(false);
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);

        Autor resultado = autorService.salvar(autor);

        assertNotNull(resultado);
        assertEquals("George R R Martin", resultado.getNome());
        verify(autorRepository, times(1)).save(autor);
    }

    @Test
    void testSalvar_DeveLancarExcecaoQuandoSalvarAutorDuplicado() {
        Autor autor = new Autor("George R R Martin");

        when(autorRepository.existsByNomeIgnoreCase(anyString())).thenReturn(true);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> autorService.salvar(autor));

        assertEquals("Já existe um autor cadastrado com o nome informado.", excecao.getMessage());
        verify(autorRepository, never()).save(any());
    }

    @Test
    void testSalvar_DeveLancarExcecaoQuandoSalvarAutorDuplicadoJaCadastrado() {
        Autor autor = new Autor(1, "George R R Martin");

        when(autorRepository.existsByNomeIgnoreCaseAndIdNot(anyString(), anyInt())).thenReturn(true);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> autorService.salvar(autor));

        assertEquals("Já existe um autor cadastrado com o nome informado.", excecao.getMessage());
        verify(autorRepository, never()).save(any());
    }

    @Test
    void testListarTodos_DeveListarTodosOsAutors() {
        List<Autor> listaAutors = Arrays.asList(
                new Autor("George R R Martin"),
                new Autor("Fantasia")
        );

        when(autorRepository.findAll()).thenReturn(listaAutors);

        List<Autor> resultado = autorService.listarTodos();

        assertEquals(2, resultado.size());
        verify(autorRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorCod_DeveBuscarAutorPorCodigo() {
        Autor autor = new Autor("George R R Martin");
        autor.setCod(1);

        when(autorRepository.findById(1)).thenReturn(Optional.of(autor));

        Autor resultado = autorService.buscarPorCod(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getCod());
        assertEquals("George R R Martin", resultado.getNome());
        verify(autorRepository, times(1)).findById(1);
    }

    @Test
    void testBuscarPorCod_DeveLancarExcecaoAoBuscarAutorNaoExistente() {
        when(autorRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class, () -> autorService.buscarPorCod(1));

        assertEquals("Autor não encontrado.", excecao.getMessage());
        verify(autorRepository, times(1)).findById(1);
    }

    @Test
    void testDeletar_DeveExcluirAutorSemVinculos() {
        Autor autor = new Autor("George R R Martin");
        autor.setCod(1);

        when(autorRepository.findById(1)).thenReturn(Optional.of(autor));
        when(livroAutorRepository.findByAutor(any(Autor.class))).thenReturn(Collections.emptyList());

        autorService.deletar(1);

        verify(autorRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeletar_DeveLancarExcecaoAoExcluirAutorComVinculos() {
        Autor autor = new Autor("George R R Martin");
        autor.setCod(1);

        Livro livro1 = new Livro();
        livro1.setCod(101);

        Livro livro2 = new Livro();
        livro2.setCod(102);

        LivroAutor livroAutor1 = new LivroAutor(livro1, autor);
        LivroAutor livroAutor2 = new LivroAutor(livro2, autor);

        when(autorRepository.findById(1)).thenReturn(Optional.of(autor));
        when(livroAutorRepository.findByAutor(autor)).thenReturn(Arrays.asList(livroAutor1, livroAutor2));

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> autorService.deletar(1));

        assertTrue(excecao.getMessage().contains("Não é possível excluir o Autor pois ele está vinculado a um ou mais livros."));
        verify(autorRepository, never()).deleteById(any());
    }

    @Test
    void testDeletar_DeveLancarExcecaoAoExcluirAutorComVinculosEm3OuMaisLivros() {
        Autor autor = new Autor("George R R Martin");
        autor.setCod(1);

        Livro livro1 = new Livro();
        livro1.setCod(101);

        Livro livro2 = new Livro();
        livro2.setCod(102);

        Livro livro3 = new Livro();
        livro2.setCod(103);

        Livro livro4 = new Livro();
        livro2.setCod(104);

        LivroAutor livroAutor1 = new LivroAutor(livro1, autor);
        LivroAutor livroAutor2 = new LivroAutor(livro2, autor);
        LivroAutor livroAutor3 = new LivroAutor(livro3, autor);
        LivroAutor livroAutor4 = new LivroAutor(livro4, autor);

        when(autorRepository.findById(1)).thenReturn(Optional.of(autor));
        when(livroAutorRepository.findByAutor(autor)).thenReturn(Arrays.asList(
                livroAutor1,
                livroAutor2,
                livroAutor3,
                livroAutor4
        ));

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> autorService.deletar(1));

        assertTrue(excecao.getMessage().contains("Não é possível excluir o Autor pois ele está vinculado a um ou mais livros. " +
                "Código(s) do(s) livro(s):"));
        verify(autorRepository, never()).deleteById(any());
    }
}
