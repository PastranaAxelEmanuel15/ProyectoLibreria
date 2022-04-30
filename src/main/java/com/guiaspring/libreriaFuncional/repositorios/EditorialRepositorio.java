/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.repositorios;

import com.guiaspring.libreriaFuncional.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author AXEL
 */
@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String>{
    @Query("select c from Editorial c where c.nombre = :nombre")
    public Editorial buscarEditorialPorNombre(@Param("nombre") String nombre);
    
    
}
