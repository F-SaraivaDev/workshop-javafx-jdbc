package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidade.Departamento;
import model.service.DepartamentoService;

public class ListaDepartamentoController implements Initializable {
	
	private DepartamentoService service;
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML
	private TableColumn<Departamento, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Departamento, String> TableColumnNome;
	
	@FXML
	private Button btNovo;
	
	private ObservableList<Departamento> obsList;
	
	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		Stage parentStage = Utils.currentStage(evento);
		criarDialogoForm("/gui/DepartamentoForm.fxml", parentStage);
	}
	
	public void setDepartamentoService(DepartamentoService service){
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
		if(service == null) {
			throw new IllegalStateException("O serviço era nulo");
		}
		List<Departamento> lista = service.findAll();
		obsList = FXCollections.observableArrayList(lista);
		tableViewDepartamento.setItems(obsList);
	}
	
	private void criarDialogoForm(String nomeAbsoluto, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane pane = loader.load();
			
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
}
























