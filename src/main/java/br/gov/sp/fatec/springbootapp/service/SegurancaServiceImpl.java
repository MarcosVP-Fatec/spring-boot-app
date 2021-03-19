package br.gov.sp.fatec.springbootapp.service;

import java.util.HashSet;

import br.gov.sp.fatec.springbootapp.entity.Autorizacao;
import br.gov.sp.fatec.springbootapp.entity.Usuario;

public class SegurancaServiceImpl implements SegurancaService {

    @Override
    public Usuario criarUsuario(String nome, String senha, String autorizacao) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setAutorizacoes(new HashSet<Autorizacao>());
        return null;
    }
    
    
}
