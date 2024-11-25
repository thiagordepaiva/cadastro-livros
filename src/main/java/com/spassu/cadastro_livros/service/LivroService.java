package com.spassu.cadastro_livros.service;

import com.spassu.cadastro_livros.model.*;
import com.spassu.cadastro_livros.repository.LivroRepository;
import com.spassu.cadastro_livros.util.Util;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro salvar(Livro livro) {
        handleAutoresELivros(livro);
        validarDuplicidadeIsbn(livro);

        return livroRepository.save(livro);
    }

    private static void handleAutoresELivros(Livro livro) {
        livro.getLivroAutores().forEach(livroAutor -> livroAutor.setLivro(livro));
        livro.getLivroAssuntos().forEach(livroAssunto -> livroAssunto.setLivro(livro));
    }

    private void validarDuplicidadeIsbn(Livro livro) {
        boolean cadastroDuplicado;

        if (livro.getCod() == null) {
            cadastroDuplicado = livroRepository.existsByIsbnIgnoreCase(
                    Util.removerAcentos(livro.getIsbn()));
        } else {
            cadastroDuplicado = livroRepository.existsByIsbnIgnoreCaseAndIdNot(
                    Util.removerAcentos(livro.getIsbn()), livro.getCod());
        }

        if (cadastroDuplicado) {
            throw new IllegalArgumentException("Já existe um livro cadastrado com o isbn informado.");
        }
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Livro buscarPorCod(Integer cod) {
        return livroRepository.findById(cod).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }

    @Transactional
    public void deletar(Integer cod) {
        livroRepository.deleteById(cod);
    }

    public byte[] gerarListagemLivros() throws JRException {
        List<Livro> livros = this.listarTodos();

        if (livros == null || livros.isEmpty()) {
            throw new IllegalStateException("A lista de livros está vazia!");
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(livros);

        JasperReport jasperReport = JasperCompileManager
                .compileReport("src/main/resources/reports/relatorio-listagem-livros.jrxml");

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("valorTotalFormatado", getValorTotalFormatado(livros));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private String getValorTotalFormatado(List<Livro> livros) {
        int valorTotal = livros.stream()
                .mapToInt(Livro::getValor)
                .sum();

        return Util.formatarValorMonetario(valorTotal);
    }
}
