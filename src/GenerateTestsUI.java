package i15091.project.cmaker;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

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

import java.io.*;

public class GenerateTestsUI extends Scene {

    GenerateTestsUI(VBox content, Stage stage, Maker maker){
        super(content, 600, 500);
        this.getStylesheets().addAll("File:../resources/cmaker.css", Setting.getTheme().getPath());
        this.maker = maker;

        content.setSpacing(20);
        content.setPadding(new Insets(5));
        content.getChildren().addAll(createTitle(), createFileView(), createButtonRow());
        content.getStyleClass().add("layout");

        this.stage = stage;
        this.addEventFilter(KeyEvent.KEY_PRESSED, CMKeys.controlButtons(btn_back, btn_create));
        this.stage.setScene(this);
    }


    private HBox createTitle() {
        HBox title_row = new HBox();
        title_row.setAlignment(Pos.CENTER);
        Label title = new Label();
        title.setText("Test generator");
        title.getStyleClass().add("title_small");
        title.setId("title_tests");
        title_row.getChildren().add(title);
        return title_row;
    }


    private HBox createFileView() {
        HBox file_view_row = new HBox(20);
        file_view_row.setPadding(new Insets(0, 0, 0, 10));
        file_view_row.setAlignment(Pos.CENTER_LEFT);
        ListView<String> file_list = new ListView<String>();
        file_list.setFocusTraversable(false);
        file_list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        file_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selected_files.add(newValue);
            }
        });
        file_list.getStyleClass().add("list_view");
        file_list.setId("tests_file_list");
        ObservableList<String> files = FXCollections.observableArrayList(maker.getFiles("src"));
        file_list.setItems(files);

        Label info_text = new Label("Select the files for which tests \nshould be generated.");
        file_view_row.getChildren().addAll(file_list, info_text);
        return file_view_row;
    }


    private HBox createButtonRow() {
        HBox button_row = new HBox(350);
        button_row.setAlignment(Pos.CENTER);
        button_row.setPadding(new Insets(5, 0, 5, 0));
        button_row.setId("bottom_row");
        btn_back = FXHelper.createButton("back", Setting.getTheme().getArrowPath(), "btn_main_menu");
        btn_back.setId("btn_generate");
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                ManagerUI manager = new ManagerUI(new HBox(), stage, maker);
            }
        });
        btn_back.getStyleClass().add("btn_main_menu");
        btn_create = FXHelper.createButton("create", Setting.getTheme().getDonePath(), "btn_main_menu");
        btn_create.setId("btn_generate");
        btn_create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                maker.getProject().generateTests(selected_files);
                ManagerUI manager = new ManagerUI(new HBox(), stage, maker);
            }
        });
        btn_create.getStyleClass().add("btn_main_menu");
        button_row.getChildren().addAll(btn_back, btn_create);
        return button_row; 
    }

    Maker maker;
    Stage stage;
    LinkedList<String> selected_files = new LinkedList<String>();

    //JavaFX elements used in several methods
    Button btn_back;
    Button btn_create;
}