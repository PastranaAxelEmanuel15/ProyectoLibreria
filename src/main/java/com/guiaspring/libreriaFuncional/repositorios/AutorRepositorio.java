/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.repositorios;

import com.guiaspring.libreriaFuncional.entidades.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor,String>{
    
    @Query("select c from Autor c where c.nombre = :nombre")
    public Autor buscarAutorPorNombre(@Param("nombre") String nombre);
}
