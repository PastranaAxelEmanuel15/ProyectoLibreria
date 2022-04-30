/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.repositorios;

import com.guiaspring.libreriaFuncional.entidades.Libro;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AXEL
 */
@Repository
public interface LibroRepositorio extends JpaRepository<Libro,String> {
    
    @Query("select c from Libro c where c.isbn = :isbn")
    public Libro buscarPorIsbn(@Param("isbn") String isbn);
    
    @Query("select c from Libro c where c.titulo = :titulo")
    public Libro buscarLibroPorTitulo(@Param("titulo") String titulo);
    
    @Query("select c from Libro c where c.titulo = :titulo")
    public List<Libro> buscarLibrosPorTitulo(@Param("titulo") String titulo);
    
    @Query("select c from Libro c where c.autor.nombre = :nombre")
    public List<Libro> buscarLibrosPorAutor(@Param("nombre") String nombre);
    
    @Query("select c from Libro c where c.editorial.nombre = :nombre")
    public List<Libro> buscarLibrosPorEditorial(@Param("nombre") String nombre);

    
    
    
}

