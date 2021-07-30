package com.projeto_hibernate.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.projeto_hibernate.HibernateUtil;

public class DaoGeneric<E> {

	private EntityManager entityManager = HibernateUtil.getEntityManager();
	
	
	/*
	 * LISTAR TODOS
	 * */
	public List<E> listar(Class<E> entidade){
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("from " + entidade.getName() + " order by id desc").getResultList();
		
		transaction.commit();
		
		return lista;
	}
	
	
	/*
	 * UPDATE MERGE - SALVA OU ATUALIZA
	 * */
	public E updateMerge(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadeSalva = entityManager.merge(entidade);
		transaction.commit();
		
		return entidadeSalva;
	}
	
	
	/*
	 * DELETE
	 * */
	public void deletePorId(E entidade) throws Exception {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		entityManager.createNativeQuery("delete from " + entidade.getClass().getSimpleName().toLowerCase() + " where id = " + id).executeUpdate();
		
		transaction.commit();
	}
	
	
	/*
	 * SALVAR
	 * */
	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}
	
	/*
	 * Pesquisar
	 * */
	public E pesquisar(E entidade) {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		
		E e = (E) entityManager.find(entidade.getClass(), id);
		
		return e;
	}
	
	
	
	
	public E pesquisarDireto(Long id, Class<E> entidade) {
				
		entityManager.clear();
		
		E e = (E) entityManager.createQuery("from " + entidade.getSimpleName() + " where id = " + id).getSingleResult();
		
		return e;
	}
	
	
	
	public EntityManager getEntityManager() {
		return entityManager;
	}


	
	
	
}
