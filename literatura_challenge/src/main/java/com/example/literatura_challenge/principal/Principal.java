package com.example.literatura_challenge.principal;

import com.example.literatura_challenge.modelos.Autor;
import com.example.literatura_challenge.modelos.Datos;
import com.example.literatura_challenge.modelos.DatosLibro;
import com.example.literatura_challenge.modelos.Libro;
import com.example.literatura_challenge.repository.AutorRepository;
import com.example.literatura_challenge.repository.LibrosRepository;
import com.example.literatura_challenge.servicios.ConsumoAPI;
import com.example.literatura_challenge.servicios.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

public class Principal {

Scanner teclado = new Scanner(System.in);
private ConsumoAPI consumoAPI = new ConsumoAPI();
private ConvierteDatos conversor = new ConvierteDatos();
private LibrosRepository repositorio ;
private AutorRepository  autorRepositorio;
private List<Libro> libros;

private final String URL_GUTENDEX = "https://gutendex.com/books/";

    public Principal(LibrosRepository repository, AutorRepository autorRepository) {
        this.repositorio = repository;
        this.autorRepositorio = autorRepository;
    }

    public void menu(){
        var opcion = 0;
        while (opcion != 6){
            var menu = """
                    
                    Elije la opcion deseada:
                    
                    1.-Buscar libro por titulo.
                    2.-Listar libros registrados.
                    3.-Listar autores registrados.
                    4.-Listar autores vivos en un determinado año.
                    5.-Listar libros por idioma
                    6.- Salir
                    """;
            System.out.println(menu);
            try {
                String input = teclado.nextLine();
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e){
                System.out.println("Solo se aceptan opciones numericas de la lista.  \n");
            }
            switch (opcion){

                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listaAutoresRegistrados();
                    break;
                case 4:
                    autoresVivosEnUnAnio();
                    break;
                case 5:
                    busquedaPorIdioma();
                    break;
                case 6:
                    System.out.println("Gracias por acceder al sistema de busqueda de libros...");
                    break;
                default:
                    System.out.println("Opcion invalida, por favor elija una opcion de la lista");
            }

        }
    }

    //Comienzan metodos del menu

    //Busqueda de libros
    private Libro buscarLibro() {
        System.out.println("Cual libro deseas buscar");
        String nombreLibro = teclado.nextLine();

        var json = consumoAPI.obtenerDatos(URL_GUTENDEX+"?search="+nombreLibro.replace(" ", "+"));
        var resultadoBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibro> libroBuscado = resultadoBusqueda.resultado().stream()
                .filter(l -> l.titulo()
                        .toUpperCase()
                        .contains(nombreLibro.toUpperCase()))
                        .findFirst();

        //verifica que el libro se haya encontrado en la API
        if (libroBuscado.isPresent()) {
            System.out.println("Libro localizado...");
            var libroDato = libroBuscado.get();
            Libro libro = new Libro(libroDato);

            //Verifica si autor se encuentra en la base de datos
            Autor autor = new Autor(libroDato.autor().get(0));
            Optional<Autor> autorExistente = autorRepositorio.findByNombre(autor.getNombre());
            if (autorExistente.isPresent()) {
                libro.setAutor(autorExistente.get());
            } else {
                //Guardar el autor en base de datos
                autorRepositorio.save(autor);
                libro.setAutor(autor);
            }
            // Verificar si el libro ya existe en la base de datos
            Optional<Libro> libroExistente = repositorio.findByTituloContainsIgnoreCase(libro.getTitulo());
            if (libroExistente.isPresent()) {
                System.out.println(libroExistente.get());
            } else {
                //Guardare libro en base de datos
                repositorio.save(libro);
                //Mostrar el libro en pantalla
                System.out.println(libro);
                return libro;

            }
        } else {
            System.out.println("No se encontro el libro");       }

        return null;
    }

    //listar los libros

    private void listarLibros() {
        libros = repositorio.findAll();

        libros.stream()
                .forEach(System.out::println);
    }

    //Listar los autores registrados en base de datos
    private void  listaAutoresRegistrados(){
        var autores = autorRepositorio.findAll();
        System.out.println("Autores en base de datos:");
        autores.stream().forEach(System.out::println);
    }

    //Autores vivos en un determinado año

    public void autoresVivosEnUnAnio(){
        System.out.println("Especifique el año: ");
        String input = teclado.nextLine();
        var anio = Integer.parseInt(input);
        var resultado = autorRepositorio.autoresVivosEnDeterminadoAnio(anio);
        if(resultado.isEmpty()) {
            System.out.println("No se encontraron autores en el año ingresado");
        } else {
            System.out.println("Autores vivos en el año: "+anio+":");
            List<Autor> autoresDelAnio = resultado;
            autoresDelAnio.forEach(System.out::println);
        }
    }

    //Busqueda por idioma


    private void busquedaPorIdioma(){
        String idiomausuario = null;
        System.out.println("Libros en que idioma deseas ver: ");
        var sub_menu = """
                    1.- Español
                    2.- Ingles
                    """;
        System.out.println(sub_menu);
        String input = teclado.nextLine();
        var idioma = Integer.parseInt(input);
        switch (idioma){
            case 1:
                idiomausuario = "es";
                break;
            case 2:
                idiomausuario = "en";
                break;
            default:
                System.out.println("Opcion invalida");
        }
        List<Libro> librosPorIdioma = repositorio.findByIdioma(idiomausuario);
        System.out.println("Libros encontrados en el idioma seleccionado:");
        librosPorIdioma.forEach(s -> System.out.println("Titulo: "+s.getTitulo()+", Autor: "+s.getAutor().getNombre()+" Descargas: "+s.getDownload_count()));
    }


}

