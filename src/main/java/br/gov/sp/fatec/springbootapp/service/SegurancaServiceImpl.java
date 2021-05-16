package br.gov.sp.fatec.springbootapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.sp.fatec.springbootapp.entity.Autorizacao;
import br.gov.sp.fatec.springbootapp.entity.Usuario;
import br.gov.sp.fatec.springbootapp.exception.RegistroJaExisteException;
import br.gov.sp.fatec.springbootapp.exception.RegistroNaoEncontratoException;
import br.gov.sp.fatec.springbootapp.repository.AutorizacaoRepository;
import br.gov.sp.fatec.springbootapp.repository.UsuarioRepository;

@Service("SegurancaService")
public class SegurancaServiceImpl implements SegurancaService {

    @Autowired // Funciona para uma classe gerenciada pelo Spring. Então precisa da anoração
    private AutorizacaoRepository autorizacaoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder pwEncoder;

    @Override
    @Transactional // Garante que tudo que ocorre dentro da classe é uma transação. Se colocado no
                   // serviço todos os métodos terão transação
    public Usuario criarUsuario(String nome, String senha, String nomeAutorizacao) {
        Autorizacao autorizacao = autorizacaoRepo.findByNome(nomeAutorizacao);

        if (usuarioRepo.existsByNome(nome.toUpperCase())){
            throw new RegistroJaExisteException("Usuário já cadastrado -> nome: " + nome);            
        }

        // Se a autorização com o nome passado não existir vamos crar uma. Criado aqui porque esta tabela só tem o campo nome mesmo.
        if (autorizacao == null) {
            autorizacao = new Autorizacao();
            autorizacao.setNome(nomeAutorizacao);
            autorizacaoRepo.save(autorizacao);
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setTxtSenha(senha);
        usuario.setSenha(pwEncoder.encode(senha));
        usuario.setAutorizacoes(new HashSet<Autorizacao>());
        usuario.getAutorizacoes().add(autorizacao);
        usuarioRepo.save(usuario);
        return usuario;
    }

    @Override
    //@PreAuthorize("isAuthenticated()")
    @PreAuthorize("hasRole('admin')")
    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepo.findAll();
    }

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOp = usuarioRepo.findById(id);
        if (usuarioOp.isPresent()){
            return usuarioOp.get();
        }
        throw new RegistroNaoEncontratoException("Usuário não encontrado -> id: " + String.valueOf(id));
    }

    @Override
    public Usuario buscarUsuarioPorNome(String nome) {

        Usuario usuario = usuarioRepo.findByNome(nome.toUpperCase());
        if (usuario != null) return usuario;
        throw new RegistroNaoEncontratoException("Usuário não encontrado -> nome: " + nome);

    }

    @Override
    public Autorizacao buscarAutorizacaoPorNome(String nomeAutorizacao) {
        Autorizacao autorizacao = autorizacaoRepo.findByNome(nomeAutorizacao.toUpperCase());
        if (autorizacao != null) return autorizacao;
        throw new RegistroNaoEncontratoException("Autorizacao não encontrada -> nomeAutorizacao: " + nomeAutorizacao);
    }
    
}
