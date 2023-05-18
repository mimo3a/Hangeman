package hangeman;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WorteEditor extends Stage {
	private Stage owner;
	private GridPane rootNode;
	private Scene meinScene;
//	die schaltefläche für addieren eines neues Wort
	private Button addWort;
//	der Name der Detei
	private String dateiName;
//	instance der  Fileklasse
	private File meinFeile;
//	wie viele Wörter gibt es schon im Wörterbuch
	private Label wortezahLabel;

	public WorteEditor(Stage fenster) {
		super();
		owner = fenster;
		initOwner(owner);

		rootNode = new GridPane();

//		eine Schaltefläche zu addieren Wörte
		addWort = new Button("Add neu Wort");
		addWort.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				addNeuWort();
			}
		});

		rootNode.add(addWort, 1, 1);
//		eine Laben zu zeigen wie viel woerter gibt es in der Datei
		wortezahLabel = new Label(" Es gibt schon  " + 0 + " Worter");
		rootNode.add(wortezahLabel, 2, 1);
		meinScene = new Scene(rootNode, 300, 400);
		setScene(meinScene);
		initModality(Modality.WINDOW_MODAL);
		initStyle(StageStyle.UTILITY);
		
		dateiName = "wortListe.dat";
		meinFeile = new File(dateiName);
	}

// Methode zu zeigen den Editor
	public void editorZeigen() {
		setTitle("Wörter Liste");
		listLesen();
		showAndWait();
	}

// Methode für addieren ein neues Wort durch einen Dialog
	public void addNeuWort() {
		int count = 0;
		String neuWort = neuWortDialog();

		if (neuWort != null) {
//			prufen wir ob die Datei schon existiert
//			wenn ja dann setzen wir der zeichner in position vor 4 bites bis Ende der Datei
//			und lesen menge von Wörten count(INT)
//			wenn die Datei noch nicht existiert dann count = 0. das Wörterbuch is leer
			if(meinFeile.exists()) {
//			nehmen wir alter count aus datei
			try(RandomAccessFile datei = new RandomAccessFile(dateiName, "r")) {
							
				datei.seek(datei.length()-4);
				count = datei.readInt();
							
				datei.close();
			}
			catch(IOException e) {
				System.out.println("Beim Lesen count ist ein Problem auftreten");
			}
			} else {
				count = 0;
			}
//	neues Wort speichern wir in der Ende der Datei gerade vor dem count (-4 bites)	
			try (RandomAccessFile datei = new RandomAccessFile(dateiName, "rw")) {
				
//	wenn die detei is nicht leer schreiben in position vor dem count (-4)
				if(datei.length() > 4) {
				datei.seek(datei.length()-4);
				datei.writeUTF(neuWort);
//	und dann speichern neue wert von count
				datei.writeInt(count +1);
				datei.close();
//	wenn die detei leer ist, hat noch keine Wörten schreiben im anfang		
				} else {
					datei.writeUTF(neuWort);
					datei.writeInt(count +1);
					datei.close();
				}
			} catch (IOException e) {
				System.out.println("Beim schreiben ist ein Problem aufgetreten");
			}

			listLesen();
		}

	}

	private void listLesen() {
		File file = new File(dateiName);
		
//		wenn die Datei existiert und die ihr Lange mehr als 4 bites 
//		setzen wir zeichner in Position 4 lleiner als Dateilange und lesen count
		if(file.exists()) {
		try (RandomAccessFile datei = new RandomAccessFile(dateiName, "r")) {
			
//	lesen die werte von count			
			datei.seek(datei.length()-4);
			int count = datei.readInt();	
//	setzen der Wert der Wörtemenge in Label 
			wortezahLabel.setText(" Es gibt schon  " + count + " Worter");
//	dann gehen am anfang zurueck
			datei.seek(0);
//	und durch die schleife kreieren wir neue Label mit den Worten
			for(int i = 0; i < count; i++) {
				rootNode.add(new Label(datei.readUTF()), 1, i + 3);
				}

			datei.close();
			

		} catch (IOException e) {
			System.out.println("Beim lesen in Methode ListLesen ist ein Problem aufgetreten");
		}
		}
	}

	private String neuWortDialog() {
		String meinWort;
		TextInputDialog meinDialog = new TextInputDialog();
		meinDialog.setTitle("Neues Wort");
		meinDialog.setHeaderText("Neues Wort");
		meinDialog.setContentText("Geben Sie bitte ein noues Wort");
		meinDialog.initOwner(owner);
		Optional<String> dialogEingabe = meinDialog.showAndWait();
		if (dialogEingabe.isPresent()) {
			
			meinWort = dialogEingabe.get();

		} else
			meinWort = null;
		return meinWort;
	}
	

}
