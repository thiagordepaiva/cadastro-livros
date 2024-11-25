package com.spassu.cadastro_livros.util;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class Util {

    public static String removerAcentos(String texto) {
        String nfd = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        return pattern.matcher(nfd).replaceAll("");
    }

    public static String formatarValorMonetario(int valorTotal) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valorTotal / 100.0);
    }
}
