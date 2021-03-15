package br.gov.sp.fatec.springbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.sp.fatec.springbootapp.entity.Usuario;

/*  Os parâmetros serão a classe da entidade e o tipo do id
    A implementação será feita pelo spring-boot
   */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}
