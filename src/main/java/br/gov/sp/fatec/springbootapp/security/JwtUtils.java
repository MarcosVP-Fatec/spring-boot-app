package br.gov.sp.fatec.springbootapp.security;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {

  /**
   * Chave para validar o token
   * O certo é pegar uma chave gerada e estar fora do código
   * Usar o Autowired e pegar uma variável de ambiente.
   */
  private static final String KEY = "spring.jwt.sec";

  public static String generateToken(Authentication usuario) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    Login usuarioSemSenha = new Login();
    usuarioSemSenha.setUsername(usuario.getName());

    if (!usuario.getAuthorities().isEmpty()) {
      usuarioSemSenha.setAutorizacao(usuario.getAuthorities().iterator().next().getAuthority());
    }

    String usuarioJson = mapper.writeValueAsString(usuarioSemSenha); //Gera um Json do objeto
    Date agora = new Date();
    Long hora = 1000L * 60L * 60L; // Uma hora em milissegundos
    //Criar o token
    return Jwts.builder().claim("userDetails", usuarioJson) 
        .setIssuer("br.gov.sp.fatec")                       //Quem está gerando
        .setSubject(usuario.getName())                      //Para que se destina (Para este usuário)
        .setExpiration(new Date(agora.getTime() + hora))    //Daqui até uma hora vai expirar.
        .signWith(SignatureAlgorithm.HS512, KEY)            //Assino com HS512 + a chave de criptografia local
        .compact();                                         //Compacta que gara uma string (Token)
  }

  /**
   * Método que abre um token
   */
  public static User parseToken(String token) throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    String credentialsJson = Jwts.parser()  //Já faz todas as validações
        .setSigningKey(KEY)                 //Chave de criptografia
        .parseClaimsJws(token)              //Abre o token
        .getBody()                          //Pega o corpo do token
        .get("userDetails", String.class);  //E do corpo pega o userdetails

    //Transformar de volta na classe Login        
    Login usuario = mapper.readValue(credentialsJson, Login.class);
/*    
    UserDetails userDetails = User.builder()
                    .username(usuario.getUsername())
                    .password("secret")
                    .authorities(usuario.getAutorizacao())
                    .build();
*/

    return (User) User.builder()
        .username(usuario.getUsername())
        .password("secret")
        .authorities(usuario.getAutorizacao())
        .build();

/*
    return new UsernamePasswordAuthenticationToken( usuario.getUsername()
                                                  , usuario.getPassword()
                                                  , userDetails.getAuthorities());*/
  }

}
