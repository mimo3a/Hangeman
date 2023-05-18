package hangeman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HangmanFX extends Application{
	
	@Override
	public void start(Stage meinStage) throws Exception {
		
		FXMLLoader meinLoader = new FXMLLoader(getClass().getResource("sb_hangman.fxml"));
		Parent root = meinLoader.load();
		FXMLController meinController = meinLoader.getController();
		meinController.setStage(meinStage);
		
		
//		Parent root = FXMLLoader.load(getClass().getResource("sb_hangman.fxml"));
		Scene meinScene = new Scene(root);
		meinStage.setTitle("Hangman");
		meinStage.setScene(meinScene);
		meinStage.show();
		
		
	}
	
	public static void main(String[] args) {
		launch(args);

	}

}
