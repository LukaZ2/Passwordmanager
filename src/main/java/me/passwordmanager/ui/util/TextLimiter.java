package me.passwordmanager.ui.util;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * Used to limit textfields in JavaFx
 */
public class TextLimiter {

    public static TextFormatter<?> limit(int limit) {
        return new TextFormatter<>(change -> {
            if(!change.isContentChange()) return change;
            int newLength = change.getControlNewText().length();
            if(newLength <= limit) return change;
            change.setText(change.getControlNewText().substring(0, limit));
            return change;
        });
    }

}
