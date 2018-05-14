package i15091.project.cmaker;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Rectangle2D;


public class CMakerUI extends Application{

    public void start(Stage stage){
        //Set main stage properties
        stage.setTitle("CMaker");
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 3);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 3);
        
        //Create Start Screen
        StartUI start_ui = new StartUI(new HBox(), stage, new Maker());
        //ManagerUI manager = new ManagerUI(new HBox(), stage, new Maker());
        //GenerateTestsUI tests = new GenerateTestsUI(new VBox(), stage, new Maker());
    }
    
    
    public static void main(String[] args){
        launch(args);
    }

    private Maker maker;
}