package com.spassu.cadastro_livros.repository;

import com.spassu.cadastro_livros.model.Autor;
import com.spassu.cadastro_livros.model.LivroAutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroAutorRepository extends JpaRepository<LivroAutor, Integer> {

    List<LivroAutor> findByAutor(Autor autor);
}
