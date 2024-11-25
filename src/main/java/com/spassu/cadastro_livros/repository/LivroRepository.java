package com.spassu.cadastro_livros.repository;

import com.spassu.cadastro_livros.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivroRepository extends JpaRepository<Livro, Integer> {

    @Query("SELECT COUNT(l) > 0 FROM Livro l WHERE LOWER(l.isbn) = LOWER(:isbn)")
    boolean existsByIsbnIgnoreCase(@Param("isbn") String isbn);

    @Query("SELECT COUNT(l) > 0 FROM Livro l WHERE LOWER(l.isbn) = LOWER(:isbn) AND l.cod != :cod")
    boolean existsByIsbnIgnoreCaseAndIdNot(@Param("isbn") String isbn, @Param("cod") Integer cod);
}
