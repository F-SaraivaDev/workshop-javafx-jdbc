package gui;

import java.net.URL;
import java.util.Date;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entidade.Vendedor;
import model.service.VendedorService;

public class VendedorListController implements Initializable, DataChangeListener {

	private VendedorService service;

	@FXML
	private TableView<Vendedor> tableViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> tableColumnId;

	@FXML
	private TableColumn<Vendedor, String> TableColumnNome;
	
	@FXML
	private TableColumn<Vendedor, String> TableColumnEmail;
	
	@FXML
	private TableColumn<Vendedor, Date> TableColumnDataNascimento;
	
	@FXML
	private TableColumn<Vendedor, Double> TableColumnSalarioBase;
	
	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnEdit;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnRemove;

	@FXML
	private Button btNovo;

	private ObservableList<Vendedor> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		Stage parentStage = Utils.currentStage(evento);
		Vendedor obj = new Vendedor();
		criarDialogoForm(obj, "/gui/VendedorForm.fxml", parentStage);
	}

	public void setVendedorService(VendedorService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL rul, ResourceBundle rb) {
		iniciarNodes();

	}

	private void iniciarNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		TableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		TableColumnDataNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
		Utils.formatTableColumnDate(TableColumnDataNascimento, "dd/MM/yyyy");
		TableColumnSalarioBase.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
		Utils.formatTableColumnDouble(TableColumnSalarioBase, 2);

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("O serviço era nulo");
		}
		List<Vendedor> lista = service.findAll();
		obsList = FXCollections.observableArrayList(lista);
		tableViewVendedor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
    
	private void criarDialogoForm(Vendedor obj, String nomeAbsoluto, Stage parentStage) {
	/*	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane pane = loader.load();

			VendedorFomController controller = loader.getController();
			controller.setDeparmento(obj);
			controller.setVendedorService(new VendedorService());
			controller.subscribeDataChangeListener(this);
			controller.atualizarDadosForm();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Informe os dados do Vendedor");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a página", e.getMessage(), AlertType.ERROR);
		}*/
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("editar");

			@Override
			protected void updateItem(Vendedor obj, boolean vazio) {
				super.updateItem(obj, vazio);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						evento -> criarDialogoForm(obj, "/gui/VendedorForm.fxml", Utils.currentStage(evento)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("excluir");

			@Override
			protected void updateItem(Vendedor obj, boolean vazio) {
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

	private void removeEntity(Vendedor obj) {
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
