package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegridadeException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidade.Departamento;
import model.service.DepartamentoService;

public class ListaDepartamentoController implements Initializable, DataChangeListener {

	private DepartamentoService service;

	@FXML
	private TableView<Departamento> tableViewDepartamento;

	@FXML
	private TableColumn<Departamento, Integer> tableColumnId;

	@FXML
	private TableColumn<Departamento, String> TableColumnNome;

	@FXML
	private TableColumn<Departamento, Departamento> tableColumnEdit;

	@FXML
	private TableColumn<Departamento, Departamento> tableColumnRemove;

	@FXML
	private Button btNovo;

	private ObservableList<Departamento> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		Stage parentStage = Utils.currentStage(evento);
		Departamento obj = new Departamento();
		criarDialogoForm(obj, "/gui/DepartamentoForm.fxml", parentStage);
	}

	public void setDepartamentoService(DepartamentoService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL rul, ResourceBundle rb) {
		iniciarNodes();

	}

	private void iniciarNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("O serviço era nulo");
		}
		List<Departamento> lista = service.findAll();
		obsList = FXCollections.observableArrayList(lista);
		tableViewDepartamento.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void criarDialogoForm(Departamento obj, String nomeAbsoluto, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane pane = loader.load();

			DepartamentoFomController controller = loader.getController();
			controller.setDeparmento(obj);
			controller.setDepartamentoService(new DepartamentoService());
			controller.subscribeDataChangeListener(this);
			controller.atualizarDadosForm();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Informe os dados do Departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Departamento obj, boolean vazio) {
				super.updateItem(obj, vazio);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						evento -> criarDialogoForm(obj, "/gui/DepartamentoForm.fxml", Utils.currentStage(evento)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button button = new Button("excluir");

			@Override
			protected void updateItem(Departamento obj, boolean vazio) {
				super.updateItem(obj, vazio);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(evento -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Departamento obj) {
	   Optional<ButtonType> resultado = Alerts.showConfirmation("Confirmação", "Você tem certeza que quer deletar?");
	   
	   if(resultado.get() == ButtonType.OK) {
		   if(service == null) {
			   throw new IllegalStateException("Serviço era nulo");
		   }
		   try {
			   service.remover(obj);
			   updateTableView();
			
		      } catch (DbIntegridadeException e) {
			     Alerts.showAlert("Erro ao remover o objeto", null, e.getMessage(), AlertType.ERROR); 
		  }
		   
	   }
	}
}
