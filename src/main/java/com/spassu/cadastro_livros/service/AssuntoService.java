package com.spassu.cadastro_livros.service;

import com.spassu.cadastro_livros.model.Assunto;
import com.spassu.cadastro_livros.model.LivroAssunto;
import com.spassu.cadastro_livros.repository.AssuntoRepository;
import com.spassu.cadastro_livros.repository.LivroAssuntoRepository;
import com.spassu.cadastro_livros.util.Util;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssuntoService {

    private final AssuntoRepository assuntoRepository;
    private final LivroAssuntoRepository livroAssuntoRepository;

    public AssuntoService(AssuntoRepository assuntoRepository,
                          LivroAssuntoRepository livroAssuntoRepository) {
        this.assuntoRepository = assuntoRepository;
        this.livroAssuntoRepository = livroAssuntoRepository;
    }

    public Assunto salvar(Assunto assunto) {
        validarDuplicidadeDescricao(assunto);

        return assuntoRepository.save(assunto);
    }

    private void validarDuplicidadeDescricao(Assunto assunto) {
        boolean cadastroDuplicado;

        if (assunto.getCod() == null) {
            cadastroDuplicado = assuntoRepository.existsByDescricaoIgnoreCase
                    (Util.removerAcentos(assunto.getDescricao()));
        } else {
            cadastroDuplicado = assuntoRepository.existsByDescricaoIgnoreCaseAndIdNot(
                    Util.removerAcentos(assunto.getDescricao()), assunto.getCod());
        }

        if (cadastroDuplicado) {
            throw new IllegalArgumentException("Já existe um assunto cadastrado com a descrição informada.");
        }
    }

    public List<Assunto> listarTodos() {
        return assuntoRepository.findAll();
    }

    public Assunto buscarPorCod(Integer cod) {
        return assuntoRepository.findById(cod).orElseThrow(() -> new RuntimeException("Assunto não encontrado."));
    }

    public void deletar(Integer cod) {
        List<LivroAssunto> vinculos = livroAssuntoRepository.findByAssunto(buscarPorCod(cod));

        if (!vinculos.isEmpty()) {
            List<Integer> codsLivros = vinculos.stream()
                    .map(la -> la.getLivro().getCod())
                    .limit(3)
                    .toList();

            String codsFormatados = codsLivros.toString();
            if (vinculos.size() > 3) {
                codsFormatados = codsFormatados.replace("]", ",...)");
            }

            throw new IllegalArgumentException("Não é possível excluir o Assunto pois ele está vinculado a um ou mais livros. " +
                    "Código(s) do(s) livro(s): " + codsFormatados);
        }

        assuntoRepository.deleteById(cod);
    }
}
