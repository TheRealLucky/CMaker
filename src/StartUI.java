package i15091.project.cmaker;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.collections.*;

import javafx.util.Duration;

import java.util.*;
import java.io.*;

public class StartUI extends Scene {

    StartUI(HBox content, Stage stage, Maker maker) {
        super(content, 900, 500);
        this.maker = maker;
        this.theme = Setting.getTheme();
        this.stage = stage;

        //If properties file does not exist
        if(theme == null){
            Setting.setTheme(Theme.BLUE);
            theme = Setting.getTheme();
        }
        CSVHandler.removeRecents("../resources/data/Recents.csv");

        getStylesheets().addAll("File:../resources/cmaker.css", theme.getPath());

        BorderPane main_pane = new BorderPane();
        main_pane.setPadding(new Insets(10, 0, 0, 0));
        main_pane.setCenter(createGridPane());
        main_pane.setTop(createHeader(theme.getLogoPath()));
        main_pane.getStyleClass().add("layout");
        main_pane.setPrefSize(650, 500);

        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        content.getChildren().addAll(createSideBar(), main_pane);

        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                if(CMKeys.EXIT_COMBINATION.match(key)) {
                    System.exit(0);
                }
                if(CMKeys.OPEN_COMBINATION.match(key)) {
                    btn_open_project.fire();
                }
                if(CMKeys.CREATE_COMBINATION.match(key)) {
                    btn_create_project.fire();
                }
                if(CMKeys.SETTINGS_COMBINATION.match(key)) {
                    btn_conf.fire();
                }
            }
        });
        this.addEventFilter(KeyEvent.KEY_PRESSED, CMKeys.controlButtons(btn_create_project, btn_open_project));

        this.stage.setScene(this);
    }

    //Create sidebar where recent projects are displayed
    private VBox createSideBar() {
        VBox side_bar = FXHelper.createVBox(Pos.TOP_CENTER, new Insets(5), new Double(10));
        side_bar.setPrefSize(250, 500);
        side_bar.getStyleClass().add("side_bar");

        Label recents_title = FXHelper.createLabel("Recent Projects", null, "text", "recents_title");

        ListView<VBox> project_list = new ListView<VBox>();
        project_list.getStyleClass().add("list_view");
        project_list.setPrefWidth(240);
        project_list.setFocusTraversable(false);
        ObservableList<VBox> items = FXCollections.observableArrayList();

        LinkedList<String[]> projects = CSVHandler.read("../resources/data/Recents.csv");
        VBox project_box;
        for(String[] project : projects) {
            project_box = new VBox();
            project_box.setPrefWidth(100);
            Tooltip tip = new Tooltip(project[1]);
            Tooltip.install(project_box, tip);

            Label project_name = FXHelper.createLabel(project[0]);
            project_name.setId("project_name");

            Label project_path = FXHelper.createLabel(project[1]);
            project_path.setId("project_path");

            project_box.getChildren().addAll(project_name, project_path);
            items.add(project_box);
        }
        project_list.setItems(items);

        project_list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    VBox selected = project_list.getSelectionModel().getSelectedItem();
                    Label temp = (Label)selected.getChildren().get(1);
                    Maker open = new Maker();
                    try{
                        open.openProject(temp.getText());
                    }
                    catch(PathNotFoundException e){
                        e.printStackTrace();
                    }
                    ManagerUI manager = new ManagerUI(new HBox(), stage, open);
                }
            }
        });

        HBox sidebar_middle_box = FXHelper.createHBox(Pos.CENTER);
        Button btn_delete_element = FXHelper.createButton("Delete Project", Theme.getDeletePath(), "btn_main_menu");
        btn_delete_element.setId("btn_delete_element");
        btn_delete_element.setFocusTraversable(false);
        btn_delete_element.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                VBox selected = project_list.getSelectionModel().getSelectedItem();
                int selectedIndex = project_list.getSelectionModel().getSelectedIndex();
                project_list.getItems().remove(selectedIndex);
                Label project_name_label = (Label)selected.getChildren().get(0);
                Label project_path_label = (Label)selected.getChildren().get(1);
                String project_path = project_path_label.getText();
                String project_name = project_name_label.getText();

                String path = project_path.substring(0, project_path.length()-project_name.length());
                Maker.deleteProject(path, project_name);
                CSVHandler.removeRecents("../resources/data/Recents.csv");

            }
        });
        sidebar_middle_box.getChildren().add(btn_delete_element);
        
        HBox sidebar_bottom_box = FXHelper.createHBox(Pos.CENTER_RIGHT, new Insets(5, 0, 0, 0));
        sidebar_bottom_box.setPrefHeight(60);
        sidebar_bottom_box.setPrefWidth(230);
        HBox settings_box = FXHelper.createHBox(Pos.CENTER_RIGHT);
        sidebar_bottom_box.setId("sidebar_bottom_box");
        HBox colors_box = FXHelper.createHBox(Pos.CENTER, new Insets(0, 20, 0, 0), null, null, null, new Double(20));

        Button blue = FXHelper.createButton(null, null, "color_button");
        blue.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                changeTheme(Theme.BLUE);
            }
        });
        blue.setId("color_blue");
        blue.setFocusTraversable(false);

        Button orange = FXHelper.createButton(null, null, "color_button");
        orange.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                changeTheme(Theme.ORANGE);
            }
        });
        orange.setId("color_orange");
        orange.setFocusTraversable(false);

        Button green = FXHelper.createButton(null, null, "color_button");
        green.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                changeTheme(Theme.GREEN);
            }
        });
        green.setId("color_green");
        green.setFocusTraversable(false);

        colors_box.getChildren().addAll(blue, orange, green);

        Button btn_settings = FXHelper.createButton("", Setting.getTheme().getSettingsPath(), "btn_main_menu");
        btn_settings.setFocusTraversable(false);

        RotateTransition rotateTransition = 
            new RotateTransition(Duration.millis(500), btn_settings);
        rotateTransition.setByAngle(180f);
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(true);

        TranslateTransition openNav=new TranslateTransition(new Duration(350), settings_box);
        openNav.setToX(settings_box.getWidth()-30);
        TranslateTransition closeNav=new TranslateTransition(new Duration(350), settings_box);

        btn_settings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                rotateTransition.play();
                if(settings_box.getTranslateX() == -225){
                    openNav.play();
                }
                else{
                    closeNav.setToX(-225);
                    closeNav.play();

                }
            }
        });
        btn_settings.setId("btn_settings");

        btn_conf = FXHelper.createButton("Settings", "btn_main_menu");
        btn_conf.setId("btn_license");
        btn_conf.setFocusTraversable(false);

        btn_conf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                //LicenseUI license = new LicenseUI(new VBox(), stage, maker);
                SettingsUI startui = new SettingsUI(new VBox(), stage, maker);
            }
        });
        settings_box.getChildren().addAll(btn_conf, colors_box, btn_settings);
        
        sidebar_bottom_box.getChildren().add(settings_box);
        side_bar.getChildren().addAll(recents_title, project_list, sidebar_middle_box, sidebar_bottom_box);
        closeNav.setToX(-225);
        closeNav.play();

        return side_bar;
    }


    //Create Grid Pane for the open / create buttons
    private GridPane createGridPane() {
        btn_create_project = FXHelper.createButton("Create", theme.getCreatePath(), "btn_main_menu");
        btn_create_project.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                if(Setting.getHomePath() == null){
                    DirectoryChooser dir_chooser = new DirectoryChooser();
                    dir_chooser.setTitle("New project path");
                    File dir = dir_chooser.showDialog(stage);
                    if(dir != null){
                        String path = dir.getAbsolutePath();
                        CreateUI create_ui = new CreateUI(new BorderPane(), stage, maker, path);
                    }
                }
                else {
                    String path = Setting.getHomePath();
                    CreateUI create_ui = new CreateUI(new BorderPane(), stage, maker, path);
                }
            }
        });

        btn_open_project = FXHelper.createButton("Open", theme.getOpenPath(), "btn_main_menu");
        btn_open_project.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                DirectoryChooser dir_chooser = new DirectoryChooser();
                dir_chooser.setTitle("Open project path");
                File dir = dir_chooser.showDialog(stage);
                if(dir != null){
                    String path = dir.getAbsolutePath();
                    Maker open = new Maker();
                    try{
                        open.openProject(path);
                    }
                    catch(PathNotFoundException e){
                        e.printStackTrace();
                    }
                    ManagerUI manager = new ManagerUI(new HBox(), stage, open);
                }
            }
        });

        GridPane main_grid = new GridPane();
        main_grid.setPadding(new Insets(16, 5, 16, 5));
        main_grid.setAlignment(Pos.BOTTOM_CENTER);

        main_grid.setConstraints(btn_create_project, 1, 0);
        main_grid.setConstraints(btn_open_project, 0, 0);

        GridPane.setHalignment(btn_create_project, HPos.RIGHT);
        GridPane.setHalignment(btn_open_project, HPos.LEFT);
        
        ColumnConstraints col1_constr = new ColumnConstraints();
        ColumnConstraints col2_constr = new ColumnConstraints();

        col1_constr.setHgrow(Priority.ALWAYS);
        col1_constr.setPercentWidth(50);
        
        col2_constr.setHgrow(Priority.ALWAYS);
        col2_constr.setPercentWidth(50);

        
        main_grid.getColumnConstraints().addAll(col1_constr, col2_constr);
        main_grid.getChildren().addAll(btn_create_project, btn_open_project);

        return main_grid;
    }


    //Create title row
    private VBox createHeader(String logo_path) {
        VBox header = FXHelper.createVBox(Pos.CENTER, null, new Double(10));

        HBox image_row = FXHelper.createHBox(Pos.CENTER, new Insets(4, 0, 0, 5), logo_path);
        HBox title_row = FXHelper.createHBox(Pos.CENTER);
        HBox subtitle_row = FXHelper.createHBox(Pos.CENTER, new Insets(150, 0, 0, 0));
        HBox text_row = FXHelper.createHBox(Pos.CENTER, new Insets(0, 0, 15, 0));

        Label title = FXHelper.createLabel("CM", null, "title");
        Label title_small = FXHelper.createLabel("AKER", new Insets(10, 0, 4, 0), "title_small");
        Label subtitle = FXHelper.createLabel("Manage your CMake projects", null, "subtitle");
        Label text = FXHelper.createLabel("Fast & Easy", null, "text_small");

        title_row.getChildren().addAll(title, title_small, image_row);
        subtitle_row.getChildren().add(subtitle);
        text_row.getChildren().add(text);

        header.getChildren().addAll(title_row, subtitle_row, text_row);
        return header;
    }

    public void changeTheme(Theme theme) {
        getStylesheets().remove(this.theme.getPath());
        Setting.setTheme(theme);
        this.theme = Setting.getTheme();
        getStylesheets().add(this.theme.getPath());
    }

    private Maker maker;
    private Theme theme;
    private Stage stage;

    //JavaFx elements used in several methods
    private Button btn_open_project;
    private Button btn_create_project;
    private Button btn_conf;
}