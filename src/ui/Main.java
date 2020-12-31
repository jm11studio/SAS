package ui;

import businesslogic.CatERing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import ui.event.EventContent;
import ui.event.EventManagement;
import ui.menu.MenuManagement;
import ui.kitchen.KitchenMenuManagement;

import java.io.IOException;

public class Main {

    @FXML
    AnchorPane paneContainer;
    @FXML
    FlowPane startPane;
    @FXML
    Start startPaneController;

    BorderPane menuManagementPane;
    BorderPane KitchenMenuSummarySheetPane;
    BorderPane EventPane;

    MenuManagement menuManagementPaneController;
    KitchenMenuManagement KitchenMenuManagementPaneController;
    EventContent EventManagementPaneController;



    public void initialize() {
        startPaneController.setParent(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu/menu-management.fxml"));
        FXMLLoader loaderK = new FXMLLoader(getClass().getResource("kitchen/kitchen-management.fxml"));
        FXMLLoader loaderE = new FXMLLoader(getClass().getResource("event/event-content.fxml"));
        try {
            menuManagementPane = loader.load();
            menuManagementPaneController = loader.getController();
            menuManagementPaneController.setMainPaneController(this);

            KitchenMenuSummarySheetPane = loaderK.load();
            KitchenMenuManagementPaneController = loaderK.getController();
            KitchenMenuManagementPaneController.setMainPaneController(this);

            EventPane = loaderE.load();
            EventManagementPaneController = loaderE.getController();
            EventManagementPaneController.setMainPaneController(this);

        } catch (IOException ex) { ex.printStackTrace(); }
    }



    public void startMenuManagement() {
        CatERing.getInstance().getUserManager().fakeLogin("Lidia");

        menuManagementPaneController.initialize();
        paneContainer.getChildren().remove(startPane);
        paneContainer.getChildren().add(menuManagementPane);
        AnchorPane.setTopAnchor(menuManagementPane, 0.0);
        AnchorPane.setBottomAnchor(menuManagementPane, 0.0);
        AnchorPane.setLeftAnchor(menuManagementPane, 0.0);
        AnchorPane.setRightAnchor(menuManagementPane, 0.0);
    }

    public void showStartPane() {
        startPaneController.initialize();
        paneContainer.getChildren().remove(menuManagementPane);
        paneContainer.getChildren().remove(KitchenMenuSummarySheetPane);
        paneContainer.getChildren().remove(EventPane);
        paneContainer.getChildren().add(startPane);
    }

    public void startKitchenManagement() {
        CatERing.getInstance().getUserManager().fakeLogin("Lidia");

        KitchenMenuManagementPaneController.initialize();
        paneContainer.getChildren().remove(startPane);
        paneContainer.getChildren().add(KitchenMenuSummarySheetPane);
        AnchorPane.setTopAnchor(KitchenMenuSummarySheetPane, 0.0);
        AnchorPane.setBottomAnchor(KitchenMenuSummarySheetPane, 0.0);
        AnchorPane.setLeftAnchor(KitchenMenuSummarySheetPane, 0.0);
        AnchorPane.setRightAnchor(KitchenMenuSummarySheetPane, 0.0);
    }

    public void startEventManagement() {
        CatERing.getInstance().getUserManager().fakeLogin("Lidia");

//        EventManagementPaneController.initialize();
        paneContainer.getChildren().remove(startPane);
        paneContainer.getChildren().add(EventPane);
        AnchorPane.setTopAnchor(EventPane, 0.0);
        AnchorPane.setBottomAnchor(EventPane, 0.0);
        AnchorPane.setLeftAnchor(EventPane, 0.0);
        AnchorPane.setRightAnchor(EventPane, 0.0);
    }

}