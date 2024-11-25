package com.spassu.cadastro_livros.repository;

import com.spassu.cadastro_livros.model.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssuntoRepository extends JpaRepository<Assunto, Integer> {

    @Query("SELECT COUNT(a) > 0 FROM Assunto a WHERE LOWER(a.descricao) = LOWER(:descricao)")
    boolean existsByDescricaoIgnoreCase(@Param("descricao") String descricao);

    @Query("SELECT COUNT(a) > 0 FROM Assunto a WHERE LOWER(a.descricao) = LOWER(:descricao) AND a.cod != :cod")
    boolean existsByDescricaoIgnoreCaseAndIdNot(@Param("descricao") String descricao, @Param("cod") Integer cod);
}
