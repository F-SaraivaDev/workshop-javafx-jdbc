package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.VendedorDao;
import model.entidade.Departamento;
import model.entidade.Vendedor;

public class VendedorDaoJDBC implements VendedorDao {

	private Connection conn;

	public VendedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Vendedor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
							"INSERT INTO vendedor "
									+ "(Nome, Email, DataNascimento, SalarioBase, DepartamentoId) "
									+ "VALUES (?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());

			int linhasAfetadas = st.executeUpdate();

			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
				
			} else {
				throw new DbException("Erro inesperado! Nenhuma linha afetada!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Vendedor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE vendedor "  
					+ "SET Nome = ?, Email = ?, DataNascimento = ?, SalarioBase = ?, DepartamentoId = ? " 
					+ "WHERE Id = ?"); 

			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}


	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM vendedor WHERE Id = ? ");
			
			st.setInt(1, id);
			
			st.executeUpdate();
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		
		}finally{
			DB.closeStatement(st);
		}

	}

	@Override
	public Vendedor findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn
					.prepareStatement("SELECT vendedor.*,departamento.Nome as DepNome "
							+ "FROM vendedor INNER JOIN departamento "
							+ "ON vendedor.DepartamentoId = departamento.Id "
							+ "WHERE vendedor.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Departamento dep = instanciarDepartamento(rs);
				Vendedor obj = instanciarVendedor(rs, dep);

				return obj;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Vendedor instanciarVendedor(ResultSet rs, Departamento dep)
			throws SQLException {
		Vendedor obj = new Vendedor();
		obj.setId(rs.getInt("Id"));
		obj.setNome(rs.getString("Nome"));
		obj.setEmail(rs.getString("Email"));
		obj.setSalarioBase(rs.getDouble("SalarioBase"));
		obj.setDataNascimento(rs.getDate("DataNascimento"));
		obj.setDepartamento(dep);
		return obj;
	}

	private Departamento instanciarDepartamento(ResultSet rs)
			throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("DepartamentoId"));
		dep.setNome(rs.getString("DepNome"));
		return dep;
	}

	@Override
	public List<Vendedor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn
					.prepareStatement("SELECT vendedor.*,departamento.Nome as DepNome "
							+ "FROM vendedor INNER JOIN departamento "
							+ "ON vendedor.DepartamentoId = departamento.Id "
							+ "ORDER BY Nome");

			rs = st.executeQuery();

			List<Vendedor> lista = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();

			while (rs.next()) {

				Departamento dep = map.get(rs.getInt("DepartamentoId"));

				if (dep == null) {
					dep = instanciarDepartamento(rs);
					map.put(rs.getInt("DepartamentoId"), dep);
				}

				Vendedor obj = instanciarVendedor(rs, dep);
				lista.add(obj);
			}
			return lista;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Vendedor> findByDepartamento(Departamento departamento) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn
					.prepareStatement("SELECT vendedor.*,departamento.Nome as DepNome "
							+ "FROM vendedor INNER JOIN departamento "
							+ "ON vendedor.DepartamentoId = departamento.Id "
							+ "WHERE DepartamentoId = ? " + "ORDER BY Nome");

			st.setInt(1, departamento.getId());
			rs = st.executeQuery();

			List<Vendedor> lista = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();

			while (rs.next()) {

				Departamento dep = map.get(rs.getInt("DepartamentoId"));

				if (dep == null) {
					dep = instanciarDepartamento(rs);
					map.put(rs.getInt("DepartamentoId"), dep);
				}

				Vendedor obj = instanciarVendedor(rs, dep);
				lista.add(obj);
			}
			return lista;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
