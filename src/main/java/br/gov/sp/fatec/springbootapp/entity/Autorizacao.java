package br.gov.sp.fatec.springbootapp.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import br.gov.sp.fatec.springbootapp.controller.View;

@Entity
@Table(name = "aut_autorizacao")
public class Autorizacao {

    @JsonView(View.UsuarioCompleto.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aut_id")
    private Long id;

    @JsonView( { View.UsuarioResumo.class , View.AutorizacaoResumo.class } )
    @Column(name = "aut_nome")
    private String nome;

    /*
     * Aqui tem que ser o LAZY porque no Usuario.java é EAGER. 
     * Não precisa mapear as colunas (Join Table) mas precisa colocar o atributo que contém este mapeamento
     * O atributo "autorizacoes" do Usuario.java é onde está o mapeamento dos joins.
     */
    @JsonView( View.AutorizacaoResumo.class )
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "autorizacoes") 
    //@JsonIgnore
    private Set<Usuario> usuarios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
   
    
    
}
