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
@Table(name = "livro_assunto", uniqueConstraints = @UniqueConstraint(columnNames = {"livro_cod", "assunto_cod"}))
public class LivroAssunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "livro_cod", nullable = false)
    private Livro livro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assunto_cod", nullable = false)
    private Assunto assunto;

    public LivroAssunto(Livro livro, Assunto assunto) {
        this.livro = livro;
        this.assunto = assunto;
    }
}
