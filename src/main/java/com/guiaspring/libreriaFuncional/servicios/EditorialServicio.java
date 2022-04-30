/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.servicios;

import com.guiaspring.libreriaFuncional.entidades.Editorial;
import com.guiaspring.libreriaFuncional.errores.ErrorServicio;
import com.guiaspring.libreriaFuncional.repositorios.EditorialRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author AXEL
 */
@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepo;

    public void anhadirEditorial(Editorial editorial) throws ErrorServicio {

//        if (editorialRepo.existsById(editorial.getId())) {
//            throw new ErrorServicio("Ya existe Editorial en la base de Datos");
//        }
        validar(editorial);

        editorialRepo.save(editorial);
    }

    public void modificarEditorial(Editorial editorial) throws ErrorServicio {
        Editorial editorialEditado = editorialRepo.getById(editorial.getId());

        if (editorialEditado != null) {
            validar(editorial);
            editorialEditado.setNombre(editorial.getNombre());
            editorialEditado.setAlta(editorial.getAlta());

            editorialRepo.save(editorialEditado);
        } else {
            throw new ErrorServicio("No se encontro El Editorial");
        }
    }

    public void eliminarEditorial(Editorial editorial) {

        editorialRepo.delete(editorial);
    }

    private void validar(Editorial editorial) throws ErrorServicio {
//        if (editorial.getId() == null || editorial.getId().isEmpty()) {
//            throw new ErrorServicio("El Id de la Editorial esta Vacio");
//        }
        if (editorial.getNombre() == null || editorial.getNombre().isEmpty()) {
            throw new ErrorServicio("El nombre de la Editorial esta Vacio");
        }

    }

    public void deshabilitar(String id) throws ErrorServicio {
        Editorial editEditorial = editorialRepo.getById(id);
        if (editEditorial != null) {
            editEditorial.setAlta(false);
            editorialRepo.save(editEditorial);
        } else {
            throw new ErrorServicio("No se encontro el Editorial");
        }

    }

    public void habilitar(String id) throws ErrorServicio {
        Editorial editEditorial = editorialRepo.getById(id);
        if (editEditorial != null) {
            editEditorial.setAlta(true);
            editorialRepo.save(editEditorial);
        } else {
            throw new ErrorServicio("No se encontro el Editorial");
        }

    }

    public List<Editorial> listarEditoriales() {
        return editorialRepo.findAll();
    }

    public Editorial buscarPorID(String id) {
        return editorialRepo.getById(id);
    }
    
    
}
