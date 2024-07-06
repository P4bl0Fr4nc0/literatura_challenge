package com.example.literatura_challenge.modelos;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

@Service
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne
    private Autor autor;
    private String idioma;
    private Integer download_count;

    public Libro() {
    }

    public Libro(DatosLibro datosLibro) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.download_count = download_count;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getDownload_count() {
        return download_count;
    }

    public void setDownload_count(Integer download_count) {
        this.download_count = download_count;
    }

    @Override
    public String toString() {
        return "Libro: " +
                "titulo='" + titulo + "," +
                " autor=" + autor.getNombre() +
                ", idioma='" + idioma +
                ", cantidad de descargas=" + download_count
                ;
    }
}
