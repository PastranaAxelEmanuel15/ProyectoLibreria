/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author AXEL
 */
@Controller
public class ErroresControlador implements ErrorController {
    
    @RequestMapping(value="/error", method = {RequestMethod.GET, RequestMethod.POST})
    public String error(Model modelo, HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        switch (statusCode) {
            case 404:
                modelo.addAttribute("explicacion", "El recurso solicitado no existe");
                break;
            case 403:
                modelo.addAttribute("explicacion", "No tiene los permisos necesarios para acceder a este recurso");
                break;
            case 500:
                modelo.addAttribute("explicacion", "La solicitud no ha podido ser procesada por el servidor");
               break;
            case 400:
                modelo.addAttribute("explicacion", "La solicitud  contiene informacion incorrecta");
                break;
        }
        modelo.addAttribute("codigo", statusCode);
        return "error";
    }
    
}
