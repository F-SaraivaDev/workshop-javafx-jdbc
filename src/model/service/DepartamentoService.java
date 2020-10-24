package model.service;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartamentoDao;
import model.entidade.Departamento;

public class DepartamentoService {
	
	private DepartamentoDao dao = DaoFactory.criarDepartamentoDao();
	
	public List<Departamento> findAll(){
	
	List<Departamento> lista = new ArrayList<>();
	    return dao.findAll();
	}
}
