package br.gov.sp.fatec.springbootapp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import br.gov.sp.fatec.springbootapp.entity.Autorizacao;
import br.gov.sp.fatec.springbootapp.entity.Usuario;
import br.gov.sp.fatec.springbootapp.repository.AutorizacaoRepository;
import br.gov.sp.fatec.springbootapp.repository.UsuarioRepository;
import br.gov.sp.fatec.springbootapp.service.SegurancaService;

@SpringBootTest
@Transactional
class SpringBootAppApplicationTests {

    @Autowired 
    private UsuarioRepository usuarioRepo;
    @Autowired 
    private AutorizacaoRepository autorizacaoRepo;
    @Autowired
    private SegurancaService segurancaService;

    @BeforeAll
    static void init(@Autowired JdbcTemplate jdbcTemplate){
        
        jdbcTemplate.update(
            "insert into usr_usuario (usr_nome, usr_senha) values(?,?)", "Mineda", "SenhaF0rte");

        jdbcTemplate.update(
            "insert into aut_autorizacao (aut_nome) values(?)", "ROLE_ADMIN");

        jdbcTemplate.update(
            "insert into uau_usuario_autorizacao (usr_id, aut_id) values(?,?)", 1L, 1L);

        jdbcTemplate.update(
            "insert into usr_usuario (usr_nome, usr_senha) values(?,?)", "UsuEXCLUIR", "Senha123");

        jdbcTemplate.update(
            "insert into aut_autorizacao (aut_nome) values(?)", "ROLE_NAO_EXCLUIR");

        jdbcTemplate.update(
            "insert into uau_usuario_autorizacao (usr_id, aut_id) values(?,?)", 2L, 2L);

    }

    private static void testeQuebra(String titulo){
        System.out.println("######################################################################################");
        System.out.println("######################################################################################");
        System.out.println("######################################################################################");
        System.out.println("##############################    " + titulo);
        System.out.println("######################################################################################");
        System.out.println("##################################################################################");
        System.out.println("##############################################################################");
        System.out.println("##########################################################################");
    }

    @Test
    void existeUsuario(){
        testeQuebra("EXISTE USUÁRIO");
        assertNotNull(usuarioRepo.buscarUsuarioPorNome("Mineda"));
    }
    @Test
    void naoExisteUsuario(){
        testeQuebra("NÃO EXISTE USUÁRIO");
        assertNull(usuarioRepo.buscarUsuarioPorNome("Moneda"));
    }

    @Test
    void testeTblUsuarioInsere(){
        testeQuebra("USUARIO - Insere Repository");
        Usuario usuario = new Usuario();
        usuario.setNome("UsuarioInsere");
        usuario.setSenha("$enhaF0rte");
        usuarioRepo.saveAndFlush(usuario);
        assertNotNull(usuario.getId());
    }

    @Test
    void testarServicoCriaUsuario(){
        testeQuebra("USUARIO - Insere Service");
        Usuario usuario = segurancaService.criarUsuario("UsuInsereService", "senha123", "#REGRA_NORMAL");
        assertNotNull(usuario);
    }

    @Test
    void testBuscarUsuariosPorNomeDeAutorizacao(){
        testeQuebra("BUSCAR USUARIO - Por Nome de Autorização");
        List<Usuario> usuarios = usuarioRepo.buscarUsuariosPorNomeDeAutorizacao("ROLE_ADMIN");
        assertTrue(usuarios.size() != 0);
    }

    @Test
    void testeTblUsuarioExclui(){
        testeQuebra("USUARIO - Exclui");
        Long local_id;
        Usuario usuario = usuarioRepo.findById(2L).get();
        local_id = usuario.getId();
        usuarioRepo.delete(usuario);
        assertFalse(usuarioRepo.existsById(local_id));
    }

    @Test
    void testePesqUsuarioNomeSenhaQuery(){
        testeQuebra("PESQUISA USUARIO - Nome e Senha - Query");
        assertTrue(usuarioRepo.buscarUsuarioPorNomeESenha("Mineda", "SenhaF0rte").getId() != null);
    }

    @Test
    void testeTblUsuarioComAutorizacaoInsere(){
        testeQuebra("USUARIO com AUTORIZACAO - Insere");
        Autorizacao autorizacao = new Autorizacao();
        autorizacao.setNome("REGRA_TESTE_1");
        autorizacaoRepo.save(autorizacao);

        Usuario usuario = new Usuario();
        usuario.setNome("#TESTE_USR");
        usuario.setSenha("1dois3quatro");
        
        usuario.setAutorizacoes(new HashSet<Autorizacao>());
        usuario.getAutorizacoes().add(autorizacao);
        usuarioRepo.save(usuario);

        assertEquals("#TESTE_USR", usuarioRepo.getOne(usuario.getId()).getNome() );
    }

    @Test
    void testeTblAutorizacaoInsere(){
        testeQuebra("AUTORIZACAO - Insere");
        Autorizacao autorizacao = new Autorizacao();
        autorizacao.setNome("REGRA_DE__TESTE");
        autorizacaoRepo.save(autorizacao);
        assertNotNull(autorizacao.getNome());
        assertNotNull(autorizacao.getId());
    }

    @Test
    void testePesqUsuarioNomeContains(){
        testeQuebra("PESQUISA USUARIO - Contém Nome");
        List<Usuario> usuarios = usuarioRepo.findByNomeContainsIgnoreCase("ined");
        assertFalse(usuarios.isEmpty());
    }

    @Test
    void testePesqUsuarioPorNomeExato(){
        testeQuebra("PESQUISA USUARIO - Nome exato");
        assertNotNull(usuarioRepo.findByNome("Mineda").getId());
    }

    void testePesqUsuarioPorNomeExatoQuery(){
        testeQuebra("PESQUISA USUARIO - Nome exato Query");
        assertNotNull(usuarioRepo.buscarUsuarioPorNome("Mineda"));
    }

    @Test
    void testBuscarUsuarioPorNome(){
        testeQuebra("BUSCAR USUARIO - Por Nome");
        Usuario usuario = usuarioRepo.buscarUsuarioPorNome("Mineda");
        assertEquals(usuario.getSenha(), "SenhaF0rte");
    }

    @Test
    void testePesqUsuarioNomeAutorizacao(){
        testeQuebra("PESQUISA USUARIO - Nome da Autorização");
        List<Usuario> usuarios = usuarioRepo.findByAutorizacoesNome("ROLE_ADMIN");
        assertEquals(1, usuarios.size());
    }

    /*
    @Test
	void contextLoads() {
    }

    // Função que cria um usuário para evitar reescrita em todas as classes de teste
    private Usuario criaUsuarioTestolino(){
        Usuario usuario = new Usuario();
        usuario.setNome("Testolino");
        usuario.setSenha("senha123");
        return usuarioRepo.save(usuario);
    }

    // Função que cria um usuário para evitar reescrita em todas as classes de teste
    private Usuario criaUsuarioTestolinoComAutorizacao( Autorizacao autorizacao ){
        Usuario usuario = new Usuario();
        usuario.setNome("Testolino");
        usuario.setSenha("senha123");
        usuario.setAutorizacoes(new HashSet<Autorizacao>());
        usuario.getAutorizacoes().add(autorizacao);
        return usuarioRepo.save(usuario);
    }

    //Função que cria uma regra de autorização para evitar reescrita em todas as classes de teste
    private Autorizacao criaAutorizacaoTeste(){
        Autorizacao autorizacao = new Autorizacao();
        autorizacao.setNome("#REGRA_TESTE");
        return autorizacaoRepo.save(autorizacao);
    }
*/
}
