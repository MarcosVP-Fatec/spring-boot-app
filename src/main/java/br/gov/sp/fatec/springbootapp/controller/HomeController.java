package br.gov.sp.fatec.springbootapp.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin        //Posso colocar aqui quais sites podem acessar.
public class HomeController {

    /**
     * Significa que esta rota vai responder a uma requisição do tipo GET
     * Vai retornar um texto "Hello World!"
     * @return String 
     */
    @GetMapping 
    public String wellcome(){
        return "Hello World!!!";
    }    
}
