package com.example.literatura_challenge.repository;

import com.example.literatura_challenge.modelos.Libro;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface LibrosRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Libro> findByIdioma(String idioma);

}
