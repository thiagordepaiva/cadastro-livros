package com.spassu.cadastro_livros.repository;

import com.spassu.cadastro_livros.CadastroLivrosApplicationTests;
import com.spassu.cadastro_livros.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("teste")
class RepositoryTest extends CadastroLivrosApplicationTests {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroAutorRepository livroAutorRepository;

    @Autowired
    private LivroAssuntoRepository livroAssuntoRepository;

    @BeforeEach
    void setUp() {
        livroAutorRepository.deleteAll();
        livroAssuntoRepository.deleteAll();
        livroRepository.deleteAll();
        autorRepository.deleteAll();
        assuntoRepository.deleteAll();
    }

    @Test
    void testSave_SalvarLivro() {
        Livro livro = criarLivro("O Guia do Mochileiro das Galáxias", "1234567890123", "Editora XYZ", 1, "1980", 123456);
        Livro livroSalvo = livroRepository.save(livro);

        assertNotNull(livroSalvo.getCod(), "Livro não foi salvo");
        assertEquals("O Guia do Mochileiro das Galáxias", livroSalvo.getTitulo());
        assertEquals("Editora XYZ", livroSalvo.getEditora());
        assertEquals(123456, livroSalvo.getValor());
        assertEquals("R$ 1.234,56", livroSalvo.getValorFormatado());
    }

    @Test
    void testSave_VincularLivroEAssunto() {
        Livro livro = livroRepository.save(criarLivro("O Guia do Mochileiro das Galáxias", "0123456789123", "Editora XYZ", 1, "1980", 123456));
        Assunto assunto = assuntoRepository.save(new Assunto("Ficção Científica"));

        LivroAssunto livroAssunto = new LivroAssunto(livro, assunto);
        LivroAssunto livroAssuntoSalvo = livroAssuntoRepository.save(livroAssunto);

        assertNotNull(livroAssuntoSalvo.getCod());
        assertEquals("Ficção Científica", livroAssuntoSalvo.getAssunto().getDescricao());

        LivroAssunto livroAssuntoBuscado = livroAssuntoRepository.findById(livroAssuntoSalvo.getCod()).orElse(null);
        assertNotNull(livroAssuntoBuscado, "Vínculo entre livro e assunto não foi encontrado");
        assertEquals("Ficção Científica", livroAssuntoBuscado.getAssunto().getDescricao());
    }

    @Test
    void testSave_VincularLivroEAutor() {
        Livro livro = livroRepository.save(criarLivro("O Guia do Mochileiro das Galáxias", "0123456789123", "Editora XYZ", 1, "1980", 123456));
        Autor autor = autorRepository.save(new Autor("Douglas Adams"));

        LivroAutor livroAutor = new LivroAutor(livro, autor);
        LivroAutor livroAutorSalvo = livroAutorRepository.save(livroAutor);

        assertNotNull(livroAutorSalvo.getCod());
        assertEquals("Douglas Adams", livroAutorSalvo.getAutor().getNome());

        LivroAutor livroAutorBuscado = livroAutorRepository.findById(livroAutorSalvo.getCod()).orElse(null);
        assertNotNull(livroAutorBuscado, "Vínculo entre livro e autor não foi encontrado");
        assertEquals("Douglas Adams", livroAutorBuscado.getAutor().getNome());
    }

    @Test
    void testFindById_BuscarLivroComVinculos() {
        Livro livro = livroRepository.save(criarLivro("O Guia do Mochileiro das Galáxias", "1593576542580", "Editora XYZ", 1, "1980", 123456));
        Assunto assunto = assuntoRepository.save(new Assunto("Ficção Científica"));
        Autor autor = autorRepository.save(new Autor("Douglas Adams"));

        livroAssuntoRepository.save(new LivroAssunto(livro, assunto));
        livroAutorRepository.save(new LivroAutor(livro, autor));

        Livro livroBuscado = livroRepository.findById(livro.getCod()).orElse(null);
        assertNotNull(livroBuscado, "Livro não foi encontrado");
        assertEquals(1, livroBuscado.getLivroAssuntos().size(), "Vínculo entre livro e assunto não foi encontrado");
        assertEquals(1, livroBuscado.getLivroAutores().size(), "Vínculo entre livro e autor não foi encontrado");
    }

    @Test
    void testSave_AlterarLivro() {
        Livro livro = criarLivro("Livro Original", "0123456789123", "Editora ABC", 2, "2020", 10000);
        Livro livroSalvo = livroRepository.save(livro);

        livroSalvo.setTitulo("Livro Alterado");
        livroSalvo.setEditora("Editora Alterada");
        livroSalvo.setValor(20000);
        Livro livroAtualizado = livroRepository.save(livroSalvo);

        assertEquals("Livro Alterado", livroAtualizado.getTitulo());
        assertEquals("Editora Alterada", livroAtualizado.getEditora());
        assertEquals(20000, livroAtualizado.getValor());
        assertEquals("R$ 200,00", livroAtualizado.getValorFormatado());
    }

    @Test
    void testDeleteById_ExcluirLivro() {
        Livro livro = livroRepository.save(criarLivro("Livro Para Deletar", "9876543210321", "Editora ABC", 1, "2020", 10000));
        livroRepository.deleteById(livro.getCod());

        Optional<Livro> livroExcluido = livroRepository.findById(livro.getCod());
        assertTrue(livroExcluido.isEmpty(), "Livro não foi excluído");
    }

    private Livro criarLivro(String titulo,
                             String isbn,
                             String editora,
                             int edicao,
                             String anoPublicacao,
                             int valor) {
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setEditora(editora);
        livro.setIsbn(isbn);
        livro.setEdicao(edicao);
        livro.setAnoPublicacao(anoPublicacao);
        livro.setValor(valor);

        return livro;
    }
}