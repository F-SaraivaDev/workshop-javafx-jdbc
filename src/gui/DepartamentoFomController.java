package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.mysql.jdbc.UpdatableResultSet;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entidade.Departamento;
import model.exceptions.ValidacaoException;
import model.service.DepartamentoService;

public class DepartamentoFomController implements Initializable {

	private Departamento entidade;

	private DepartamentoService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private Label labelErroNome;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void setDeparmento(Departamento entidade) {
		this.entidade = entidade;
	}

	public void setDepartamentoService(DepartamentoService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent evento) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade está nula");
		}

		if (service == null) {
			throw new IllegalStateException("Entidade está nula");
		}

		try {
			entidade = getFormDados();
			service.salvarOuAtualizar(entidade);
			notifyDataChangeListeners();
			Utils.currentStage(evento).close();

		} catch(ValidacaoException e){
			setMensagemErro(e.getErros());
			
     	}catch (DbException e) {
			Alerts.showAlert("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Departamento getFormDados() {
		Departamento obj = new Departamento();

		ValidacaoException exception = new ValidacaoException("Erro de validação");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "O campo não pode ser vazio!");
		}

		obj.setNome(txtNome.getText());

		if (exception.getErros().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utils.currentStage(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		iniciarNodes();
	}

	private void iniciarNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtId, 30);
	}

	public void atualizarDadosForm() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade era nula");
		}

		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
	}
	
	private void setMensagemErro(Map<String, String> erro) {
		Set<String> campos = erro.keySet();
		
		if(campos.contains("nome")) {
			labelErroNome.setText(erro.get("nome"));
		}
	}

}























