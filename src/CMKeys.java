package i15091.project.cmaker;

import javafx.event.EventHandler;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class CMKeys {

    //Killer feature im backend (siagt kana owa is sau geil)
    public static EventHandler<KeyEvent> controlButtons(Button... buttons) {
        EventHandler<KeyEvent> handler =  new EventHandler<KeyEvent>() { 
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    for(Button btn : buttons) {
                        if(btn.isFocused()) {
                            btn.fire();
                        }
                    }
                }
            }
        };
        return handler;
    }

    
    public static EventHandler<KeyEvent> controlBoxes(CheckBox... boxes) {
        EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    for(CheckBox box : boxes) {
                        if(box.isFocused()){
                            if(box.isSelected()) {
                                box.setSelected(false);
                            }
                            else {
                                box.setSelected(true);
                            }
                        }
                    }
                }
            }
        };
        return handler;
    }

    //Startscreen combinations
    public static final KeyCombination EXIT_COMBINATION = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination OPEN_COMBINATION = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination CREATE_COMBINATION = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination SETTINGS_COMBINATION = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    
    //Manager screen combinatinos
    public static final KeyCombination MAKE_COMBINATION = new KeyCodeCombination(KeyCode.M, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination BUILD_COMBINATION = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination EXTERNAL_COMBINATION = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination PREVIEW_COMBINATION = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination ADD_COMBINATION = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination TEST_COMBINATION = new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN);
}