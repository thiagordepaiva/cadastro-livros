package com.spassu.cadastro_livros.model;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class LivroTest {

    @Test
    void testGetValorFormatado() {
        Livro livro = new Livro();
        livro.setValor(123456);

        Assert.isTrue(livro.getValorFormatado().equals("R$ 1.234,56"), "Valor Formatado do livro não confere");
    }

    @Test
    void testGetValorFormatado_isValor0() {
        Livro livro = new Livro();
        livro.setValor(0);

        Assert.isTrue(livro.getValorFormatado().equals("R$ 0,00"), "Valor Formatado do livro não confere");
    }

    @Test
    void testGetValorFormatado_isValorNegativo() {
        Livro livro = new Livro();
        livro.setValor(-100);

        Assert.isTrue(livro.getValorFormatado().equals("-R$ 1,00"), "Valor Formatado do livro não confere");
    }

    @Test
    void testGetValorFormatado_isValorNull() {
        Livro livro = new Livro();

        Assert.isTrue(livro.getValorFormatado().equals("R$ 0,00"), "Valor Formatado do livro não confere");
    }
}
