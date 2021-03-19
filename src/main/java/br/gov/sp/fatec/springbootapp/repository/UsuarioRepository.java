package br.gov.sp.fatec.springbootapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.gov.sp.fatec.springbootapp.entity.Usuario;

/*  Os parâmetros serão a classe da entidade e o tipo do id
    A implementação será feita pelo spring-boot
   */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * I Spring fornece um Query Method   
     * Meu resultado pode voltar uma lista ou somente um
     * O nome do método tem que começar com "findBy" + atributo
     * Ex. para o atributo "nome"
     *  findByNome (Que seria o mesmo que findByNomeEquals)
     * Ex. para usar o like
     *  findByNomeContains(String nome);
     *  findByNomeContainsIgnoreCase(String nome);
     * 
     * 
     */
    public List<Usuario> findByNomeContainsIgnoreCase(String nome);

    public Usuario findByNome(String nome); //O atributo nome é unique key.

    @Query("select u from Usuario u where u.nome = ?1")
    public Usuario buscarUsuarioPorNome(String nome);

    //Para buscar por mais de um parâmetro (Exemplo para login)
    public Usuario findByNomeAndSenha(String nome, String senha);

    @Query("select u from Usuario u where u.senha = ?2 and u.nome = ?1")
    public Usuario buscarUsuarioPorNomeESenha(String nome, String senha);

    //Buscar todo mundo que é administrador
    //O Nome, neste caso, é um atributo da classe Autorizacao
    public List<Usuario> findByAutorizacoesNome(String nomeAutorizacao);

    //Join
    @Query("select u from Usuario u inner join u.autorizacoes a where a.nome = ?1 order by u.nome")
    public List<Usuario> buscarUsuariosPorNomeDeAutorizacao(String nomeAutorizacao);



}
