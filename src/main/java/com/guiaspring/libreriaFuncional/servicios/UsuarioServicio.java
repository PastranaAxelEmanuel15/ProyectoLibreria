/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guiaspring.libreriaFuncional.servicios;

import com.guiaspring.libreriaFuncional.Enum.Rol;
import com.guiaspring.libreriaFuncional.entidades.Foto;
import com.guiaspring.libreriaFuncional.entidades.Usuario;
import com.guiaspring.libreriaFuncional.errores.ErrorServicio;
import com.guiaspring.libreriaFuncional.repositorios.UsuarioRepositorio;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author AXEL
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private FotoServicio fotoServicio;

    public Usuario registrarUsuario(String id, String username, String nombre, String apellido, String password_1, String password_2, Date FechaDeNacimiento, Integer edad, MultipartFile file) throws ErrorServicio, Exception {

        if (username == null || username.isEmpty()) {
            throw new ErrorServicio("El Username no puede estar vacio");
        }

        Usuario usuarioNuevo = usuarioRepositorio.buscarUsuarioPorUsername(username);

        if (id != null && !id.isEmpty()) {

            usuarioNuevo = buscarUsuarioPorID(id);
            //usuarioNuevo.setUsername(username);
            if (usuarioNuevo == null) {
                throw new Exception("No existe el usuario porque no existe en la base de datos");
            }

        } else {

            if (usuarioNuevo != null) {
                throw new ErrorServicio("Ya existe usuario con ese Username, pruebe con otro Username");
            }

            if (password_1 == null || password_1.isEmpty()) {
                throw new ErrorServicio("La contraseña no puede estar vacio");
            }

            if (password_2 == null || password_2.isEmpty()) {
                throw new ErrorServicio("Debe Repetir la contraseña");
            }
            usuarioNuevo = new Usuario();
//            if (file != null) {
//                Foto foto = fotoServicio.guardar(file);
//                usuarioNuevo.setFoto(foto);
//            }

            if (!password_1.equals(password_2)) {
                throw new ErrorServicio("Las contraseñas deben ser iguales");
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            usuarioNuevo.setContrasenha(encoder.encode(password_1));

            usuarioNuevo.setRolUser(Rol.USUARIO);

        }
        if (edad == null || edad < 1) {
            throw new ErrorServicio("Deber ingresar una edad valida");
        }
        usuarioNuevo.setUsername(username);
        usuarioNuevo.setNombre(nombre);
        usuarioNuevo.setApellido(apellido);

        if (file != null) {
            Foto foto = fotoServicio.guardar(file);
            usuarioNuevo.setFoto(foto);
        }

        usuarioNuevo.setFechaDeNacimiento(FechaDeNacimiento);
        usuarioNuevo.setEdad(edad);

        return usuarioRepositorio.save(usuarioNuevo);
    }

    public Usuario modificarUsuario(Usuario usuario) {
        Usuario usuarioEditado = usuarioRepositorio.getById(usuario.getId());

        return usuarioRepositorio.save(usuarioEditado);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    public Usuario buscarUsuarioPorID(String id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    public void agregarUsuarioALaSesion(Usuario usuario) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        session.setAttribute("usuario", usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try { // esta parte configura los permisos de los usuarios
            Usuario usuario = usuarioRepositorio.buscarUsuarioPorUsername(username);
            List<GrantedAuthority> autorities = new ArrayList<>();

            agregarUsuarioALaSesion(usuario);
            autorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRolUser()));

            return new User(username, usuario.getContrasenha(), autorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("El usuario no existe");
        }
    }

    public void deshabilitar(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
