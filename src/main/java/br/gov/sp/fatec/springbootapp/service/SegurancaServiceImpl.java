package br.gov.sp.fatec.springbootapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.sp.fatec.springbootapp.entity.Autorizacao;
import br.gov.sp.fatec.springbootapp.entity.Usuario;
import br.gov.sp.fatec.springbootapp.repository.AutorizacaoRepository;
import br.gov.sp.fatec.springbootapp.repository.UsuarioRepository;

@Service("SegurancaService")
public class SegurancaServiceImpl implements SegurancaService {

    @Autowired // Funciona para uma classe gerenciada pelo Spring. Então precisa da anoração
               // @Service
    private AutorizacaoRepository autorizacaoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    @Transactional // Garante que tudo que ocorre dentro da classe é uma transação. Se colocado no
                   // serviço todos os métodos terão transação
    public Usuario criarUsuario(String nome, String senha, String nomeAutorizacao) {
        Autorizacao autorizacao = autorizacaoRepo.findByNome(nomeAutorizacao);

        // Se a autorização não existir vamos crar uma
        if (autorizacao == null) {
            autorizacao = new Autorizacao();
            autorizacao.setNome(nomeAutorizacao);
            autorizacaoRepo.save(autorizacao);
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setAutorizacoes(new HashSet<Autorizacao>());
        usuario.getAutorizacoes().add(autorizacao);
        usuarioRepo.save(usuario);
        return usuario;
    }

    @Override
    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepo.findAll();
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOp = usuarioRepo.findById(id);
        if (usuarioOp.isPresent()){
            return usuarioOp.get();
        }
        throw new RuntimeException("Usuário não encontrado!");
    }

    @Override
    public Usuario buscarUsuarioPorNome(String nome) {

        Usuario usuario = usuarioRepo.findByNome(nome);
        if (usuario != null) return usuario;
        throw new RuntimeException("Usuário não encontrdo!");

    }
    
    
}
