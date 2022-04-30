/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.servicios;

import com.guiaspring.libreriaFuncional.entidades.Autor;
import com.guiaspring.libreriaFuncional.entidades.Editorial;
import com.guiaspring.libreriaFuncional.entidades.Libro;
import com.guiaspring.libreriaFuncional.errores.ErrorServicio;
import com.guiaspring.libreriaFuncional.repositorios.AutorRepositorio;
import com.guiaspring.libreriaFuncional.repositorios.EditorialRepositorio;
import com.guiaspring.libreriaFuncional.repositorios.LibroRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author AXEL
 */
@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepo;

    @Autowired
    private AutorRepositorio repoAutor;

    @Autowired
    private EditorialRepositorio repoEditorial;

    @Autowired
    private EditorialServicio editorialServicio;

    @Autowired
    private AutorServicio autorServicio;

    public void anhadirLibro(String isbn, String titulo, Integer ejemplares, Integer anho, String nombreAutor, String nombreEditorial) throws Exception {
        
        if (libroRepo.buscarPorIsbn(isbn) != null) {
            throw new ErrorServicio("El Libro ya existe no se puede añadir");
        }

        Libro libroNuevo = new Libro();

        validar(isbn, titulo, ejemplares, anho, nombreAutor, nombreEditorial);

        libroNuevo.setIsbn(isbn);
        libroNuevo.setTitulo(titulo);
        libroNuevo.setEjemplares(ejemplares);
        libroNuevo.setEjemplareRestantes(ejemplares);
        libroNuevo.setEjemplaresPrestados(0);
        libroNuevo.setAnio(anho);
        libroNuevo.setAlta(true);

        Autor autor = repoAutor.buscarAutorPorNombre(nombreAutor);
        if (autor != null) {
            libroNuevo.setAutor(autor);
        } else {
            autor = new Autor();
            autor.setNombre(nombreAutor);
            autor.setAlta(true);
            autorServicio.anhadirAutor(autor);

            libroNuevo.setAutor(autor);
        }

        Editorial editorial = repoEditorial.buscarEditorialPorNombre(nombreEditorial);
        if (editorial != null) {
            libroNuevo.setEditorial(editorial);
        } else {
            editorial = new Editorial();
            editorial.setNombre(nombreEditorial);
            editorial.setAlta(true);
            editorialServicio.anhadirEditorial(editorial);

            libroNuevo.setEditorial(editorial);
        }

        libroRepo.save(libroNuevo);
    }

    public void modificarLibro(String id, String isbn, String titulo, Integer ejemplares, Integer anhio, String nombreAutor, String Editorial) throws ErrorServicio {
        Libro libroEditado = libroRepo.buscarPorIsbn(isbn);

        if (libroEditado != null) {
            validar(isbn, titulo, ejemplares, anhio, nombreAutor, Editorial);
            libroEditado.setTitulo(titulo);
            libroEditado.setEjemplares(ejemplares);
            libroEditado.setAnio(anhio);
            Autor autor = repoAutor.buscarAutorPorNombre(nombreAutor);
            if (autor != null) {
                libroEditado.setAutor(autor);
            } else {
                autor = new Autor();
                autor.setNombre(nombreAutor);
                autor.setAlta(true);
                autorServicio.anhadirAutor(autor);

                libroEditado.setAutor(autor);
            }

            Editorial editorial = repoEditorial.buscarEditorialPorNombre(Editorial);
            if (editorial != null) {
                libroEditado.setEditorial(editorial);
            } else {
                editorial = new Editorial();
                editorial.setNombre(Editorial);
                editorial.setAlta(true);
                editorialServicio.anhadirEditorial(editorial);

                libroEditado.setEditorial(editorial);
            }

            libroRepo.save(libroEditado);
        } else {
            throw new ErrorServicio("No se encontro El Libro");
        }

    }

    public void eliminarLibro(Libro libro) {

        libroRepo.delete(libro);
    }

    private void validar(String isbn, String titulo, Integer ejemplares, Integer anho, String nombreAutor, String nombreEditorial) throws ErrorServicio {
        if (isbn == null || isbn.isEmpty()) {
            throw new ErrorServicio("El ISBN esta Vacio");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("Debe Ingresar un Titulo");
        }
        if (ejemplares < 0) {
            throw new ErrorServicio("Debe Ingresar un numero y no debe ser menor a 0");
        }
        if (anho < 0 || anho > 2022) {
            throw new ErrorServicio("Debe Ingresar Un año valido");
        }
        if (nombreAutor == null || nombreAutor.isEmpty()) {
            throw new ErrorServicio("Debe Indicar un nombre al Autor");
        }
        if (nombreEditorial == null || nombreEditorial.isEmpty()) {
            throw new ErrorServicio("Debe Indicar la Editorial");
        }
    }

    public void deshabilitar(String isbn) throws ErrorServicio {
        Libro editLibro = libroRepo.buscarPorIsbn(isbn);
        if (editLibro != null) {
            editLibro.setAlta(false);
            libroRepo.save(editLibro);
        } else {
            throw new ErrorServicio("No se encontro el Libro");
        }

    }

    public void habilitar(String isbn) throws ErrorServicio {
        Libro editLibro = libroRepo.buscarPorIsbn(isbn);
        if (editLibro != null) {
            editLibro.setAlta(true);
            libroRepo.save(editLibro);
        } else {
            throw new ErrorServicio("No se encontro el Libro");
        }

    }
    public List<Libro> listarLibros(){
         return libroRepo.findAll();
     }
    
    public Libro buscarPorID(String id){
        return libroRepo.getById(id);
    }
}
