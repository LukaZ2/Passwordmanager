package me.passwordmanager.ui.util;

import javafx.stage.Modality;
import javafx.stage.Stage;

public class FxGuiHandler {

//    public void openGui(FxGuiController gui) {
//        openGui(MainApplication.getInstance().getStage(), gui);
//    }
//    public void openGui(Stage stage, FxGuiController gui) {
//        var loader = new FXMLLoader(MainApplication.class.getResource(gui.xmlFileName()));
//        loader.setControllerFactory(type -> gui);
//        try {
//            stage.setScene(new Scene(loader.load()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        gui.setStage(stage);
//    }
//
//    public void openSubGui(FxGuiController gui) {
//        openSubGui(gui, false);
//    }
//    public void openSubGui(FxGuiController gui, boolean modal) {
//        openGui(modal ? MainApplication.getInstance().createModalSubstage() : MainApplication.getInstance().createSubstage(), gui);
//    }

    public static Stage createSubstage(Stage parent) {
        var substage = new Stage();
        substage.initOwner(parent);
        return substage;
    }

    public static Stage createModalSubstage(Stage parent) {
        var substage = createSubstage(parent);
        substage.initModality(Modality.WINDOW_MODAL);
        return substage;
    }
}
