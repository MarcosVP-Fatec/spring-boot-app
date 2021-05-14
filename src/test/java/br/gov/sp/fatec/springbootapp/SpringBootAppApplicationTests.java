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

    /**
     * Função que cria um usuário para evitar reescrita em todas as classes de teste
     */
    private Usuario criaUsuarioTestolino(){
        Usuario usuario = new Usuario();
        usuario.setNome("Testolino");
        usuario.setSenha("senha123");
        return usuarioRepo.save(usuario);
    }

    /**
     * Função que cria um usuário para evitar reescrita em todas as classes de teste
     */
    private Usuario criaUsuarioTestolinoComAutorizacao( Autorizacao autorizacao ){
        Usuario usuario = new Usuario();
        usuario.setNome("Testolino");
        usuario.setSenha("senha123");
        usuario.setAutorizacoes(new HashSet<Autorizacao>());
        usuario.getAutorizacoes().add(autorizacao);
        return usuarioRepo.save(usuario);
    }

    /**
     * Função que cria uma regra de autorização para evitar reescrita em todas as classes de teste
     */
    private Autorizacao criaAutorizacaoTeste(){
        Autorizacao autorizacao = new Autorizacao();
        autorizacao.setNome("#REGRA_TESTE");
        return autorizacaoRepo.save(autorizacao);
    }

    private void testeQuebra(String titulo){
        System.out.println("######################################################################################");
        System.out.println("##############################    " + titulo);
        System.out.println("######################################################################################");
        System.out.println("##################################################################################");
        System.out.println("##############################################################################");
        System.out.println("##########################################################################");
        System.out.println("######################################################################");
        System.out.println("##################################################################");
        System.out.println("##############################################################");
        System.out.println("##########################################################");
        System.out.println("######################################################");
        System.out.println("##################################################");
        System.out.println("##############################################");
        System.out.println("##########################################");
    }

    @Test
	void contextLoads() {
    }
    
    @Test
    void testeTblUsuarioInsere(){
        testeQuebra("USUARIO - Insere");
        Usuario usuario = new Usuario();
        usuario.setNome("Teste X");
        usuario.setSenha("$enhaF0rte");
        usuarioRepo.save(usuario);
        assertNotNull(usuario.getId());
    }

    @Test
    void testeTblUsuarioExclui(){
        testeQuebra("USUARIO - Exclui");
        Long local_id;
        Usuario usuario = new Usuario();
        usuario.setNome("Teste X");
        usuario.setSenha("$enhaF0rte");
        usuarioRepo.save(usuario);
        local_id = usuario.getId();
        usuarioRepo.delete(usuario);
        assertFalse(usuarioRepo.existsById(local_id));
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
    void testePesqUsuarioNomeContains(){
        testeQuebra("PESQUISA USUARIO - Contém Nome");
        this.criaUsuarioTestolino();
        List<Usuario> usuarios = usuarioRepo.findByNomeContainsIgnoreCase("ino");
        assertFalse(usuarios.isEmpty());
    }

    @Test
    void testePesqUsuarioPorNomeExato(){
        testeQuebra("PESQUISA USUARIO - Nome exato");
        this.criaUsuarioTestolino();
        assertNotNull(usuarioRepo.findByNome("Testolino"));
    }

    void testePesqUsuarioPorNomeExatoQuery(){
        testeQuebra("PESQUISA USUARIO - Nome exato Query");
        this.criaUsuarioTestolino();
        assertNotNull(usuarioRepo.buscarUsuarioPorNome("Testolino"));
    }
/*
    @Test
    void testBuscarUsuarioPorNome(){
        testeQuebra("BUSCAR USUARIO - Por Nome");
        this.criaUsuarioTestolino();
        Usuario usuario = usuarioRepo.buscarUsuarioPorNome("Testolino");
        assertEquals(usuario.getSenha(), "senha123");
    }
*/
    @Test
    void testePesqUsuarioNomeAutorizacao(){
        testeQuebra("PESQUISA USUARIO - Nome da Autorização");
        this.criaUsuarioTestolinoComAutorizacao(this.criaAutorizacaoTeste());
        List<Usuario> usuarios = usuarioRepo.findByAutorizacoesNome("#REGRA_TESTE");
        assertEquals(1, usuarios.size());
    }

    @Test
    void testePesqUsuarioNomeSenha(){
        testeQuebra("PESQUISA USUARIO - Nome e Senha");
        this.criaUsuarioTestolino();
        assertTrue(usuarioRepo.findByNomeAndSenha("Testolino", "senha123") != null);
    }

    @Test
    void testePesqUsuarioNomeSenhaQuery(){
        testeQuebra("PESQUISA USUARIO - Nome e Senha - Query");
        this.criaUsuarioTestolino();
        assertTrue(usuarioRepo.buscarUsuarioPorNomeESenha("Testolino", "senha123") != null);
    }

    @Test
    void testBuscarUsuariosPorNomeDeAutorizacao(){
        testeQuebra("BUSCAR USUARIO - Por Nome de Autorização");
        this.criaUsuarioTestolinoComAutorizacao(this.criaAutorizacaoTeste());
        List<Usuario> usuarios = usuarioRepo.buscarUsuariosPorNomeDeAutorizacao("#REGRA_TESTE");
        assertTrue(usuarios.size() == 1);
    }

    @Test
    void testarServicoCriaUsuario(){
        Usuario usuario = segurancaService.criarUsuario("Pessoa#normal", "senha123", "#REGRA_NORMAL");
        assertNotNull(usuario);
    }

    @BeforeAll
    static void init(@Autowired JdbcTemplate jdbcTemplate){
        
        jdbcTemplate.update(
            "insert into usr_usuario (usr_nome, usr_senha) values(?,?)", "Mineda", "SenhaF0rte");

        jdbcTemplate.update(
            "insert into aut_autorizacao (aut_nome) values(?)", "ROLE_ADMIN");

        jdbcTemplate.update(
            "insert into uau_usuario_autorizacao (usr_id, aut_id) values(?,?)", 1L, 1L);

    }

}
