package application.model;

import java.util.LinkedList;

import application.controller.MainInterfaceController;
import application.view.MessageView;
import application.view.Piantina;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class RoomHandler {

	private static RoomHandler instance = null;
	private LinkedList<Stanza> stanze;
	private String idProprietario;

	public static RoomHandler getInstance() {
		if (instance == null)
			instance = new RoomHandler();
		return instance;
	}

	private RoomHandler() {
		stanze = DatabaseHandler.getInstance().getStanze();
		if(stanze == null)
			stanze = new LinkedList<Stanza>();
	}

	public boolean aggiungiStanza(String nome, int larghezza, int profondit�) {
		stanze.add(new Stanza(nome, idProprietario, larghezza, profondit�));
		return true;
	}

	public boolean rimuoviStanza(String id) {
		for (Stanza i : stanze) {
			if (i.getId() == id) {
				stanze.remove(i);
			}
		}
		return true;
	}

	public void caricaStanze() {
		stanze = DatabaseHandler.getInstance().getStanze();
	}
	
	public void salvaStanze() {
		try {
			DatabaseHandler.getInstance().saveRooms(stanze);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getProprietario() {
		return idProprietario;
	}

	public void setProprietario(String idProprietario) {
		this.idProprietario = idProprietario;
	}

	public LinkedList<Stanza> getStanze() {
		return stanze;
	}

	public boolean ricerca(String txtspotlighttext) {

		for (Stanza s : RoomHandler.getInstance().getStanze()) {
			for (Mobile m : s.getMobili()) {
				{
					if (m.getNome().equals(txtspotlighttext)) {
						MainInterfaceController.setStanzaCorrente(s);
						MainInterfaceController.setMobileCorrente(m);
						Piantina.getInstance().evidenziaMobile();
						return true;
					}
					for (Oggetto o : m.getOggetti()) {
						if (o.getNome().equals(txtspotlighttext)) {
							MainInterfaceController.setStanzaCorrente(s);
							MainInterfaceController.setMobileCorrente(m);
							Piantina.getInstance().evidenziaMobile();
							return true;
						}
					}
				}
			}
		}
		MessageView.showMessageAlert(AlertType.INFORMATION, "Errore", "Il mobile ricercato non � presente.");
		return false;
	}

}
