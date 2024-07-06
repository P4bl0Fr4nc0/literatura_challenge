package com.example.literatura_challenge.repository;

import com.example.literatura_challenge.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface AutorRepository extends JpaRepository<Autor, Long> {

Optional<Autor> findByNombre(String nombre);

    @Query("SELECT s FROM Autor s WHERE s.fechaNacimiento <= :anio AND s.fechaFallecimiento >= :anio")
    List<Autor> autoresVivosEnDeterminadoAnio(int anio);
}
