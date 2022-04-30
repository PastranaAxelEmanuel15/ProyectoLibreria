/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.controladores;

import com.guiaspring.libreriaFuncional.entidades.Usuario;
import com.guiaspring.libreriaFuncional.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author AXEL
 */
@Controller
@RequestMapping("/foto")
public class FotoControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/usuario")
    public ResponseEntity<byte[]> devolverFotoDePerfil(
            @RequestParam("idUsuario") String id
    ) {
        try {
            Usuario usuario = usuarioServicio.buscarUsuarioPorID(id);
            byte[] foto = usuario.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
