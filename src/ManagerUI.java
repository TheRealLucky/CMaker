package i15091.project.cmaker;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
import javafx.util.Duration;

import java.io.*;


public class ManagerUI extends Scene {
    
    ManagerUI(HBox content, Stage stage, Maker maker){
        super(content, 900, 500);
        this.getStylesheets().addAll("File:../resources/cmaker.css", Setting.getTheme().getPath());
        this.maker = maker;

        content.getChildren().addAll(createManagerPane(), createFileBox());

        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent key) {
                if(CMKeys.MAKE_COMBINATION.match(key)) {
                    btn_build.fire();
                }
                if(CMKeys.BUILD_COMBINATION.match(key)) {
                    btn_cmake.fire();
                }
                if(CMKeys.PREVIEW_COMBINATION.match(key)) {
                    btn_preview.fire();
                }
                if(CMKeys.EXTERNAL_COMBINATION.match(key)) {
                    btn_external.fire();
                }
                if(CMKeys.TEST_COMBINATION.match(key)) {
                    btn_add_test.fire();
                }
                if(CMKeys.ADD_COMBINATION.match(key)) {
                    btn_add_src.fire();
                }
            }
        });

        this.stage = stage;
        this.addEventFilter(KeyEvent.KEY_PRESSED, CMKeys.controlButtons(btn_back, btn_build, btn_cmake, btn_preview, btn_external));
        this.stage.setScene(this);
    }


    private BorderPane createManagerPane() {
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10));

        VBox content = createContent();

        bp.getStyleClass().add("layout");
        bp.setId("manager_pane");
        bp.setPrefWidth(650);
        bp.setMinWidth(650);
        bp.setTop(createTitle("Project Manager"));
        bp.setCenter(content);
        bp.setBottom(createBottomLine());
        BorderPane.setAlignment(content, Pos.TOP_CENTER);

        return bp;
    }

    private HBox createBottomLine() {
        HBox bottom_row = new HBox(200);
        bottom_row.setAlignment(Pos.CENTER_LEFT);
        btn_back = FXHelper.createButton("back", Setting.getTheme().getArrowPath(), "btn_main_menu");
        btn_back.setId("btn_manager_back");
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                StartUI start_ui = new StartUI(new HBox(), stage, new Maker());
            }
        });
        VBox info_row = new VBox();
        Label project_text = new Label(maker.getProject().getName());
        Label path_text = new Label(maker.getProject().getPath());
        info_row.getChildren().addAll(project_text, path_text);


        bottom_row.getChildren().addAll(btn_back, info_row);
        return bottom_row;
    }


    private VBox createContent() {
        VBox content = new VBox();

        HBox console_row = new HBox();
        console_row.setPadding( new Insets(10) );
        ScrollPane console_pane = new ScrollPane();
        console_pane.getStyleClass().add("scroll_pane");
        console_pane.setHbarPolicy(ScrollBarPolicy.NEVER);
        console_pane.setVbarPolicy(ScrollBarPolicy.NEVER);
        console_pane.setPrefWidth(600);
        console_pane.setPrefHeight(200);
        Label console_area = new Label();
        console_area.setAlignment(Pos.TOP_LEFT);
        console_area.setPrefWidth(680);
        console_area.setWrapText(true);
        console_pane.setContent(console_area);
        console_row.getChildren().add(console_pane);

        console_area.heightProperty().addListener(new ChangeListener<Number>() {
            @Override 
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                console_pane.setVvalue(1);
            }
        }); 
        

        //HBox button_row = new HBox(20);
        //button_row.setAlignment(Pos.CENTER);

        GridPane buttons = new GridPane();
        buttons.setAlignment(Pos.CENTER);
        buttons.setVgap(20);
        buttons.setHgap(30);

        btn_build = new Button("Make");
        btn_build.getStyleClass().addAll("btn_main_menu", "btn_manager");
        btn_build.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                int exec = CMConsole.execute(new String(maker.getPath() + "/build"), "make");
                if(exec == 0){
                    console_text.append("Make successful!" + newline);
                    console_area.setText(console_text.toString());
                }
                else{
                    console_text.append("Make failed!" + newline);
                    console_text.append(CMConsole.error_msg + newline);
                    console_area.setText(console_text.toString());
                }
            }
        });

        btn_cmake = new Button("Build");
        btn_cmake.getStyleClass().addAll("btn_main_menu", "btn_manager");
        btn_cmake.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                int exec = CMConsole.execute(new String(maker.getPath() + "/build"), "cmake ..");
                if(exec == 0){
                    console_text.append("Build successful!" + newline);
                    console_area.setText(console_text.toString());
                }
                else{
                    console_text.append("Build failed!" + newline);
                    console_text.append(CMConsole.error_msg + newline);
                    console_area.setText(console_text.toString());
                }
            }
        });

        
        btn_preview = new Button("CMakeLists");
        btn_preview.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                String command = new String("cat " + maker.getPath() + "/CMakeLists.txt");
                console_text.append(CMConsole.getOutput(command) + newline);
                console_area.setText(console_text.toString());
            }
        });
        btn_preview.getStyleClass().addAll("btn_main_menu", "btn_manager");
        btn_preview.setId("btn_generate_tests");

        btn_external = new Button("External");
        btn_external.getStyleClass().addAll("btn_main_menu", "btn_manager");
        btn_external.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                ExternalUI external = new ExternalUI(new BorderPane(), stage, maker);
            }
        });

        buttons.setConstraints(btn_build, 0, 0);
        buttons.setConstraints(btn_cmake, 1, 0);
        buttons.setConstraints(btn_preview, 2, 0);
        buttons.setConstraints(btn_external, 1, 1);

        buttons.getChildren().addAll(btn_build, btn_cmake, btn_preview, btn_external);

        content.getChildren().addAll(console_row, buttons/*button_row, middle_row*/);
        content.setAlignment(Pos.TOP_LEFT);
        return content;
    }


    private HBox createTitle(String text) {
        HBox title_box = new HBox();
        Label title = new Label(text);
        title.getStyleClass().add("title_small");
        title_box.getChildren().add(title);
        title_box.setAlignment(Pos.CENTER);
        return title_box;
    }

    private void setFiles(ScrollPane pane) {
        VBox content_src = new VBox();
        if(maker.getFiles("src") != null){
            for(String filename : maker.getFiles("src")){
                VBox file = new VBox();
                file.getStyleClass().add("file_box");
                file.getChildren().add(new Label(filename));
                content_src.getChildren().add(file);
            }
            pane.setContent(content_src);
        }
    }


    private VBox createFileBox() {
        file_show = new VBox();
        file_show.setPrefWidth(250);
        file_show.getStyleClass().add("file_show");

        HBox title = createTitle("Files");
        title.getStyleClass().add("show_file_title");


        VBox files_box = new VBox(10);
        files_box.setPadding(new Insets(10));

        BorderPane source_title = new BorderPane();
        source_title.setPadding(new Insets(10, 10, 0, 5));

        Label source_title_label = new Label("Source Files");
        source_title_label.getStyleClass().add("text_small");

        //Source files
        ScrollPane source_files = new ScrollPane();
        source_files.setPrefHeight(200);
        source_files.getStyleClass().add("scroll_pane");
        source_files.setHbarPolicy(ScrollBarPolicy.NEVER);
        source_files.setVbarPolicy(ScrollBarPolicy.NEVER);
        setFiles(source_files);

        btn_add_src = FXHelper.createButton("", Theme.getAddPath(), null);
        btn_add_src.setId("btn_add");
        btn_add_src.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                AddFileUI ui = new AddFileUI(new BorderPane(), stage, maker);
                setFiles(source_files);
            }
        });

        source_title.setLeft(source_title_label);
        source_title.setAlignment(btn_add_src, Pos.CENTER);
        source_title.setRight(btn_add_src);

        BorderPane tests_title = new BorderPane();
        tests_title.setPadding(new Insets(10, 10, 0, 5));

        Label tests_title_text = new Label("Test Files");
        tests_title_text.getStyleClass().add("text_small");

        btn_add_test = FXHelper.createButton("", Theme.getAddPath(), null);
        btn_add_test.setId("btn_add");
        btn_add_test.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                GenerateTestsUI tests = new GenerateTestsUI(new VBox(), stage, maker);
            }
        });

        tests_title.setLeft(tests_title_text);
        tests_title.setAlignment(btn_add_test, Pos.CENTER);
        tests_title.setRight(btn_add_test);

        //Test files
        ScrollPane test_files = new ScrollPane();
        test_files.setPrefHeight(200);
        test_files.getStyleClass().add("scroll_pane");
        test_files.setHbarPolicy(ScrollBarPolicy.NEVER);
        test_files.setVbarPolicy(ScrollBarPolicy.NEVER);
        VBox content_test = new VBox();
        if(maker.getFiles("tests") != null){
            if(maker.getProject().junit_enabled){
                for(String filename : maker.getFiles("tests")){
                    VBox file = new VBox();
                    file.getStyleClass().add("file_box");
                    file.getChildren().add(new Label(filename));
                    content_test.getChildren().add(file);
                }
            }
            test_files.setContent(content_test);
        }
        files_box.getChildren().addAll(source_title, source_files, tests_title, test_files);

        BorderPane bp = new BorderPane();

        bp.setPadding( new Insets(10) );
        bp.setTop(title);
        bp.setCenter(files_box);
        bp.getStyleClass().add("layout");

        file_show.getChildren().add(bp);
        return file_show;
    }

    Stage stage;
    Maker maker;
    VBox file_show;

    StringBuilder console_text = new StringBuilder();
    private final static String newline = System.getProperty("line.separator");

    //JavaFx elements used in several methods
    private Button btn_back;
    private Button btn_cmake;
    private Button btn_build;
    private Button btn_preview;
    private Button btn_external;
    private Button btn_add_src;
    private Button btn_add_test;

    private Boolean back = false;

}