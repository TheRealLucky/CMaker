package i15091.project.cmaker;

import javafx.application.*;
import javafx.stage.*;
import javafx.beans.value.*;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.*;
import java.io.*;

public class CreateUI extends Scene {

    CreateUI(BorderPane bp, Stage stage, Maker maker, String path) {
        super(bp, 900, 500);
        this.maker = maker;
        maker.clearProject();
        this.path = path;
        this.theme = Setting.getTheme();
        this.stage = stage;
        getStylesheets().addAll("File:../resources/cmaker.css", theme.getPath());

        bp.getStyleClass().add("layout");
        bp.setPadding(new Insets(10, 5, 0, 5));
        bp.setTop(createTitle());
        bp.setCenter(createGridPane());
        bp.setBottom(createBottomLine());

        this.addEventFilter(KeyEvent.KEY_PRESSED, CMKeys.controlButtons(btn_back, btn_create));
        this.addEventFilter(KeyEvent.KEY_PRESSED, CMKeys.controlBoxes(entry_point_box, junit_enabled));
        this.stage.setScene(this);
    }


    private HBox createBottomLine() {
        HBox bottom_row = new HBox(450);
        bottom_row.setAlignment(Pos.CENTER);
        bottom_row.getStyleClass().add("bottom_row");
        bottom_row.setPrefHeight(50);

        //Create Buttons
        btn_back = new Button("Back");
        ImageView logo = new ImageView();
        logo.setImage(new Image(Setting.getTheme().getArrowPath()));
        logo.setFitHeight(16);
        logo.setFitWidth(16);
        btn_back.setGraphic(logo);
        btn_back.getStyleClass().add("btn_main_menu");
        btn_back.setPrefWidth(60);
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                StartUI start = new StartUI(new HBox(), stage, maker);
            }
        });

        btn_create = FXHelper.createButton("Create", Theme.getArrowRightPath(), "btn_main_menu");
        btn_create.setPrefWidth(60);
        btn_create.setContentDisplay(ContentDisplay.RIGHT);
        btn_create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                try{
                    if(input.getText().equals("")) {
                        input.setId("input_red");
                        Tooltip tip = new Tooltip("Please specify a project name!");
                        Tooltip.install(input, tip);
                    }
                    else{
                        maker.createProject(input.getText(), path, junit_enabled.isSelected(), entry_point_box.isSelected());
                        ManagerUI manager = new ManagerUI(new HBox(), stage, maker);
                    }
                }
                catch(ProjectExistsException e){
                    e.printStackTrace();
                }
                catch(CouldNotCreateProjectException e){
                    e.printStackTrace();
                }
                
            }
        });

        bottom_row.getChildren().addAll(btn_back, btn_create);
        return bottom_row;
    }


    private GridPane createGridPane() {
        Label name = new Label("NAME ");
        name.getStyleClass().add("text_small");
        VBox name_input_row = new VBox();
        input = new TextField();
        name_input_row.getChildren().add(input);
        input.getStyleClass().add("input");
        input.setId("input_black");

        Label entry_point = new Label("GENERATE TEMPLATE FILE ");
        entry_point.getStyleClass().add("text_small");
        Label unit_tests = new Label("ENABLE UNIT TESTS ");
        unit_tests.getStyleClass().add("text_small"); 
        Label path_info = new Label("PATH ");
        path_info.getStyleClass().add("text_small");

        Label project_path = new Label(path);
        project_path.getStyleClass().add("text");

        final ToggleGroup group = new ToggleGroup();

        //Create check boxes
        entry_point_box = new CheckBox();
        entry_point_box.getStyleClass().add("check_box");
        junit_enabled = new CheckBox();
        junit_enabled.getStyleClass().add("check_box");

        junit_enabled.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean old_val, Boolean new_val) {
                    if(new_val == true){
                        entry_point_box.setDisable(true);
                    }
                    else{
                        entry_point_box.setDisable(false);
                    }
                    entry_point_box.setSelected(true);
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0, 10, 10, 10));
        grid.setVgap(40);

        grid.setConstraints(name, 0, 1);
        grid.setConstraints(name_input_row, 1, 1);
        grid.setConstraints(entry_point, 0, 2);
        grid.setConstraints(entry_point_box, 1, 2);
        grid.setConstraints(unit_tests, 0, 3);
        grid.setConstraints(junit_enabled, 1, 3);
        grid.setConstraints(path_info, 0, 4);
        grid.setConstraints(project_path, 1, 4);

        ColumnConstraints col1_constr = new ColumnConstraints();
        ColumnConstraints col2_constr = new ColumnConstraints();

        col1_constr.setHgrow(Priority.ALWAYS);
        col1_constr.setHalignment(HPos.LEFT);
        col1_constr.setPercentWidth(50);
        
        col2_constr.setHgrow(Priority.ALWAYS);
        col2_constr.setHalignment(HPos.CENTER);
        col2_constr.setPercentWidth(50);
        
        grid.getColumnConstraints().addAll(col1_constr, col2_constr);
        grid.getChildren().addAll(name, entry_point, unit_tests, name_input_row,
                                      entry_point_box, junit_enabled, path_info, project_path);

        return grid;
    }

    private VBox createTitle() {
        VBox title_row = new VBox();
        title_row.setAlignment(Pos.CENTER);
        Label title = new Label("PROJECT PROPERTIES");
        title.getStyleClass().add("title_properties");
        title_row.getChildren().add(title);
        return title_row;
    }


    private Stage stage;
    private Maker maker;
    private Theme theme;
    private String path;

    //JavaFx elements used in several methods
    private CheckBox entry_point_box;
    private CheckBox junit_enabled;
    private TextField input;

    private Button btn_back;
    private Button btn_create;
}