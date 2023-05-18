package hangeman;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FXMLController {
	
	@FXML 
	private ComboBox<String> auswahl;
	
	@FXML
	private Label ausgabeText;
	
	@FXML
	private Label anzVersuche;
	
	@FXML
	private Canvas zeichenflaeche;
	
	@FXML
	private Label punkAusgabe;
	
	
	
	private String [] zeichen = new String [26];
	private StringBuilder anzeige;
	private String suchtwort;
	private int restDurchlaufe;
	private int fehler;
	private GraphicsContext gc;
	private Score spielPunkte;
	private Stage meineStage;
	private String [] woerter;
	
	@FXML
	protected void beendenKlick(ActionEvent event) {
		Platform.exit();
	}
	
	@FXML
	protected void wordEditorKlick(ActionEvent event) {
		
		new WorteEditor(meineStage).editorZeigen();
	}
	
	// die Methode zur Auswahl aus dem Kombinationfeld
	@FXML
	protected void auswahlNeu(ActionEvent event) {
		pruefen(auswahl.getSelectionModel().getSelectedItem());
		
		
		// ist das Spiel zu ende oder nicht
		
		gewinnerOderNicht();
		
	}
	
	@FXML
	void initialize() {
		
		int tempIndex = 0;
		restDurchlaufe = 9;
		anzVersuche.setText(Integer.toString(restDurchlaufe));
		
		for(char temp = 'a'; temp <= 'z'; temp ++) {
			zeichen[tempIndex] = Character.toString(temp);
			tempIndex ++;
		}
		
		auswahl.getItems().addAll(zeichen);
		neuesWort();
		
		gc = zeichenflaeche.getGraphicsContext2D();
		
	}
	
//	die Methode ermittelt zufaellig ein Wort
	private void neuesWort() {
		
		int zufall = 0;
		
//	wenn es keine Detei mit Wörten gibt erstellen wir einen einfachen Array mit einem wort
//	um die Programm starten kann
		
		if(!new File("wortListe.dat").exists()) {
			woerter = new String [1];
			woerter[0] = "mama";
		
//	wenn die Datei schon exestiert lesen wir zuerst die menge der Wörten
		} else {
			try(RandomAccessFile datei = new RandomAccessFile("wortListe.dat", "r")) {
//	lesen den count, menge der worter
				datei.seek(datei.length()-4);
				int count = datei.readInt();
//	gehen wir zu Anfang zurück
				datei.seek(0);
//	erstellen einen Array mit vorgegebener Länge
				woerter = new String [count];
//	tragen die Wörte in Array ein
				for(int i = 0; i < count; i++) {
					woerter[i] = datei.readUTF();
					
				}
			}
			catch(IOException e) {
				System.out.println("Beim Lesen ist ein Problem aufgetretet");
			}
		}

		zufall = (int)(Math.random() * woerter.length);
		
		suchtwort = new String(woerter[zufall]);
		anzeige = new StringBuilder(suchtwort);
		
//		alle Zeichen in der Anzeige ersetzen durch * 
		for(int zeichen = 0; zeichen < suchtwort.length(); zeichen ++) {
			anzeige.setCharAt(zeichen, '*');
			ausgabeText.setText(anzeige.toString());		
		}
		
		
	}
	
//	die Methode zum Pruefen
	private void pruefen(String auswahlZeichen) {
		
		char zeichen;
		int treffer = 0;
		zeichen = auswahlZeichen.charAt(0);
		
//		gibt es das Zeichen auch im Suchtwort?
		treffer = suchtwort.toLowerCase().indexOf(zeichen);
		if(treffer < 0) {
			restDurchlaufe --;
			anzVersuche.setText(Integer.toString(restDurchlaufe));
			erhoeheFehler();
			
			punkAusgabe.setText(Integer.toString(spielPunkte.verraenderePunkte(-1)));
		}
		else {
			while(treffer >= 0) {
				anzeige.setCharAt(treffer, suchtwort.charAt(treffer));
				treffer ++;
				treffer = suchtwort.toLowerCase().indexOf(zeichen, treffer);
				
				punkAusgabe.setText(Integer.toString(spielPunkte.verraenderePunkte(5)));
			}
			ausgabeText.setText(anzeige.toString());
		}	
	}
	
	private void gewinnerOderNicht() {
		
		boolean ende = false;
		
		gc.setLineWidth(1);
		if(restDurchlaufe == 0) {
			gc.strokeText("Das gesuchte Wort war: " + suchtwort, 20, 100);
			ende = true;
			
		}
//		ist das Wort erraten worden?
		if(anzeige.toString().equals(suchtwort)) {
			spielPunkte.verraenderePunkte(restDurchlaufe * 10);
			gc.strokeText("Hurra! Sie haben gewonnen!", 20, 100);
			ende = true;
		}
		if(ende == true) {
			
			if(spielPunkte.neuerEintrag() == true)
				spielPunkte.listeZeigen();
			Platform.exit();
		}
	}
	
//	Fehler hochlaelen und Galgen zeichen
	private void erhoeheFehler() {
		
		fehler = fehler + 1;
		gc.setLineWidth(4);
		
		switch(fehler) {
		case 1 : gc.strokeLine(10, 10, 10, 200);
		break;
		case 2 : gc.strokeLine(10, 10, 100, 10);
		break;
		case 3 : gc.strokeLine(40, 10, 10, 40);
		break;
		case 4 : gc.strokeLine(100, 10, 100, 50);
		break;
		case 5 : gc.strokeLine(70, 50, 130, 50);
		break;
		case 6 : gc.strokeLine(130, 50, 130, 110);
		break;
		case 7 : gc.strokeLine(130, 110, 70, 110);
		break;
		case 8 : gc.strokeLine(70, 110, 70, 50);
		break;
		case 9 : gc.strokeLine(0, 200, 20, 200);
		break;
		}
	}
	
	public void setStage(Stage meineStage) {
		this.meineStage = meineStage;
		spielPunkte = new Score(this.meineStage);
	}
	
	
}
