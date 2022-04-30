/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.controladores;

import com.guiaspring.libreriaFuncional.entidades.Autor;
import com.guiaspring.libreriaFuncional.entidades.Editorial;
import com.guiaspring.libreriaFuncional.entidades.Libro;
import com.guiaspring.libreriaFuncional.errores.ErrorServicio;
import com.guiaspring.libreriaFuncional.servicios.LibroServicio;
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
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    LibroServicio libroService = new LibroServicio();

    @GetMapping("")
    public String libros(Model modelo) {
        List<Libro> libros = libroService.listarLibros();
        modelo.addAttribute("listaLibros", libros);
        //
        return "libros";
    }

    @GetMapping("/formulario")
    public String formulario(Model modelo,
            @RequestParam(name = "idLibro", required = false) String id //modificar o guardar
    ) {
        Libro libro;
        if (id == null) {
            libro = new Libro();

            libro.setAutor(new Autor());
            libro.setEditorial(new Editorial());
        } else {
            libro = libroService.buscarPorID(id);
        }
        modelo.addAttribute("idLibro", id);
        modelo.addAttribute("libro", libro);
        return "libro-formulario";
    }

    @PostMapping("/formulario/save")
    public String guardarLibro(ModelMap modelo,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "idLibro", required = false) String id,
            @RequestParam("isbn") String isbn,
            @RequestParam("titulo") String titulo,
            @RequestParam("anio") Integer numero,
            @RequestParam("ejemplares") Integer ejemplares,
            @RequestParam("autor") String nombreAutor,
            @RequestParam("editorial") String nombreEditorial
    ) {
        Libro libro = null;
        
        try {
            String success;
            libro = libroService.buscarPorID(id);
            
            if (id == null || id.isEmpty()) {
                libro = new Libro();
                libro.setAutor(new Autor());
                libro.setEditorial(new Editorial());
                libroService.anhadirLibro(isbn, titulo, ejemplares, numero, nombreAutor, nombreEditorial);
                success = "Libro creado con exito";          

            } else {
                libro = libroService.buscarPorID(id);
                
                libroService.modificarLibro(id, isbn, titulo, ejemplares, numero, nombreAutor, nombreEditorial);
                success = "Libro modificado con exito";
                
            }
            modelo.addAttribute("success", success);
            modelo.addAttribute("libro", libro);

        } catch (Exception ex) {
            modelo.addAttribute("error", ex.getMessage());
            libro = new Libro();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(numero);
            libro.setEjemplares(ejemplares);

            modelo.addAttribute("nombreAutor", nombreAutor);
            modelo.addAttribute("nombreEditorial", nombreEditorial);
            modelo.addAttribute("libro", libro);
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "libro-formulario";
        }
        //modelo.addAttribute("libro", libro);
       
        return "redirect:/libro/formulario";
    }

    @PostMapping("/modificar")
    public String modificarLibro() {
        //
        return "libros";
    }
    
    @GetMapping("/darBaja")
    public String daBaja(ModelMap modelo,
            @RequestParam(name = "idLibro", required = true) String id
    ){
        
        try {
            Libro libro = libroService.buscarPorID(id);
            libroService.deshabilitar(libro.getIsbn());
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "libros";
        }
        return "redirect:/libro";
    }
    
    @GetMapping("/darAlta")
    public String daAlta(ModelMap modelo,
            @RequestParam(name = "idLibro", required = true) String id
    ){
        try {
            Libro libro = libroService.buscarPorID(id);
            libroService.habilitar(libro.getIsbn());
        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "libros";
        }
        return "redirect:/libro";
    }
}
