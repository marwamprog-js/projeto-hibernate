package com.projeto_hibernate.dao;

import java.util.List;

import javax.persistence.Query;

import com.projeto_hibernate.model.UsuarioPessoa;

import net.bytebuddy.implementation.bind.annotation.Super;

public class DaoUsuario<E> extends DaoGeneric<UsuarioPessoa>{

	
	/*
	 * Método para remover Usuário com todos os seus dados
	 * */
	public void removerUsuario(UsuarioPessoa usuario) throws Exception {
		
		getEntityManager().getTransaction().begin();
		
		getEntityManager().remove(usuario);
		
		getEntityManager().getTransaction().commit();
		
		super.deletePorId(usuario);
	}

	public List<UsuarioPessoa> pesquisar(String campoPesquisa) {
		
		Query query = super.getEntityManager().createQuery("from UsuarioPessoa where nome like '%"+campoPesquisa+"%'");
				
		return query.getResultList();
	}

	
	
	
}
