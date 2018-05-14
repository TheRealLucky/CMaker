package i15091.project.cmaker;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

import javafx.event.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.beans.value.*;

import java.util.*;

import i15091.project.cmaker.CMKeys;
import i15091.project.cmaker.FXHelper;
import i15091.project.cmaker.Maker;
import i15091.project.cmaker.Theme;
import javafx.util.Duration;

import java.io.*;

public class AddFileUI extends Scene{

    AddFileUI(BorderPane content, Stage stage, Maker maker){
        super(content, 500, 300);
        this.getStylesheets().addAll("File:../resources/cmaker.css", Setting.getTheme().getPath());
        this.maker = maker;
        this.stage = stage;

        content.setTop(createTitle());
        content.setBottom(createBottomRow());
        VBox body = createBody();
        content.setAlignment(body, Pos.CENTER);
        content.setCenter(body);
        content.getStyleClass().add("layout");

        this.addEventFilter(KeyEvent.KEY_PRESSED, CMKeys.controlButtons(btn_back, btn_add));

        this.stage.setScene(this);
    }

    private VBox createBody() {
        VBox body = new VBox(30);
        body.setAlignment(Pos.CENTER);
        Label input_titel = new Label("Add a source file either with or \nwithout the '.java' extension");
        HBox input_row = new HBox(50);
        input_row.setAlignment(Pos.CENTER);
        input = new TextField();
        input.getStyleClass().add("input");
        Label input_text = new Label("FILENAME");
        input_row.getChildren().addAll(input_text, input);
        body.getChildren().addAll(input_titel, input_row);
        return body;
    }

    private HBox createTitle() {
        HBox title_box = new HBox();
        Label title = new Label("Add Java File");
        title.getStyleClass().add("title_small");
        title.setId("add_title");
        title_box.setAlignment(Pos.CENTER);
        title_box.getChildren().add(title);
        return title_box;
    }

    private HBox createBottomRow() {
        HBox bottom_row = new HBox(200);
        bottom_row.setAlignment(Pos.CENTER);
        btn_add = FXHelper.createButton("add", Theme.getAddCirclePath(), "btn_main_menu");
        btn_add.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                if(input.getText().equals("")) {
                    input.setId("input_red");
                    Tooltip tip = new Tooltip("Please type in a filename!");
                    Tooltip.install(input, tip);
                }
                else {
                    maker.addSourceFile(input.getText());
                    ManagerUI manager = new ManagerUI(new HBox(), stage, maker);
                }
            } 
        });
        btn_back = FXHelper.createButton("back", Theme.getArrowPath(), "btn_main_menu");
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                ManagerUI manager = new ManagerUI(new HBox(), stage, maker);
            }
        });
        bottom_row.getChildren().addAll(btn_back, btn_add);
        return bottom_row;
    }

    Stage stage;
    Maker maker;

    Button btn_back;
    Button btn_add;

    TextField input;
}