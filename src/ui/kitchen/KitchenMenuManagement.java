package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.summarySheet.SummarySheet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import ui.Main;

import java.io.IOException;

public class KitchenMenuManagement {

    @FXML
    Label userLabel;
    @FXML
    BorderPane containerPane;
    @FXML
    BorderPane menuListPane;
    @FXML
    KitchenMenuList menuListPaneController;

    BorderPane menuContentPane;
    KitchenMenuContent menuContentPaneController;

    Main mainPaneController;


    public void initialize () {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("kitchen-content.fxml"));

        try { menuContentPane = loader.load(); }
        catch (IOException ex) { ex.printStackTrace(); }

        menuContentPaneController = loader.getController();
        menuContentPaneController.setMenuManagementController(this);

        if (CatERing.getInstance().getUserManager().getCurrentUser() != null) {
            String uname = CatERing.getInstance().getUserManager().getCurrentUser().getUserName();
            userLabel.setText( uname );
        }

        menuListPaneController.setParent(this);
    }

    public void showCurrentMenu () {
        menuContentPaneController.initialize();
        containerPane.setCenter(menuContentPane);
    }

    public void showMenuList (SummarySheet m) {
        menuListPaneController.initialize();
        menuListPaneController.selectSummarySheet(m);
        containerPane.setCenter(menuListPane);
    }

    public void endMenuManagement () {
        mainPaneController.showStartPane();
    }

    public void setMainPaneController(Main main) {
        mainPaneController = main;
    }
}
