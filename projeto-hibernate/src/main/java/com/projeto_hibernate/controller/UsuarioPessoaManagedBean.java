package com.projeto_hibernate.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;
import com.projeto_hibernate.dao.DaoEmail;
import com.projeto_hibernate.dao.DaoUsuario;
import com.projeto_hibernate.datatablelazy.LazyDataTableModelUserPessoa;
import com.projeto_hibernate.model.Email;
import com.projeto_hibernate.model.UsuarioPessoa;


@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped
public class UsuarioPessoaManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private LazyDataTableModelUserPessoa<UsuarioPessoa> list = new LazyDataTableModelUserPessoa<UsuarioPessoa>();
	private DaoUsuario<UsuarioPessoa> dao = new DaoUsuario<UsuarioPessoa>();
	private BarChartModel barChartModel = new BarChartModel();
	private Email emailUser = new Email();
	private DaoEmail<Email> daoEmail = new DaoEmail<Email>();
	private String campoPesquisa;

	
	@PostConstruct
	public void init() {
		list.load(0, 5, null, null, null);
		montarGrafico();
	}


	private void montarGrafico() {
		barChartModel = new BarChartModel();
		
		ChartSeries userSalario = new ChartSeries();			

		for (UsuarioPessoa usuario : list.list) {
			if(usuario.getSalario() != null) {
				userSalario.set(usuario.getNome(), usuario.getSalario()); //Adicionando na lista MAP				
			}
		}
		
		barChartModel.addSeries(userSalario); //Adiciona toda a lista no gráfico		
		barChartModel.setTitle("Gráfico de salários");
	}

	
	/*
	 * Pesquisar
	 * */
	public void pesquisar() {
		
		list.pesquisar(campoPesquisa);
		montarGrafico();
	}
	
	
	/*
	 * Metódo para Download da IMAGEM
	 * */
	public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		
		String fileDownloadId = params.get("fileDownloadId");		
		UsuarioPessoa pessoa = dao.pesquisarDireto(Long.parseLong(fileDownloadId), UsuarioPessoa.class);
		
		byte[] imagem = new Base64().decodeBase64(pessoa.getImagem().split("\\,")[1]);
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		response.addHeader("Content-Disposition", "attachment;filename=download.png");
		response.setContentType("application/octet-stream");
		response.setContentLength(imagem.length);
		response.getOutputStream().write(imagem);
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	
	/*
	 * REceber arquivo upload
	 * */
	public void upload(FileUploadEvent imagem) {
		
		String img = "data:image/png;base64," + DatatypeConverter.printBase64Binary(imagem.getFile().getContents());		
		usuarioPessoa.setImagem(img);
		
	}
	
	
	/*
	 * REMOVER EMAIL
	 * */
	public void removerEmail() throws Exception {
		
		String codigoEmail = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codigoEmail");
		
		Email remover = new Email();
		remover.setId(Long.parseLong(codigoEmail));
		daoEmail.deletePorId(remover);
		usuarioPessoa.getEmails().remove(remover);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado", "Removido com sucesso!"));
		
	}
	
	/*
	 * Adicionar EMAILS
	 * */
	public void addEmail() {
		
		emailUser.setUsuarioPessoa(usuarioPessoa);
		emailUser = daoEmail.updateMerge(emailUser);
		usuarioPessoa.getEmails().add(emailUser);
		
		emailUser = new Email();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado", "Salvo com sucesso!"));
	}
	
	/*
	 * PESQUISA CEP viaCEP (API)
	 * */
	public void pesquisaCep(AjaxBehaviorEvent event) {
		
		try {
			
			URL url = new URL("https://viacep.com.br/ws/" + usuarioPessoa.getCep() + "/json/");
			
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while((cep = br.readLine()) != null) {
				
				jsonCep.append(cep);
				
			}
			
			UsuarioPessoa userCepPessoa = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);
			
			usuarioPessoa.setCep(userCepPessoa.getCep());
			usuarioPessoa.setLogradouro(userCepPessoa.getLogradouro());
			usuarioPessoa.setComplemento(userCepPessoa.getComplemento());
			usuarioPessoa.setNumero(userCepPessoa.getNumero());
			usuarioPessoa.setBairro(userCepPessoa.getBairro());
			usuarioPessoa.setLocalidade(userCepPessoa.getLocalidade());
			usuarioPessoa.setUf(userCepPessoa.getUf());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/*
	 * SALVAR
	 * */
	public String salvar() {
		dao.salvar(usuarioPessoa);
		
		if(!list.list.contains(usuarioPessoa)) {
			list.list.add(usuarioPessoa);
		} else {
			list.list.remove(usuarioPessoa);
			list.list.add(usuarioPessoa);
		}
		
		usuarioPessoa = new UsuarioPessoa();
		
		init();
		
		//Mensagem de salvo com sucesso
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));

		return "";
	}

	
	
	/*
	 * EXCLUIR
	 * */
	public String excluir() {	

		try {
			
			dao.removerUsuario(usuarioPessoa);
			list.list.remove(usuarioPessoa);
			usuarioPessoa = new UsuarioPessoa();
			
			init();
			
			FacesContext.getCurrentInstance()
			.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Excluido com sucesso!"));
			
		} catch (Exception e) {
			e.printStackTrace();
			
			if(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance()
					.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Existem telefones para o Usuário!"));
			}
		}

		return "";
	}

	
	/*
	 * NOVO
	 * */
	public String novo() {
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}


	
	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}
	public String getCampoPesquisa() {
		return campoPesquisa;
	}
	public void setEmailUser(Email emailUser) {
		this.emailUser = emailUser;
	}
	public Email getEmailUser() {
		return emailUser;
	}
	public BarChartModel getBarChartModel() {
		return barChartModel;
	}
	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}
	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	public LazyDataTableModelUserPessoa<UsuarioPessoa> getList() {
		montarGrafico();
		return list;
	}
}
