package br.gov.sp.fatec.springbootapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import br.gov.sp.fatec.springbootapp.entity.Autorizacao;
import br.gov.sp.fatec.springbootapp.entity.Usuario;
import br.gov.sp.fatec.springbootapp.repository.AutorizacaoRepository;
import br.gov.sp.fatec.springbootapp.repository.UsuarioRepository;

@SpringBootTest
@Transactional
@Rollback
class SpringBootAppApplicationTests {

    @Autowired 
    private UsuarioRepository usuarioRepo;
    @Autowired 
    private AutorizacaoRepository autorizacaoRepo;

	@Test
	void contextLoads() {
    }
    
    @Test
    void testeTblUsuarioInsere(){

        Usuario usuario = new Usuario();
        usuario.setNome("Teste X");
        usuario.setSenha("$enhaF0rte");
        usuarioRepo.save(usuario);
        assertNotNull(usuario.getId());
        
    }

    @Test
    void testeTblUsuarioExclui(){

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

        Autorizacao autorizacao = new Autorizacao();
        autorizacao.setNome("REGRA_DE__TESTE");
        autorizacaoRepo.save(autorizacao);
        assertNotNull(autorizacao.getNome());
    }

    @Test
    void testeTblUsuarioComAutorizacaoInsere(){

        Autorizacao autorizacao = new Autorizacao();
        autorizacao.setNome("REGRA_TESTE_2");
        autorizacaoRepo.save(autorizacao);

        Usuario usuario = new Usuario();
        usuario.setNome("MItces");
        usuario.setSenha("$enhaF0rte");
        
        usuario.setAutorizacoes(new HashSet<Autorizacao>());
        usuario.getAutorizacoes().add(autorizacao);
        usuarioRepo.save(usuario);
        assertEquals( usuario.getAutorizacoes().size(), 1);
        
    }

}
