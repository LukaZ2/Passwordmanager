package me.passwordmanager.ui.util;

import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The type Fx gui handler.
 */
public class FxGuiHandler {

    /**
     * Create substage from parent
     *
     * @param parent the parent
     * @return the substage
     */
    public static Stage createSubstage(Stage parent) {
        var substage = new Stage();
        substage.initOwner(parent);
        return substage;
    }

    /**
     * Create modal substage from parent
     *
     * @param parent the parent
     * @return the modal substage
     */
    public static Stage createModalSubstage(Stage parent) {
        var substage = createSubstage(parent);
        substage.initModality(Modality.WINDOW_MODAL);
        return substage;
    }
}
