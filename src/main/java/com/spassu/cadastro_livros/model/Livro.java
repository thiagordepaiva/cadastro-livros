package com.spassu.cadastro_livros.model;

import com.spassu.cadastro_livros.util.Util;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cod;

    @Column(nullable = false, length = 40)
    private String titulo;

    @Column(nullable = false, length = 40)
    private String editora;

    @Column(nullable = false)
    private Integer edicao;

    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @Column(name = "ano_publicacao", nullable = false, length = 4)
    private String anoPublicacao;

    @Column(nullable = false)
    private Integer valor;

    @OneToMany(mappedBy = "livro", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LivroAssunto> livroAssuntos = new ArrayList<>();

    @OneToMany(mappedBy = "livro", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LivroAutor> livroAutores = new ArrayList<>();

    @Transient
    private String valorFormatado;

    public Livro(int cod) {
        this.cod = cod;
    }

    public String getValorFormatado() {
        return Util.formatarValorMonetario(Objects.requireNonNullElse(this.valor, 0));
    }
}
