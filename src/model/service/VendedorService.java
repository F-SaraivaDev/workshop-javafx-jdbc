package model.service;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.VendedorDao;
import model.entidade.Vendedor;

public class VendedorService {
	
	private VendedorDao dao = DaoFactory.criarVendedorDao();
	
	public List<Vendedor> findAll(){
	
	List<Vendedor> lista = new ArrayList<>();
	    return dao.findAll();
	}
	
	public void salvarOuAtualizar(Vendedor obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		
		}else {
			dao.update(obj);
		}
	}
	
	public void remover(Vendedor obj) {
		dao.deleteById(obj.getId());
	}
}
