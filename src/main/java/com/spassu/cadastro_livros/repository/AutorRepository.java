package com.spassu.cadastro_livros.repository;

import com.spassu.cadastro_livros.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Integer> {

    @Query("SELECT COUNT(a) > 0 FROM Autor a WHERE LOWER(a.nome) = LOWER(:nome)")
    boolean existsByNomeIgnoreCase(@Param("nome") String nome);

    @Query("SELECT COUNT(a) > 0 FROM Autor a WHERE LOWER(a.nome) = LOWER(:nome) AND a.cod != :cod")
    boolean existsByNomeIgnoreCaseAndIdNot(@Param("nome") String nome, @Param("cod") Integer cod);
}
