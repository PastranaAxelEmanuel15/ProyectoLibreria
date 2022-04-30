/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.controladores;

import com.guiaspring.libreriaFuncional.servicios.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class portalControlador {
    
    @Autowired
    private MailService mailService;
    
    @GetMapping("")
    public String index(ModelMap modelMap, 
            @RequestParam(name = "nombre", required=false ) String nombre){
        modelMap.addAttribute("nombre",nombre);
        return "index";
    }
    
    @GetMapping("/enviarmail")
    public String enviarMail(Model modelo) {
        mailService.enviarEmailSimple("pastranaaxele@gmail.com", "Saludos", "Hola soy un mail enviado desde un proyecto con spring boot");
        modelo.addAttribute("mail", "se esta procesando el envio de mail");
        return "index";
    }
    
}
