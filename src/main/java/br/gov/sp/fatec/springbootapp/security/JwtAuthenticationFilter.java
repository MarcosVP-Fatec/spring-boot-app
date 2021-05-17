package br.gov.sp.fatec.springbootapp.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter( ServletRequest  request
                        , ServletResponse response
                        , FilterChain     chain) throws IOException, ServletException {

        try {
            //Faz um cast do request
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            //Com o tipo HttpServletRequest conseguimos pegar os seus dados
            String authorization = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization != null) {
                //No lucar de Basic xxx o JWT traz Bearer, então tiramos isso da autorização antes de usar 
                //(Fica só o token). O parse valida o token
                Authentication credentials = JwtUtils.parseToken(authorization.replaceAll("Bearer ", ""));
                //Singleton que não pode ser instanciado novamente.
                //Isso pega a instancia local que o sistema está usando.
                //Passa o objeto do tipo Authentication que tem o usuário e suas autorizações
                SecurityContextHolder.getContext().setAuthentication(credentials);
            }
            chain.doFilter(request, response);
        } catch (Throwable t) {
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, t.getMessage());
        }
    }

}
