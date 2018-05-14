package i15091.project.cmaker;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

import javafx.event.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.beans.value.*;
import javafx.collections.*;

import java.util.*;

import i15091.project.cmaker.FXHelper;
import i15091.project.cmaker.Maker;
import i15091.project.cmaker.Theme;

import java.io.*;

public class ExternalUI extends Scene {

    ExternalUI(BorderPane content, Stage stage, Maker maker){
        super(content, 500, 400);
        this.getStylesheets().addAll("File:../resources/cmaker.css", Setting.getTheme().getPath());
        this.maker = maker;
        this.stage = stage;

        content.setTop(createTitle());
        content.setCenter(createContent());
        content.setBottom(createBottomRow());
        content.getStyleClass().add("layout");

        this.addEventFilter(KeyEvent.KEY_PRESSED, CMKeys.controlButtons(btn_back));
        this.stage.setScene(this);
    }

    public HBox createTitle() {
        HBox title_box = new HBox();
        title_box.setAlignment(Pos.CENTER);
        Label title = new Label("Add External Jar");
        title.setId("add_title");
        title.getStyleClass().add("title_small");
        title_box.getChildren().add(title);
        return title_box;
    }

    public VBox createContent() {
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        Label target = new Label("Drop '.jar' files in this field to\nadd it to the project.\nPlease do not drop files with withespaces.");
        target.getStyleClass().add("drag_and_drop");
        target.setPrefSize(300, 300);
        target.setAlignment(Pos.CENTER);
        target.setTextAlignment(TextAlignment.CENTER);
        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });
        
        // Dropping over surface
        target.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    for (File file:db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        if(filePath.substring(filePath.length()-4, filePath.length()).equals(".jar")){
                            target.setId("text_black");
                            target.setText("Jar file added to project!");
                            maker.getProject().addJar(filePath);
                        }
                        else{
                            target.setId("text_red");
                            target.setText("Only files with the extension '.jar'\n will be accepted!");
                        }
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        content.getChildren().add(target);
        return content;
    }
 

    public HBox createBottomRow() {
        HBox bottom_row = new HBox();
        bottom_row.setAlignment(Pos.CENTER);
        bottom_row.setPadding(new Insets(0, 0, 5, 0));
        btn_back = FXHelper.createButton("back", Theme.getArrowPath(), "btn_main_menu");
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                ManagerUI manager = new ManagerUI(new HBox(), stage, maker);
            }
        });
        bottom_row.getChildren().addAll(btn_back);
        return bottom_row;
    }

    Maker maker;
    Stage stage;

    Button btn_back;
}