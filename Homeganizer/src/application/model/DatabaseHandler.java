package application.model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;

import org.mindrot.jbcrypt.BCrypt;

import application.view.MessageView;
import javafx.scene.control.Alert.AlertType;

public class DatabaseHandler {
	private HashMap<String, User> users;
	private HashMap<String,Integer> elencoCounter;
	private MemorizedUserPassword memorizedUser;
	private LinkedList<Stanza> stanze;
	private String currentUser;

	private static DatabaseHandler instance = null;

	public static final String nomiCounter[] = { "IDCOUNTER", "IDCOUNTERARMADIO", "IDCOUNTERCASSAPANCA",
			"IDCOUNTERLIBRERIA", "IDCOUNTERSCAFFALE", "IDCOUNTERTAVOLO", "IDCOUNTERBO", "IDCOUNTERCO", "IDCOUNTERDI",
			"IDCOUNTEREL", "IDCOUNTERLI", "IDCOUNTERUT" };

	public static DatabaseHandler getInstance() {
		if (instance == null)
			instance = new DatabaseHandler();
		return instance;
	}

	private DatabaseHandler() {
		memorizedUser = null;
		initialLoad();
	}

	private void initialLoad() {
		users = new HashMap<String, User>();
		File f = new File("database.db");
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:database.db");
			createTablesIfNotExist(con, mustInitializeCounters);
			PreparedStatement stm1 = con.prepareStatement("SELECT * FROM users;");
			ResultSet result = stm1.executeQuery();
			while (result.next()) {
				User u = new User(result.getString("username"), result.getString("password"),
						result.getString("secureAnswer"));
				users.put(u.getUsername(), u);
			}
			stm1 = con.prepareStatement("SELECT * FROM memUser;");
			result = stm1.executeQuery();
			while (result.next())
				memorizedUser = new MemorizedUserPassword(result.getString("username"), result.getString("password"));
			stm1.close();
		} catch (Exception e) {
			MessageView.showMessageAlert(AlertType.ERROR, "Errore",
					"Si � verificato un errore. Contattare l'amministratore");
		}
	}

	private static void createTablesIfNotExist(Connection con, boolean mustInitializeCounters) throws Exception {
		PreparedStatement stm = con.prepareStatement(
				"CREATE TABLE IF NOT EXISTS users(username varchar(2),password varchar(2),secureAnswer varchar(2));");
		stm.executeUpdate();
		stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS memUser(username varchar(2),password varchar(2));");
		stm.executeUpdate();
		stm = con.prepareStatement(
				"CREATE TABLE IF NOT EXISTS stanze(id varchar(2),nome varchar(2),proprietario varchar(2),larghezza int,profondit� int);");
		stm.executeUpdate();
		stm = con.prepareStatement(
				"CREATE TABLE IF NOT EXISTS mobili(id varchar(2),idStanza varchar(2),nome varchar(2),tipo varchar(2),x int,y int,w int,h int);");
		stm.executeUpdate();
		stm = con.prepareStatement(
				"CREATE TABLE IF NOT EXISTS oggetti(id varchar(2),idMobile varchar(2),nome varchar(2),descrizione varchar(2),tipo varchar(2));");
		stm.executeUpdate();
		stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS counter(tipo varchar(2),valore int);");
		stm.executeUpdate();
		if (mustInitializeCounters(stm))
			initializeCounters(con, stm);
		stm.close();
	}

	private static boolean mustInitializeCounters(PreparedStatement stm) {
		return false;
	}

	private static void initializeCounters(Connection con, PreparedStatement stm) throws Exception {
		for (int i = 0; i < 12; i++) {
			stm = con.prepareStatement("INSERT INTO counter VALUES(?,?);");
			stm.setString(1, nomiCounter[i]);
			stm.setInt(2, 1);
			stm.executeUpdate();
		}
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

	public void addMemorizedUser(String username, String password) {
		clearMemorizedUser();
		memorizedUser = new MemorizedUserPassword(username, password);
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:database.db");
			PreparedStatement stm1 = con.prepareStatement("INSERT INTO memUser VALUES(?,?);");
			stm1.setString(1, memorizedUser.getUsername());
			stm1.setString(2, memorizedUser.getPassword());
			stm1.executeUpdate();
			stm1.close();
		} catch (Exception e) {
			MessageView.showMessageAlert(AlertType.ERROR, "Errore",
					"Si � verificato un errore. Contattare l'amministratore");
		}
	}

	public void clearMemorizedUser() {
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:database.db");
			PreparedStatement stm1 = con.prepareStatement("DELETE FROM memUser;");
			stm1.executeUpdate();
			stm1.close();
		} catch (Exception e) {
			MessageView.showMessageAlert(AlertType.ERROR, "Errore",
					"Si � verificato un errore. Contattare l'amministratore");
		}
	}

	public void addUser(User u) {
		users.put(u.getUsername(), u);
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:database.db");
			PreparedStatement stm1 = con.prepareStatement("INSERT INTO users VALUES(?,?,?);");
			stm1.setString(1, u.getUsername());
			stm1.setString(2, u.getPassword());
			stm1.setString(3, u.getSecureAnswer());
			stm1.executeUpdate();
			stm1.close();
		} catch (Exception e) {
			MessageView.showMessageAlert(AlertType.ERROR, "Errore",
					"Si � verificato un errore. Contattare l'amministratore");
		}
	}

	public boolean userExists(String u) {
		return users.containsKey(u);
	}

	public boolean userAnswer(String insertedUsername, String secureAnswer) {
		return users.get(insertedUsername).getSecureAnswer().equals(secureAnswer);
	}

	public void UpdateUser(User u) {
		users.put(u.getUsername(), u);
		try {
			Connection con = DriverManager.getConnection("jdbc:sqlite:database.db");
			PreparedStatement stm1 = con.prepareStatement("UPDATE users SET password=? WHERE username=?;");
			stm1.setString(1, u.getPassword());
			stm1.setString(2, u.getUsername());
			stm1.executeUpdate();
			stm1.close();
		} catch (Exception e) {
			MessageView.showMessageAlert(AlertType.ERROR, "Errore",
					"Si � verificato un errore. Contattare l'amministratore");
		}
	}

	public MemorizedUserPassword getMemorizedUser() {
		return memorizedUser;
	}

	public LinkedList<Stanza> getStanze() {
		return stanze;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public void loadRooms(String insertedUsername) throws Exception {
		Connection con = DriverManager.getConnection("jdbc:sqlite:database.db");
		PreparedStatement stm1 = con.prepareStatement("SELECT * FROM stanze WHERE proprietario=?;");
		stm1.setString(1, insertedUsername);
		ResultSet result = stm1.executeQuery();
		while (result.next()) {
			Stanza s = new Stanza(result.getString("id"), result.getString("nome"), result.getString("proprietario"),
					result.getInt("larghezza"), result.getInt("profondit�"));
			loadFurniture(result.getString("id"), s, con);
			stanze.add(s);
		}
		stm1.close();
	}

	private void loadFurniture(String idStanza, Stanza s, Connection con) throws Exception {
		PreparedStatement stm1 = con.prepareStatement("SELECT * FROM mobili WHERE idStanza=?;");
		stm1.setString(1, idStanza);
		ResultSet result = stm1.executeQuery();
		while (result.next()) {
			Mobile m = new Mobile(result.getString("id"), result.getString("idStanza"), result.getString("nome"),
					result.getString("tipo"), result.getInt("x"), result.getInt("y"), result.getInt("w"),
					result.getInt("h"));
			loadObjects(result.getString("id"), m, con);
			s.aggiungiMobile(m);
		}
		stm1.close();
	}

	private void loadObjects(String idMobile, Mobile m, Connection con) throws Exception {
		PreparedStatement stm1 = con.prepareStatement("SELECT * FROM oggetti WHERE idMobile=?;");
		stm1.setString(1, idMobile);
		ResultSet result = stm1.executeQuery();
		while (result.next()) {
			Oggetto o = new Oggetto(result.getString("id"), result.getString("idMobile"), result.getString("nome"),
					result.getString("descrizione"), result.getString("tipo"));
			m.aggiungiOggetto(o);
		}
		stm1.close();
	}

}
