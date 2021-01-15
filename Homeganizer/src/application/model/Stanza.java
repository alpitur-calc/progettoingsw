package application.model;

import java.util.ArrayList;
import java.util.LinkedList;

public class Stanza {

	private String id;
	private int larghezza, profonditā;
	private String proprietario;
	private LinkedList<Mobile> mobili;
	private ArrayList<String> whitelisted;
	
	public Stanza(String Id, String Proprietario, int Larghezza, int Profonditā) {
		this.id= Id;
		this.proprietario= Proprietario;
		this.larghezza= Larghezza;
		this.profonditā= Profonditā;
		this.mobili = new LinkedList<Mobile>();
	}
	
	public void aggiungiMobile(Mobile m) {
		mobili.add(m);
	}
	
	public boolean isWhitelisted(String id) {
		for(String p : whitelisted) {
			if(p.equals(id)) { return true; }
		}	
				
		return false;
	}
	
	public LinkedList <Mobile> getMobili() {
		return mobili;
	}
	
	public String getId() {
		return id;
	}

	public int getLarghezza() {
		return larghezza;
	}

	public int getProfonditā() {
		return profonditā;
	}

	public String getProprietario() {
		return proprietario;
	}
			
}
