package model.dao;

import java.util.List;

import model.entidade.Departamento;

public interface DepartamentoDao {

	void insert(Departamento obj);	
	void update(Departamento obj);
	void deleteById(Integer id);
	
	Departamento findById(Integer id);
	
	List<Departamento> findAll();
	
	
}
