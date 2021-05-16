package br.gov.sp.fatec.springbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sp.fatec.springbootapp.security.Login;

@RestController
@RequestMapping(value = "/login")
@CrossOrigin
public class LoginController {

    @Autowired
    private AuthenticationManager authManager; //Quem faz a autenticação no spring
    
    /**
     * O objeto aut contém apenas usuário e senha e, depois de autenticar recebe mais dados
     */
    @PostMapping()
    public Login autenticar(@RequestBody Login login){
        Authentication auth = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
        auth = authManager.authenticate(auth); //Se não passar por este método vai sair com uma exception.
        return login;
    }

}
