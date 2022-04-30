/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.servicios;

import com.guiaspring.libreriaFuncional.entidades.Foto;
import com.guiaspring.libreriaFuncional.repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author AXEL
 */
@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotoRepositorio;

    public Foto guardar(MultipartFile archivo) throws Exception {
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setName(archivo.getName());
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (IOException ex) {
                throw new Exception("no se pudo guardar");
            }
        } else {
            return null;
        }

    }
}
