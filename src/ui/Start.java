package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Start {

    private Main mainPaneController;

    @FXML
    void beginMenuManagement () { mainPaneController.startMenuManagement(); }
    @FXML
    public void beginSummarySheet () { mainPaneController.startKitchenManagement(); }

    public void beginEventManagement(ActionEvent actionEvent) { mainPaneController.startEventManagement(); }

    public void setParent(Main main) {
        this.mainPaneController = main;
    }

    public void initialize() { }
}