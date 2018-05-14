package i15091.project.cmaker;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
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

import i15091.project.cmaker.FXHelper;
import i15091.project.cmaker.Maker;
import i15091.project.cmaker.Setting;
import i15091.project.cmaker.Theme;
import javafx.util.Duration;

import java.io.*;


public class SettingsUI extends Scene {
    
    SettingsUI(VBox content, Stage stage, Maker maker){
        super(content, 900, 500);
        this.getStylesheets().addAll("File:../resources/cmaker.css", Setting.getTheme().getPath());
        this.maker = maker;

        content.getStyleClass().add("layout");
        content.getChildren().addAll(createTitle(), createContent(), createBottomRow());

        this.stage = stage;
        this.addEventFilter(KeyEvent.KEY_PRESSED, CMKeys.controlButtons(btn_back, btn_change));
        this.stage.setScene(this);
    }

    public HBox createTitle() {
        HBox title_row = new HBox();
        title_row.setPadding(new Insets(5, 0, 0, 0));
        title_row.setAlignment(Pos.CENTER);
        Label title = new Label("Settings");
        title.getStyleClass().add("title_small");
        title_row.getChildren().add(title);
        return title_row;
    }

    public TabPane createContent() {
        TabPane pane = new TabPane();
        pane.getStyleClass().add("tab_pane");
        VBox text_box = new VBox();
        text_box.setPadding(new Insets(10, 0, 0, 0));
        text_box.setAlignment(Pos.CENTER);
        Text text = new Text(CSVHandler.readFile("../resources/data/license.txt"));
        text.getStyleClass().add("license_text");
        text.setTextAlignment(TextAlignment.CENTER);
        text_box.getChildren().add(text);
        VBox version_box = new VBox();
        version_box.setAlignment(Pos.CENTER);
        Text version = new Text("Developed by Patrick Krukenfellner\nVersion 1.0.1");
        version.setTextAlignment(TextAlignment.CENTER);
        version.setId("version_text");
        version_box.getChildren().add(version);

        Tab tab_lic = new Tab();
        tab_lic.getStyleClass().add("tab");
        tab_lic.setText("License");
        tab_lic.setContent(text_box);
        tab_lic.setClosable(false);

        Tab tab_conf = new Tab();
        tab_conf.getStyleClass().add("tab");
        tab_conf.setText("Settings");
        tab_conf.setContent(createInputRow());
        tab_conf.setClosable(false);

        Tab tab_version = new Tab();
        tab_version.getStyleClass().add("tab");
        tab_version.setText("Version");
        tab_version.setContent(version_box);
        tab_version.setClosable(false);

        pane.getTabs().addAll(tab_conf, tab_lic, tab_version);
        return pane;
    }

    public VBox createInputRow() {
        VBox content_row = new VBox();
        content_row.setAlignment(Pos.CENTER);
        HBox path_row = new HBox(30);
        path_row.setPadding(new Insets(40, 0, 20, 0));
        path_row.setAlignment(Pos.CENTER);
        Label path_text = new Label();
        if(Setting.getHomePath() != null){
            path_text.setText(Setting.getHomePath());
        } 
        else {
            path_text.setText("Not set");
        }
        path_text.getStyleClass().add("path_text");
        Label home_text = new Label("HOME PATH");
        home_text.getStyleClass().add("path_text");
        Text info_text = new Text("This is your home path.\nProjects will be created here, if it is set.");
        info_text.setTextAlignment(TextAlignment.CENTER);
        info_text.getStyleClass().add("text");
        btn_change = FXHelper.createButton("Change Path", "btn_main_menu");
        btn_change.getStyleClass().add("btn_manager");
        btn_change.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent event) {
                DirectoryChooser dir_chooser = new DirectoryChooser();
                dir_chooser.setTitle("Set default directory");
                File dir = dir_chooser.showDialog(stage);
                if(dir != null){
                    String path = dir.getAbsolutePath();
                    //Set default path
                    Setting.setHome(path);;
                    path_text.setText(Setting.getHomePath());
                }
            }
        });
        path_row.getChildren().addAll(home_text, path_text);
        content_row.getChildren().addAll(info_text, path_row, btn_change);
        return content_row;
    }

    public HBox createBottomRow() {
        HBox bottom_row = new HBox();
        bottom_row.setPadding(new Insets(5, 0, 0, 0));
        bottom_row.setId("bottom_row");
        bottom_row.setAlignment(Pos.CENTER);
        btn_back = FXHelper.createButton("back", Theme.getArrowPath(), "btn_main_menu");
        btn_back.setId("btn_back");
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                StartUI start = new StartUI(new HBox(), stage, maker);
            }
        });
        bottom_row.getChildren().add(btn_back);
        return bottom_row;
    }

    Stage stage;
    Maker maker;

    Button btn_back;
    Button btn_change;
}