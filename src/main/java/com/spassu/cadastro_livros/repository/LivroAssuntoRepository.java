package com.spassu.cadastro_livros.repository;

import com.spassu.cadastro_livros.model.Assunto;
import com.spassu.cadastro_livros.model.LivroAssunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroAssuntoRepository extends JpaRepository<LivroAssunto, Integer> {

    List<LivroAssunto> findByAssunto(Assunto assunto);
}
