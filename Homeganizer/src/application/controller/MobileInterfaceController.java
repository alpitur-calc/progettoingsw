package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.model.Mobile;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MobileInterfaceController implements Initializable {

    @FXML
    private TextField txtNome;

    @FXML
    private ComboBox<String> cmbTipo;

    @FXML
    private Button btnConferma;

    @FXML
    private Button btnAnnulla;
 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		cmbTipo.getItems().add(Mobile.ARMADIO);
		cmbTipo.getItems().add(Mobile.CASSAPANCA);
		cmbTipo.getItems().add(Mobile.LIBRERIA);
		cmbTipo.getItems().add(Mobile.SCAFFALE);
		cmbTipo.getItems().add(Mobile.TAVOLO);
		
		btnConferma.setOnMouseClicked(handleBtnConfermaClicked);
		btnAnnulla.setOnMouseClicked(handleBtnAnnullaClicked);
	}
	
	private EventHandler<MouseEvent> handleBtnConfermaClicked = new EventHandler<MouseEvent>(){

		@Override
		public void handle(MouseEvent event) {
			MainInterfaceController.ConfermaCreazioneMobile= true;
			Stage thisStage = (Stage) btnConferma.getScene().getWindow();
	    	thisStage.close();
		}
		
	};
	
	private EventHandler<MouseEvent> handleBtnAnnullaClicked = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			MainInterfaceController.ConfermaCreazioneMobile= false;
			Stage thisStage = (Stage) btnConferma.getScene().getWindow();
	    	thisStage.close();
		}
		
	};
}
