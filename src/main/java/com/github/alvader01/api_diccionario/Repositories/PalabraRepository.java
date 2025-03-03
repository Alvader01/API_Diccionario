package com.github.alvader01.api_diccionario.Repositories;

import com.github.alvader01.api_diccionario.Entities.Palabra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PalabraRepository extends JpaRepository<Palabra, Long> {
    // Consulta personalizada para obtener todas las palabras que pertenecen a una categoría gramatical específica.
    @Query("SELECT p FROM Palabra p WHERE p.categoriaGramatical = :categoria")
    List<Palabra> findByCategoriaGramatical(String categoria);

    // Consulta personalizada para obtener todas las palabras que comienzan con una letra específica (inicial).
    @Query("SELECT p FROM Palabra p WHERE p.termino LIKE :letra%")
    List<Palabra> findByInicial(String letra);

    // Metodo para encontrar una palabra por su término.
    // Este metodo devuelve un `Optional<Palabra>` porque no está garantizado que se encuentre una palabra con el término proporcionado.
    Optional<Palabra> findByTermino(String termino);

    // Metodo para verificar si ya existe una palabra con el término especificado.
    // Retorna un valor booleano que indica si el término ya está registrado en la base de datos.
    boolean existsByTermino(String termino);
}