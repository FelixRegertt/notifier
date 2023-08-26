package com.notifier.app.persistencemodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository usuarioRepository;

    
    @Autowired
    public UserService(UserRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public void agregarUsuario(User usuario) {
        usuarioRepository.save(usuario);
    }


    public User obtenerUsuario(String id) {
        return usuarioRepository.findById(id).orElse(null);
    }


    public void actualizarUsuario(User usuario) {
        usuarioRepository.save(usuario);
    }


    public void eliminarUsuario(String id) {
        usuarioRepository.deleteById(id);
    }
}
