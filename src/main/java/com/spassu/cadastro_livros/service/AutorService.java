package com.spassu.cadastro_livros.service;

import com.spassu.cadastro_livros.model.Autor;
import com.spassu.cadastro_livros.model.LivroAutor;
import com.spassu.cadastro_livros.repository.AutorRepository;
import com.spassu.cadastro_livros.repository.LivroAutorRepository;
import com.spassu.cadastro_livros.util.Util;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroAutorRepository livroAutorRepository;

    public AutorService(AutorRepository autorRepository,
                        LivroAutorRepository livroAutorRepository) {
        this.autorRepository = autorRepository;
        this.livroAutorRepository = livroAutorRepository;
    }

    public Autor salvar(Autor autor) {
        validarDuplicidadeNome(autor);

        return autorRepository.save(autor);
    }

    private void validarDuplicidadeNome(Autor autor) {
        boolean cadastroDuplicado;

        if (autor.getCod() == null) {
            cadastroDuplicado = autorRepository.existsByNomeIgnoreCase(
                    Util.removerAcentos(autor.getNome()));
        } else {
            cadastroDuplicado = autorRepository.existsByNomeIgnoreCaseAndIdNot(
                    Util.removerAcentos(autor.getNome()), autor.getCod());
        }

        if (cadastroDuplicado) {
            throw new IllegalArgumentException("Já existe um autor cadastrado com o nome informado.");
        }
    }

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public Autor buscarPorCod(Integer cod) {
        return autorRepository.findById(cod).orElseThrow(() -> new RuntimeException("Autor não encontrado."));
    }

    public void deletar(Integer cod) {
        List<LivroAutor> vinculos = livroAutorRepository.findByAutor(buscarPorCod(cod));

        if (!vinculos.isEmpty()) {
            List<Integer> codsLivros = vinculos.stream()
                    .map(la -> la.getLivro().getCod())
                    .limit(3)
                    .toList();

            String codsFormatados = codsLivros.toString();
            if (vinculos.size() > 3) {
                codsFormatados = codsFormatados.replace("]", ",...)");
            }

            throw new IllegalArgumentException("Não é possível excluir o Autor pois ele está vinculado a um ou mais livros. " +
                    "Código(s) do(s) livro(s): " + codsFormatados);
        }

        autorRepository.deleteById(cod);
    }
}

