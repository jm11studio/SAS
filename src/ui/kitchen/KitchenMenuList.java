package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.kitchen.KitchenManager;
import businesslogic.summarySheet.SummarySheet;
import businesslogic.user.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class KitchenMenuList {
    private KitchenMenuManagement KitchenManagementController;

    @FXML
    ListView<SummarySheet> menuList;
    @FXML
    Button eliminaButton;
    @FXML
    Button apriButton;
    @FXML
    Button copiaButton;

    ObservableList<SummarySheet> KitchenListItems;

    @FXML
    public void nuovoButtonPressed() {
        User u = CatERing.getInstance().getUserManager().getCurrentUser();

        TextInputDialog dial = new TextInputDialog("SummarySheet");
        dial.setTitle("Crea un nuovo summary sheet");
        dial.setHeaderText("Scegli il titolo per il nuovo summary sheet");
        Optional<String> result = dial.showAndWait();

        if (result.isPresent()) {
            try {
                SummarySheet sh = CatERing.getInstance().getKitchenManager().createSummarySheet( result.get(), u );
                KitchenListItems.add(sh);
                KitchenManagementController.showCurrentMenu();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    @FXML
    public void fineButtonPressed() { KitchenManagementController.endMenuManagement(); }

    public void setParent(KitchenMenuManagement menuManagement) {
        KitchenManagementController = menuManagement;
    }

    public void initialize() {
        if (KitchenListItems == null) {
            KitchenListItems = CatERing.getInstance().getKitchenManager().getAllSumamarySheet();

            menuList.setItems(KitchenListItems);
            menuList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            menuList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SummarySheet>() {
                @Override
                public void changed(ObservableValue<? extends SummarySheet> observable, SummarySheet oldValue, SummarySheet newValue) {
                    KitchenManager.setCurrentSummarySheet(newValue);

                    eliminaButton.setDisable( false );
                    apriButton.setDisable( false );
                    copiaButton.setDisable( false );
                }
            });
        } else { menuList.refresh(); }
    }

    @FXML
    public void eliminaButtonPressed() {
        User u = CatERing.getInstance().getUserManager().getCurrentUser();

        try {
            SummarySheet sh = menuList.getSelectionModel().getSelectedItem();
            CatERing.getInstance().getKitchenManager().deleteSummarySheet(sh, u);
            KitchenListItems.remove(sh);
        } catch (Exception ex ) { ex.printStackTrace(); }
    }

    @FXML
    void apriMenuButtonPressed() {
        User u = CatERing.getInstance().getUserManager().getCurrentUser();

        try {
            SummarySheet m = menuList.getSelectionModel().getSelectedItem();
            CatERing.getInstance().getKitchenManager().chooseSummarySheet(m, u);
            KitchenManagementController.showCurrentMenu();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    @FXML
    void copiaMenuButtonPressed() {
        User u = CatERing.getInstance().getUserManager().getCurrentUser();

        try {
            SummarySheet m = menuList.getSelectionModel().getSelectedItem();
            SummarySheet copia = CatERing.getInstance().getKitchenManager().copySummarySheet(m, u);

            KitchenListItems.add(copia);
            KitchenManagementController.showCurrentMenu();

        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void selectSummarySheet(SummarySheet m) {
        if (m != null) this.menuList.getSelectionModel().select(m);
        else this.menuList.getSelectionModel().select(-1);
    }

}