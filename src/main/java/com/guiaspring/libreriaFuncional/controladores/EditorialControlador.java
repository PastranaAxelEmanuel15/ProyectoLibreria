/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.controladores;

import com.guiaspring.libreriaFuncional.entidades.Editorial;
import com.guiaspring.libreriaFuncional.errores.ErrorServicio;
import com.guiaspring.libreriaFuncional.servicios.EditorialServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {

    @Autowired
    EditorialServicio editorialServicio = new EditorialServicio();

    @GetMapping("")
    public String listaEditoriales(Model modelo) {
        List<Editorial> listaEditoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("listaEditoriales", listaEditoriales);

        return "editoriales";
    }

    @GetMapping("/formulario")
    public String editoriales(Model modelo,
            @RequestParam(name = "idEditorial", required = false) String id,
            @RequestParam(name = "nombreEditorial", required = false) String nombreEditorial
    ){
        Editorial editorial;
        if (id == null) {
            editorial = new Editorial();
        } else {
            editorial = editorialServicio.buscarPorID(id);
        }
        modelo.addAttribute("idEditorial", id);
        modelo.addAttribute("editorial", editorial);
        return "editorial-formulario";
    }

    @PostMapping("/formulario/save")
    public String guardarEditorial(ModelMap modelo,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "idEditorial", required = false) String id,
            @RequestParam("nombreEditorial") String nombreEditorial
    ) {
        Editorial editorial = null;

        try {
            String success;
            if (id == null || id.isEmpty()) {
                editorial = new Editorial();
                editorial.setNombre(nombreEditorial);
                editorial.setAlta(true);

                editorialServicio.anhadirEditorial(editorial);
                success = "Libro creado con exito";

            } else {
                editorial = editorialServicio.buscarPorID(id);
                editorial.setNombre(nombreEditorial);
                editorialServicio.modificarEditorial(editorial);
                success = "Libro modificado con exito";

            }
            modelo.addAttribute("success", success);
            modelo.addAttribute("editorial", editorial);

        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
            editorial = new Editorial();
            modelo.addAttribute("nombreEditorial", nombreEditorial);
            modelo.addAttribute("editorial", editorial);
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "editorial-formulario";
        }
        //modelo.addAttribute("libro", libro);

        return "redirect:/editorial/formulario";
    }

    @GetMapping("/darBaja")
    public String daBaja(Model modelo,
            @RequestParam(name = "idEditorial", required = true) String id
    ) {

        try {
            editorialServicio.deshabilitar(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "editoriales";
        }
        return "redirect:/editorial";
    }

    @GetMapping("/darAlta")
    public String daAlta(Model modelo,
            @RequestParam(name = "idEditorial", required = true) String id
    ) {
        try {
            editorialServicio.habilitar(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "editoriales";
        }
        return "redirect:/editorial";
    }

}
