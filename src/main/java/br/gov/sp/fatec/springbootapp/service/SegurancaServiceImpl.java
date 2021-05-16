package br.gov.sp.fatec.springbootapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.sp.fatec.springbootapp.entity.Autorizacao;
import br.gov.sp.fatec.springbootapp.entity.Usuario;
import br.gov.sp.fatec.springbootapp.exception.RegistroJaExisteException;
import br.gov.sp.fatec.springbootapp.exception.RegistroNaoEncontradoException;
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

        if (usuarioRepo.existsByNome(nome.toUpperCase())) {
            throw new RegistroJaExisteException("Usuário já cadastrado -> nome: " + nome);
        }

        // Se a autorização com o nome passado não existir vamos crar uma. Criado aqui
        // porque esta tabela só tem o campo nome mesmo.
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> buscarTodosUsuarios() {
        return usuarioRepo.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USUARIO')")
    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOp = usuarioRepo.findById(id);
        if (usuarioOp.isPresent()) {
            return usuarioOp.get();
        }
        throw new RegistroNaoEncontradoException("Usuário não encontrado -> id: " + String.valueOf(id));
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public Usuario buscarUsuarioPorNome(String nome) {

        Usuario usuario = usuarioRepo.findByNome(nome.toUpperCase());
        if (usuario != null)
            return usuario;
        throw new RegistroNaoEncontradoException("Usuário não encontrado -> nome: " + nome);

    }

    @PreAuthorize("isAuthenticated()")
    @Override
    public Autorizacao buscarAutorizacaoPorNome(String nomeAutorizacao) {
        Autorizacao autorizacao = autorizacaoRepo.findByNome(nomeAutorizacao.toUpperCase());
        if (autorizacao != null)
            return autorizacao;
        throw new RegistroNaoEncontradoException("Autorizacao não encontrada: " + nomeAutorizacao);
    }

    /**
     * Este método mostra para o spring onde estão os detalhes (regras) que deverão ser usadas como em
     * hasRole('ROLE_ADMIN'), por exemplo
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByNome(username);
        if(usuario==null){
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        return User.builder().username(username)
            .password(usuario.getSenha())
            .authorities(usuario.getAutorizacoes().stream() //authorities espere um vetor de string. 
                                                            //Pega as autorizações e transforma em uma lista
                .map(Autorizacao::getNome).collect(Collectors.toList()) //Retira só o nome cria uma lista 
                .toArray(new String[usuario.getAutorizacoes().size()])) //Passa para um array de strings
            .build();
    }

}
