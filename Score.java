package hangeman;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Score extends Stage{
	
	private Stage owner;
	private GridPane rootNode;
	private int punkte;
	private Listenelement [] bestehenliste;
	private int anzal;
	private String dateiName;
	
	
	public Score(Stage fenster) {
		super();
		owner = fenster;
		this.initOwner(owner);
		rootNode = new GridPane();
		Scene meinScene = new Scene(rootNode, 300, 300);
		this.setScene(meinScene);
		this.initModality(Modality.WINDOW_MODAL);
		this.initStyle(StageStyle.UTILITY);
		this.dateiName = "score.dat";
		
		anzal = 10;
		bestehenliste = new Listenelement[anzal];
		for(int i = 0; i < anzal; i++)
			bestehenliste[i] = new Listenelement(0, "Nobody");
		
		File dateiTest = new File(dateiName);
		if(dateiTest.exists())
			datenLesen();
		
		loeschePunkte();
	}
	
	public int verraenderePunkte(int punkte) {
		this.punkte = this.punkte + punkte;
		return this.punkte;
	}
	
	public void loeschePunkte() {
		punkte = 0;
	}
	
	public boolean neuerEintrag() {
		String tempName;
		if(punkte > bestehenliste[anzal-1].getListePunkte()) {
			TextInputDialog meinDialog = new TextInputDialog();
			meinDialog.setTitle("Gewonnen");
			meinDialog.setHeaderText("Herzlich Gluckwunsch");
			meinDialog.setContentText("Bitte geben Sie Ihren Namen ein: ");
			meinDialog.initOwner(owner);
			Optional<String>dialogEingabe = meinDialog.showAndWait();
			if(dialogEingabe.isPresent())
				tempName = dialogEingabe.get();
			else 
				tempName = "Max Musterman";
			bestehenliste[anzal-1].setzeEintrag(punkte, tempName);
			Arrays.sort(bestehenliste);
			datenSchreiben();
			return true;
		}
		else {
			return false;
		}
	}
	
	public void listeZeigen() {
		setTitle("Beste Liste");
		rootNode.add(new Label("Punkte"), 0, 0);
		rootNode.add(new Label("Name"), 1, 0);
		for(int index = 0; index < anzal; index++) {
			rootNode.add(new Label(Integer.toString(bestehenliste[index].getListePunkte())), 0, index + 1);
			rootNode.add(new Label(bestehenliste[index].getListeName()), 1, index + 1);
		}
		showAndWait();
				
	}
	
	public void datenSchreiben() {
		try(RandomAccessFile datei = new RandomAccessFile(dateiName, "rw")) {
			for(int index = 0; index < anzal; index++) {
				datei.writeInt(bestehenliste[index].getListePunkte());
				datei.writeUTF(bestehenliste[index].getListeName());
			}
		}
		catch(IOException e) {
			System.out.println("Beim Scgreiben der Bestenliste ist ein Problem aufgetreten");
		}
	}
	
	public void datenLesen() {
		String tempName;
		int tempPunkte;
		try(RandomAccessFile datei = new RandomAccessFile(dateiName, "r")) {
			for(int index = 0; index < anzal; index++) {
				tempPunkte = datei.readInt();
				tempName = datei.readUTF();
				bestehenliste[index].setzeEintrag(tempPunkte, tempName);
			}
		}
		catch(IOException e) {
			System.out.println("Beim Laden der Besteliste ist ein Problem aufgetreten");
		}
	}
	
	class Listenelement implements Comparable<Listenelement> {
		private int listePunkte;
		private String listeName;
		
		public Listenelement(int listepunkte, String listename) {
			setzeEintrag(listepunkte, listename);
		}
		@Override
		public int compareTo(Listenelement tempEitrag) {
			if(this.listePunkte < tempEitrag.listePunkte)
				return 1;
			if(this.listePunkte > tempEitrag.listePunkte)
				return -1;
			else
				return 0;
		}
		
		public void setzeEintrag(int listePunkte, String listeName) {
			this.listeName = listeName;
			this.listePunkte = listePunkte;
		}
		
		public int getListePunkte() {
			return listePunkte;
		}
		
		public String getListeName() {
			return listeName;
		}
			
		
		
		
	}
	

}
