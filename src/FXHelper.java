package i15091.project.cmaker;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ContentDisplay;

import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;

import javafx.event.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.beans.value.*;

import java.util.*;

import java.io.*;

public class FXHelper {

    //Button helper methods
    public static Button createButton(String text, String image_path, Integer image_size, String styleclass) {
        Button btn = new Button(text);
        if(styleclass != null) {
            btn.getStyleClass().add(styleclass);
        }
        if(image_path != null) {
            ImageView logo = new ImageView();
            logo.setImage(new Image(image_path));
            if(image_size != null) {
                logo.setFitWidth(image_size);
                logo.setFitHeight(image_size);
            }
            else{
                logo.setFitHeight(16);
                logo.setFitWidth(16);
            }
            btn.setGraphic(logo);
            return btn;
        }
        return btn;

    }

    public static Button createButton(String text, String image_path, String styleclass) {
        return createButton(text, image_path, null, styleclass);
    }

    public static Button createButton(String text) {
        return createButton(text, null, null, null);
    }

    public static Button createButton(String text, String styleclass) {
        return createButton(text, null, null, styleclass);
    }

    //HBox helper methods
    public static HBox createHBox(Pos alignment, Insets padding, String logo_path
                                               , Double max_width, Double max_height
                                               , Double spacing) {
        HBox box = new HBox();
        if(alignment != null) {
            box.setAlignment(alignment);
        }
        if(padding != null) {
            box.setPadding(padding);
        }

        if(max_width != null) {
            box.setMaxWidth(max_width);
        }

        if(max_height != null) {
            box.setMaxHeight(max_height);
        }

        if(spacing != null) {
            box.setSpacing(spacing);
        }

        if(logo_path != null) {
            ImageView logo = new ImageView();
            logo.setImage(new Image(logo_path));
            logo.setFitHeight(28);
            logo.setFitWidth(28);
            box.getChildren().add(logo);
        }

        return box;
    }

    public static HBox createHBox(Pos alignment, Insets padding, String logo_path) {
        return createHBox(alignment, padding, logo_path, null, null, null);
    }

    public static HBox createHBox(Pos alignment) {
        return createHBox(alignment, null, null);
    }

    public static HBox createHBox(Insets padding) {
        return createHBox(null, padding, null);
    }

    public static HBox createHBox(Pos alignment, Insets padding) {
        return createHBox(alignment, padding, null);
    }

    //VBox helper methods 
    public static VBox createVBox(Pos alignment, Insets padding, Double spacing) {
        VBox box = new VBox();
        if(alignment != null){
            box.setAlignment(alignment);
        }
        if(padding != null) {
            box.setPadding(padding);
        }
        if(spacing != null) {
            box.setSpacing(spacing);
        }
        return box;
    }

    public static VBox createVBox(Pos alignment, Insets padding) {
        return createVBox(alignment, padding, null);
    }

    public static VBox createVBox(Pos alignment) {
        return createVBox(alignment, null);
    }

    public static VBox createVBox(Insets padding) {
        return createVBox(null, padding);
    }

    //Label helper methods
    public static Label createLabel(String text, Insets padding, String styleclass, String id) {
        Label label = new Label();
        if(text != null) {
            label.setText(text);
        }

        if(padding != null) {
            label.setPadding(padding);
        }
        
        if(styleclass != null) {
            label.getStyleClass().add(styleclass);
        }

        if(id != null) {
            label.setId(id);
        }

        return label;
    }

    public static Label createLabel(String text) {
        return createLabel(text, null, null);
    }

    public static Label createLabel(String text, Insets padding, String styleclass) {
        return createLabel(text, padding, styleclass, null);
    }
}