/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.servicios;

import com.guiaspring.libreriaFuncional.entidades.Autor;
import com.guiaspring.libreriaFuncional.errores.ErrorServicio;
import com.guiaspring.libreriaFuncional.repositorios.AutorRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author AXEL
 */
@Service
public class AutorServicio {
    
    @Autowired
    private AutorRepositorio autorRepo;
    
    public void anhadirAutor(Autor autor) throws ErrorServicio{
        
//        if(autorRepo.existsById(autor.getId())){
//           throw new ErrorServicio("El Autor ya existe no se puede añadir"); 
//        }
        
        validar(autor);
        autor.setAlta(true);
        autorRepo.save(autor);
    }
    
    public void modificarAutor(Autor autor) throws ErrorServicio{
        Autor autorEditado = autorRepo.getById(autor.getId());
        
        if(autorEditado != null){
            validar(autor);
            autorEditado.setNombre(autor.getNombre());
            autorEditado.setAlta(autor.getAlta());

            autorRepo.save(autorEditado);
        }else{
            throw new ErrorServicio("No se encontro El Autor");
        }
    }
    
    
    
    public void eliminarAutor(Autor autor){
        
        
        autorRepo.delete(autor);
    }

    private void validar(Autor autor) throws ErrorServicio {
//        if(autor.getId() == null || autor.getId().isEmpty()){
//            throw new ErrorServicio("El Id del Autor esta Vacio");
//        }
        if(autor.getNombre()== null || autor.getNombre().isEmpty()){
            throw new ErrorServicio("El nombre del Autor esta Vacio");
        }
        
    }
    
    public void deshabilitar(String id) throws ErrorServicio{
        Autor editAutor = autorRepo.getById(id);
        if(editAutor != null){
            editAutor.setAlta(false);
            autorRepo.save(editAutor);
        }else{
            throw new ErrorServicio("No se encontro el Autor");
        }
        
    }
    
     public void habilitar(String id) throws ErrorServicio{
        Autor editAutor = autorRepo.getById(id);
        if(editAutor != null){
            editAutor.setAlta(true);
            autorRepo.save(editAutor);
        }else{
            throw new ErrorServicio("No se encontro el Autor");
        }
        
    }

    public List<Autor> listarAutores() {
        return autorRepo.findAll();
    }

    public Autor buscarPorID(String id) {
        return autorRepo.getById(id);
    }
    
     
}
