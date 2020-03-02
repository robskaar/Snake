package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private static Font winnerFont;

    @Override
    public void start(Stage primaryStage) throws Exception{
        winnerFont = Font.loadFont(getClass().getResourceAsStream("/Resources/ka1.ttf"),100);
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/main.fxml"));
        primaryStage.setTitle("Snake");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Font getWinnerFont(){
        return winnerFont;
    }

}