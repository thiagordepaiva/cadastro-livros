package com.spassu.cadastro_livros.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "livro_autor", uniqueConstraints = @UniqueConstraint(columnNames = {"livro_cod", "autor_cod"}))
public class LivroAutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "livro_cod", nullable = false)
    private Livro livro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_cod", nullable = false)
    private Autor autor;

    public LivroAutor(Livro livro, Autor autor) {
        this.livro = livro;
        this.autor = autor;
    }
}
