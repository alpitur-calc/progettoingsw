package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.GroupLayout.Alignment;

import application.SceneHandler;
import application.model.RoomHandler;
import application.view.MessageView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MainInterfaceController implements Initializable {

	
    @FXML
    private ListView<Pane> lstRooms;

    @FXML
    private ListView<?> lstFurniture;

    @FXML
    private Menu mnuAccount;

    @FXML
    private MenuItem mtmLogOut;

    @FXML
    private Menu mnuAzioni;

    @FXML
    private Menu mnuAbout;

    @FXML
    private Menu mnuSpotlight;

    @FXML
    private TextField txtSpotlight;

    @FXML
    private Canvas cnvRoom;

    @FXML
    private TextArea txtObjectDescription;
    
    	
    private Button btnAddRoom;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
		Pane p = new Pane();
    	btnAddRoom = new Button();
    	btnAddRoom.setPrefSize(225.0, 35.0);
    	
    	Image buttonImg =  new Image(getClass().getResourceAsStream("/resources/ButtonAddImage.png"));
    	ImageView imgv= new ImageView(buttonImg);
    	btnAddRoom.setGraphic(imgv);
    	btnAddRoom.setAlignment(Pos.CENTER);
    	btnAddRoom.setOnMousePressed(handleBtnAddRoom);
    	
    	p.getChildren().add(btnAddRoom);
    	lstRooms.getItems().add(lstRooms.getItems().size(),p);  	
	}

    @FXML
    void handleMnuAboutClicked(ActionEvent event) {

    }

    @FXML
    void handleMnuAccountCLicked(ActionEvent event) {

    }

    @FXML
    void handleMnuAzioniClicked(ActionEvent event) {

    }

    @FXML
    void handleMnuSpotlightClicked(ActionEvent event) {

    }

    @FXML
	void handleMtmLogOut(ActionEvent event) throws Exception {
    	SceneHandler.getInstance().goToScene("loginInterface.fxml", "Room Editor", 1280, 720);
    }
    
    private EventHandler<MouseEvent> handleBtnAddRoom = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent e) {
				
			String nome="";
			int larghezza = 0, profonditÓ = 0;
			
			Stage roomProp;
			
			roomProp = new Stage();
	    	try {
	    		FXMLLoader loaderRoomProp = new FXMLLoader(getClass().getResource("/application/view/roomPropertiesInterface.fxml"));
	    		AnchorPane rootRoomProp = loaderRoomProp.load();
	    		Scene sceneRoomProp = new Scene(rootRoomProp);
	    		roomProp.setTitle("Inserire dati stanza");
	    		roomProp.setScene(sceneRoomProp);
			} catch (IOException e1) { e1.printStackTrace(); }
	
			roomProp.showAndWait();
			Pane p = (Pane) roomProp.getScene().getRoot().getChildrenUnmodifiable().get(0);
			
			try {
			if(p.getChildren().get(1).getId().equals("txtRoomName")) {
				TextField txtRoomName = (TextField) p.getChildren().get(1);
				nome = txtRoomName.getText();
			}

			if(p.getChildren().get(2).getId().equals("txtHeight")) {
				TextField txtHeight = (TextField) p.getChildren().get(2);
				profonditÓ = Integer.parseInt(txtHeight.getText());
			}

			if(p.getChildren().get(3).getId().equals("txtWidth")) {
				TextField txtWidth = (TextField) p.getChildren().get(3);
				larghezza = Integer.parseInt(txtWidth.getText());
			}
			
			RoomHandler.getInstance().aggiungiStanza(nome, larghezza, profonditÓ);
			addStanzaToList(nome);
			}
			catch(NumberFormatException e2) {
				MessageView.showMessageAlert(AlertType.WARNING, "Dati inseriti non validi", "Per favore, riprovare e inserire i dati correttamente.");
			}
		}
	};
	
	private void addStanzaToList(String nome) {
		Pane p = new Pane();
		HBox h = new HBox();
		h.setAlignment(Pos.CENTER);
		Image img = new Image(getClass().getResourceAsStream("/resources/StanzaIcon.png"));
		ImageView imgViewIcona = new ImageView(img); 
		Label nomeStanza = new Label(nome);
		nomeStanza.setFont(new Font(14));
		h.getChildren().add(imgViewIcona);
		h.getChildren().add(nomeStanza);
		p.getChildren().add(h);
		p.setOnMouseClicked(handleStanzaInListClicked);
    	lstRooms.getItems().add(lstRooms.getItems().size()-1,p);  
	}
	
	private EventHandler<MouseEvent> handleStanzaInListClicked = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			MessageView.showMessageAlert(AlertType.INFORMATION, "Stanza", "Ecco la tua stanza, maledetto!!");
		}
	};
}
