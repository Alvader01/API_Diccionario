package com.github.alvader01.api_diccionario.Repositories;

import com.github.alvader01.api_diccionario.Entities.Definicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefinicionRepository extends JpaRepository<Definicion, Long> {
    // Metodo para buscar definiciones por el ID de la palabra relacionada.
    List<Definicion> findByPalabraId(Long palabraId);
}