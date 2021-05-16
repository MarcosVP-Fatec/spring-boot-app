package br.gov.sp.fatec.springbootapp.security;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity //Habilita segurança e configurações padrões
@EnableGlobalMethodSecurity(prePostEnabled = true) //Significa que a segurança será habilitada por anotação.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Este CSRF é para quando temos o próprio spring gerando as páginas html (back e front misturados)
        //Basicamente garante que somente as páginas rodando dentro do spring podem acessar o back-end
        //Como estamos usando o REST isto não é interessante.
        http.csrf().disable().httpBasic().and()
        // this disables session creation on Spring Secutiry
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS); //Irá usar nova requisição com credenciais.
    }
    
}
