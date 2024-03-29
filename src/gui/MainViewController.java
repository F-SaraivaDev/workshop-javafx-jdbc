package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.DepartamentoService;
import model.service.VendedorService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVendedor;

	@FXML
	private MenuItem menuItemDepartamento;

	@FXML
	private MenuItem menuItemSobre;

	@FXML
	public void onMenuItemVendedorAction() {
		loadView("/gui/VendedorList.fxml", (VendedorListController controller) -> {
			controller.setVendedorService(new VendedorService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemDepartamentoAction() {
		loadView("/gui/DepartamentoList.fxml", (DepartamentoListController controller) -> {
			controller.setDepartamentoService(new DepartamentoService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemSobretAction() {
		loadView("/gui/Sobre.fxml", x -> {});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	private synchronized <T> void loadView(String nomeAbsoluto, Consumer<T> acaoInicializacao) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			VBox novoVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(novoVBox.getChildren());
			
			T controller = loader.getController();
            acaoInicializacao.accept(controller);
			
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Erro ao carregar p�gina", e.getMessage(), AlertType.ERROR);

		}

	}

}
