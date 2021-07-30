package com.projeto_hibernate.controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.projeto_hibernate.dao.DaoTelefone;
import com.projeto_hibernate.dao.DaoUsuario;
import com.projeto_hibernate.model.Telefone;
import com.projeto_hibernate.model.UsuarioPessoa;

@ManagedBean(name = "telefoneManagedBean")
@ViewScoped
public class TelefoneManagedBean {

	private UsuarioPessoa usuario = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoUsuario = new DaoUsuario<UsuarioPessoa>();
	private DaoTelefone<Telefone> daoTelefone = new DaoTelefone<Telefone>();
	private Telefone telefone = new Telefone();
	
	
	@PostConstruct
	public void init () {
		
		String idUsuario = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUsuario");
		usuario = daoUsuario.pesquisarDireto(Long.parseLong(idUsuario), UsuarioPessoa.class);
		
	}
	
	
	/*
	 * Novo
	 * */
	public String novo() {
		telefone = new Telefone();
		
		return "";
	}
	
	
	/*
	 * SALVAR
	 * */
	public String salvar() {
		
		telefone.setPessoa(usuario);
		daoTelefone.salvar(telefone);
		telefone = new Telefone();
		
		usuario = daoUsuario.pesquisarDireto(usuario.getId(), UsuarioPessoa.class);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));
		
		return "";
	}
	
	
	
	/*
	 * REMOVER
	 * */
	public String removeTelefone() throws Exception {
		
		daoTelefone.deletePorId(telefone);
		telefone = new Telefone();
		usuario = daoUsuario.pesquisarDireto(usuario.getId(), UsuarioPessoa.class);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Removido com sucesso!"));
		return "";
	}
	
	
	
	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}
	public Telefone getTelefone() {
		return telefone;
	}
	public void setDaoTelefone(DaoTelefone<Telefone> daoTelefone) {
		this.daoTelefone = daoTelefone;
	}
	public DaoTelefone<Telefone> getDaoTelefone() {
		return daoTelefone;
	}
	public void setUsuario(UsuarioPessoa usuario) {
		this.usuario = usuario;
	}
	public UsuarioPessoa getUsuario() {
		return usuario;
	}
	
}
