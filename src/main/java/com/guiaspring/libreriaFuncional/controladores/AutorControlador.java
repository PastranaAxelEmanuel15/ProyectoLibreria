/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.controladores;

import com.guiaspring.libreriaFuncional.entidades.Autor;
import com.guiaspring.libreriaFuncional.errores.ErrorServicio;
import com.guiaspring.libreriaFuncional.servicios.AutorServicio;
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

/**
 *
 * @author AXEL
 */
@Controller
@RequestMapping("/autor")
public class AutorControlador {
    
    @Autowired
    AutorServicio autorServicio = new AutorServicio();
    
    @GetMapping("")
    public String listaAutores(Model modelo){
        List<Autor> listaAutores = autorServicio.listarAutores();
        modelo.addAttribute("listaAutores", listaAutores);
        return "autores";
    }
    
    @GetMapping("/formulario")
    public String formulario(Model modelo,
            @RequestParam(name = "idAutor", required = false) String id,
            @RequestParam(name = "nombreAutor", required = false) String nombreAutor
    ){
        Autor autor;
        if (id == null) {
            autor = new Autor();
        } else {
            autor = autorServicio.buscarPorID(id);
        }
        modelo.addAttribute("idAutor", id);
        modelo.addAttribute("autor", autor);
        return "autor-formulario";
    }
    
    @PostMapping("/formulario/save")
    public String guardarAutor(ModelMap modelo,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "idAutor", required = false) String id,
            @RequestParam("nombreAutor") String nombreAutor
    ) {
        Autor autor = null;
        
        try {
            String success;
            if (id == null || id.isEmpty()) {
                autor = new Autor();
                autor.setNombre(nombreAutor);
                
                autorServicio.anhadirAutor(autor);
                success = "Libro creado con exito";          

            } else {
                autor = autorServicio.buscarPorID(id);
                autor.setNombre(nombreAutor);
                autorServicio.modificarAutor(autor);
                success = "Libro modificado con exito";
                
            }
            modelo.addAttribute("success", success);
            modelo.addAttribute("autor", autor);

        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
            autor = new Autor();
            modelo.addAttribute("nombreAutor", nombreAutor);
            modelo.addAttribute("autor", autor);
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "autor-formulario";
        }
        //modelo.addAttribute("libro", libro);
       
        return "redirect:/autor/formulario";
    }
    
    @GetMapping("/darBaja")
    public String daBaja(Model modelo,
            @RequestParam(name = "idAutor", required = true) String id
    ){
        
        try {
            autorServicio.deshabilitar(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "autores";
        }
        return "redirect:/autor";
    }
    
    @GetMapping("/darAlta")
    public String daAlta(Model modelo,
            @RequestParam(name = "idAutor", required = true) String id
    ){
        try {
            autorServicio.habilitar(id);
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "autores";
        }
        return "redirect:/autor";
    }
    
}
