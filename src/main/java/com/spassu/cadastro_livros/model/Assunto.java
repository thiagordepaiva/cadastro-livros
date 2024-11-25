package com.spassu.cadastro_livros.model;

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
@Table(name = "assunto")
public class Assunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cod;

    @Column(nullable = false, length = 40, unique = true)
    private String descricao;

    public Assunto(String descricao) {
        this.descricao = descricao;
    }
}

