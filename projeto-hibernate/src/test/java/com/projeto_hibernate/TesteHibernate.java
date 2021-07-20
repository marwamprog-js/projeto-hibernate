package com.projeto_hibernate;

import java.util.List;

import org.junit.Test;

import com.projeto_hibernate.dao.DaoGeneric;
import com.projeto_hibernate.model.Telefone;
import com.projeto_hibernate.model.UsuarioPessoa;

public class TesteHibernate {

	@Test
	public void TesteHibernateUtil() {
		
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa pessoa = new UsuarioPessoa();
		pessoa.setIdade(32);
		pessoa.setEmail("debora@teste.com");
		pessoa.setLogin("debora");
		pessoa.setNome("Debora");
		pessoa.setSenha("123");
		pessoa.setSobreNome("Cristina");
		
		
		dao.salvar(pessoa);
		
	}
	
	
	
	@Test
	public void testeUpdate() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
				
		UsuarioPessoa pessoa = dao.pesquisarDireto(2L, UsuarioPessoa.class);
		
		pessoa.setIdade(27);
		
		pessoa = dao.updateMerge(pessoa);
		
		System.out.println(pessoa);
	}
	
	
	
	@Test
	public void testeDelete() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa pessoa = dao.pesquisarDireto(10L, UsuarioPessoa.class);
		
		dao.deletePorId(pessoa);
	}
	
	
	@Test
	public void testeListar() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		
		List<UsuarioPessoa> lista = dao.listar(UsuarioPessoa.class);
		
		for(UsuarioPessoa pessoa : lista) {
			System.out.println(pessoa);
		}

	}
	
	
	
	@Test
	public void testeBuscar() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa pessoa = new UsuarioPessoa();
		pessoa.setId(2L);
		
		pessoa = dao.pesquisar(pessoa);
		
		System.out.println(pessoa);
	}
	
	
	
	@Test
	public void testeBuscar2() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
				
		UsuarioPessoa pessoa = dao.pesquisarDireto(1L, UsuarioPessoa.class);
		
		System.out.println(pessoa);
	}
	
	@Test
	public void testeQueryList() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> lista = dao.getEntityManager().createQuery(" from UsuarioPessoa Where id = 2").getResultList();
		
		for(UsuarioPessoa pessoa : lista) {
			System.out.println(pessoa);
		}
	}
	
	
	@Test
	public void testeQueryListMaxResult() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> lista = dao.getEntityManager().
				createQuery(" from UsuarioPessoa order by id").setMaxResults(3)
				.getResultList();
		
		for(UsuarioPessoa pessoa : lista) {
			System.out.println(pessoa);
		}
	}
	
	
	@Test
	public void testeQueryListParameter() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> lista = dao.getEntityManager().createQuery("from UsuarioPessoa where nome = :nome or sobrenome = :sobrenome")
				.setParameter("nome", "Marwam")
				.setParameter("sobrenome", "Cristina")
				.getResultList();
		
		for(UsuarioPessoa pessoa : lista) {
			System.out.println(pessoa);
		}
	}
	
	
	
	@Test
	public void testeSomaIdade() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		Double somaIdade = (Double) dao.getEntityManager()
				.createQuery("select avg(u.idade) from UsuarioPessoa u")
				.getSingleResult();
		
		System.out.println("Média de todas as idades é = " + somaIdade);
	}
	
	
	@Test
	public void testeNamedQueryFindAll() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> lista = dao.getEntityManager().createNamedQuery("UsuarioPessoa.findAll").getResultList();
		
		for(UsuarioPessoa pessoa : lista) {
			System.out.println(pessoa);
		}
	}
	
	
	
	@Test
	public void testeNamedQueryPorNome() {
		DaoGeneric<UsuarioPessoa> dao = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> lista = dao.getEntityManager()
				.createNamedQuery("UsuarioPessoa.buscaPorNome")
				.setParameter("nome", "Marwam")
				.getResultList();
		
		for(UsuarioPessoa pessoa : lista) {
			System.out.println(pessoa);
		}
	}
	
	
	@Test
	public void testeGravaTelefone() {
		DaoGeneric dao = new DaoGeneric();
		UsuarioPessoa pessoa = (UsuarioPessoa) dao.pesquisarDireto(2L, UsuarioPessoa.class);
		
		Telefone tel = new Telefone();
		tel.setTipo("Casa");
		tel.setNumero("(11) 9977-7788");
		tel.setPessoa(pessoa);
		
		
		dao.salvar(tel);
		
	}
	
	
	
	@Test
	public void testeConsultaTelefones() {
		DaoGeneric dao = new DaoGeneric();
		UsuarioPessoa pessoa = (UsuarioPessoa) dao.pesquisarDireto(1L, UsuarioPessoa.class);
		
		System.out.println(pessoa.getNome());
		System.out.println("");
		for (Telefone fone : pessoa.getTelefones()) {
			//System.out.println(fone.getPessoa().getNome());
			System.out.println(fone.getTipo());
			System.out.println(fone.getNumero());			
			System.out.println("------------------------------------------");
		}
		
	}
	
	
}
