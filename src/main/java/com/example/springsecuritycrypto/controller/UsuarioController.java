package com.example.springsecuritycrypto.controller;

import com.example.springsecuritycrypto.model.Usuario;
import com.example.springsecuritycrypto.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {


    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/listarTodos")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Usuario> salvar (@RequestBody Usuario usuario){
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    @GetMapping("/validarSenha")
    public ResponseEntity<Boolean> validarSenha(@RequestParam String login, //Recebe o usuário e a senha
                                                @RequestParam String senha){

        Optional<Usuario> optUsuario = usuarioRepository.findByLogin(login); // Busca se o login existe

        if(optUsuario.isEmpty()){ // se o login for vazio..
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);// responde Unauthorized
        }
            Usuario usuario = optUsuario.get(); // crio a variável usuário recebendo  login.get que aqui chama a variavel criada para a realização da pesquisa
            boolean valid = passwordEncoder.matches(senha, usuario.getSenha()); // Cria um boleano para checar se a senha informada é a mesma da senha no banco

            HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED; //Se for valido, devolve http Ok, senão devolve Http Unauthorized

            return ResponseEntity.status(status).body(valid);
            /*Retorna o ResponseEntity com o resultado de status,
            junto de resultado da variavel criada pra checar se as senhas conferem.*/


    }
}
