package com.spassu.cadastro_livros;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = CadastroLivrosApplication.class)
@TestPropertySource(properties = {"spring.profiles.active=teste"})
public class CadastroLivrosApplicationTests {

    @Test
    void test_contextLoads() {
        /* Teste de contexto */
        assertTrue(true, "O contexto foi carregado corretamente.");
    }
}
