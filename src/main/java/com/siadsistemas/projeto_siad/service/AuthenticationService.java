package com.siadsistemas.projeto_siad.service;

import com.siadsistemas.projeto_siad.dto.RegisterDTO;
import com.siadsistemas.projeto_siad.model.Users;
import com.siadsistemas.projeto_siad.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UsersRepository usersRepository;

    public synchronized Users registrarUsuario(RegisterDTO dto){
        var user = usersRepository.findByLogin(dto.login());

        if(user != null){
            throw new RuntimeException("Usuário já cadastrado!");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());

        Users newUser = new Users();
        newUser.setCodigo(6);
        newUser.setLogin(dto.login());
        newUser.setPassword(encryptedPassword);
        newUser.setRole(dto.role());

        return usersRepository.save(newUser);
    }

}