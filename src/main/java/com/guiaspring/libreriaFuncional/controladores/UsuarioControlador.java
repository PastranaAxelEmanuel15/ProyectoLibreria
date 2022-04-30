/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.controladores;

import com.guiaspring.libreriaFuncional.entidades.Usuario;
import com.guiaspring.libreriaFuncional.errores.ErrorServicio;
import com.guiaspring.libreriaFuncional.servicios.UsuarioServicio;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author AXEL
 */
@Controller()
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR')")
    @GetMapping("/lista")
    public String listaUsuarios(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();

        modelo.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    @GetMapping("/formulario")
    public String registro(ModelMap modelo,
            @RequestParam(name = "idUsuario", required = false) String id
            , RedirectAttributes redirectAttributes
    ) {
        try {
            if (id != null) {
                Usuario usuario = usuarioServicio.buscarUsuarioPorID(id);
                modelo.addAttribute("id", usuario.getId());
                modelo.addAttribute("username", usuario.getUsername());
                modelo.addAttribute("nombre", usuario.getNombre());
                modelo.addAttribute("apellido", usuario.getApellido());
                modelo.addAttribute("fechaDeNacimiento", usuario.getFechaDeNacimiento());
                modelo.addAttribute("edad", usuario.getEdad());
                modelo.addAttribute("modificar", "Modificar");
            } else {
                modelo.addAttribute("username", "");
                modelo.addAttribute("nombre", "");
                modelo.addAttribute("apellido", "");
                modelo.addAttribute("password1", "");
                modelo.addAttribute("password2", "");
                modelo.addAttribute("fechaDeNacimiento", new Date());
                modelo.addAttribute("edad", 0);
                modelo.addAttribute("registrar", "Registrarme");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No Se pudo Encontrar EL USUARIO");
            return "redirect:/";
        }

        return "usuario-formulario";
    }

    @PostMapping("/registro")
    public String registrarUsuario(ModelMap modelo,
            @RequestParam("id") String id,
            @RequestParam("username") String username,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam(name="password1", required = false ) String password,
            @RequestParam(name="password2", required = false ) String password2,
            @RequestParam("edad") Integer edad,
            @RequestParam(name="file", required = false ) MultipartFile file,
             @RequestParam("fechaDeNacimiento")
            @DateTimeFormat(pattern = "yyyy-mm-dd") Date FechaDeNacimiento
    ) throws Exception {
        try {
//            Date FechaDeNacimiento = new Date();
            Usuario usuario = usuarioServicio.registrarUsuario(id, username, nombre, apellido, password, password2, FechaDeNacimiento, edad, file);
            modelo.addAttribute("success", "Usuario " + id!= null ? "editado": "registrado"+ "con exito");
            
            return "usuario-formulario";
        } catch (Exception ex) {
            ex.printStackTrace();
            
                modelo.addAttribute("id", id);
                modelo.addAttribute("username", username);
                modelo.addAttribute("nombre", nombre);
                modelo.addAttribute("apellido", apellido);
                modelo.addAttribute("fechaDeNacimiento", FechaDeNacimiento);
                modelo.addAttribute("edad", edad);
            
            
            modelo.addAttribute("error", ex.getMessage());
            return "usuario-formulario";
        }

    }
    
    
//    @PostMapping("/modificar")
//    public String modificar() {
//        return "redirect:/usuario/lista";
//    }

}
