package com.projeto_hibernate.datatablelazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.projeto_hibernate.dao.DaoUsuario;
import com.projeto_hibernate.model.UsuarioPessoa;

public class LazyDataTableModelUserPessoa<T> extends LazyDataModel<UsuarioPessoa>{

	private DaoUsuario<UsuarioPessoa> daoUsuario = new DaoUsuario<UsuarioPessoa>();
	public List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	
	private String sql = " from UsuarioPessoa ";
	
	@Override
	public List<UsuarioPessoa> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		
		//O que vai ser mostrado em uma pagina
		list = daoUsuario.getEntityManager().createQuery(getSql())
				.setFirstResult(first)
				.setMaxResults(pageSize).getResultList();
		
		sql = " from UsuarioPessoa";		
		
		setPageSize(pageSize);
		
		Integer quantidadeRegistros = Integer.parseInt(daoUsuario.getEntityManager().createQuery("select count(1) " + getSql()).getSingleResult().toString());
		setRowCount(quantidadeRegistros); //Saber o total de registros para paginação
		return list;
		
	
	}
	
	
	/*
	 * Método para concatenar query quando 
	 * usuário fizer uma pesquisa por Nome no formulário
	 * */
	public void pesquisar(String campoPesquisa) {
		sql += " where nome like '%"+campoPesquisa+"%'";
	}
	
	public List<UsuarioPessoa> getList() {
		return list;
	}
	public String getSql() {
		return sql;
	}
	
	
}
