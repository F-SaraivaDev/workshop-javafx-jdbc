package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departamento;
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
	public void onBtNovoAction() {
		System.out.println("onBtNovoAction");
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
}
























