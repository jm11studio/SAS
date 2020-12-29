package ui.event;

import businesslogic.CatERing;
import businesslogic.event.Event;
import businesslogic.summarySheet.SummarySheet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import ui.Main;

import java.io.IOException;

public class EventManagement {

    @FXML
    Label userLabel;
    @FXML
    BorderPane containerPane;
    @FXML
    BorderPane menuListPane;
    @FXML
    EventContent menuListPaneController;

    BorderPane menuContentPane;
    EventContent menuContentPaneController;

    Main mainPaneController;

    public void initialize () {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("event-content.fxml"));

        try { menuContentPane = loader.load(); }
        catch (IOException ex) { ex.printStackTrace(); }

        menuContentPaneController = loader.getController();
        if (CatERing.getInstance().getUserManager().getCurrentUser() != null) {
            String uname = CatERing.getInstance().getUserManager().getCurrentUser().getUserName();
            userLabel.setText( uname );
        }

    }

    public void showCurrentMenu () {
        menuContentPaneController.initialize();
        containerPane.setCenter(menuContentPane);
    }

    public void showMenuList (Event m) {
        menuListPaneController.initialize();
        containerPane.setCenter(menuListPane);
    }

    public void endMenuManagement () {
        mainPaneController.showStartPane();
    }

    public void setMainPaneController(Main main) {
        mainPaneController = main;
    }

}