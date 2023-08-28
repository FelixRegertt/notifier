package com.notifier.app.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository usuarioRepository;

    
    @Autowired
    public UserService(UserRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public void save(User usuario) {
        usuarioRepository.save(usuario);
    }


    public User get(String id) {
        return usuarioRepository.findById(id).orElse(null);
    }


    public void update(User usuario) {
        usuarioRepository.save(usuario);
    }

}
