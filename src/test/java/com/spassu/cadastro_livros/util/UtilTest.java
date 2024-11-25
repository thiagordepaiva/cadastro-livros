package com.spassu.cadastro_livros.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void testFormatarValorMonetario() {
        assertEquals("R$ 1.234,56", Util.formatarValorMonetario(123456));
    }

    @Test
    void testFormatarValorMonetarioComZero() {
        assertEquals("R$ 0,00", Util.formatarValorMonetario(0));
    }

    @Test
    void testRemoverAcentos() {
        assertEquals("O Guia do Mochileiro das Galaxias", Util.removerAcentos("O Guia do Mochileiro das Galáxias"));
    }
}