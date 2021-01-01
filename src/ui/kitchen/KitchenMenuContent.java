package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchen.Kitchen;
import businesslogic.kitchen.KitchenException;
import businesslogic.kitchen.KitchenManager;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.recipe.Recipe;
import businesslogic.summarySheet.SummarySheet;
import businesslogic.task.Task;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import persistence.PersistenceManager;
import ui.general.EventsInfoDialog;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Optional;

public class KitchenMenuContent {
    public Label description_text;
    public Label Menu_text;
    public VBox DefinitionTaskBox;
    public Label IDLabel;
    public TextField NameTextField;
    public TextField RecipeTextField;
    public TextField QuantityTextField;
    public TextField TimeTextField;
    public TextField CompletedTextField;
    public TextField CoookTextField;
    public Button SaveTaskChanges;

    @FXML
    Label titleLabel;

    @FXML
    ListView<Task> sectionList;

    @FXML
    Label itemsTitle;

    @FXML
    ToggleButton freeItemsToggle;

    @FXML
    ListView<Task> itemsList;

    @FXML
    Button deleteSectionButton;
    @FXML
    Button editSectionButton;
    @FXML
    Button upSectionButton;
    @FXML
    Button downSectionButton;
    @FXML
    Button upItemButton;
    @FXML
    Button downItemButton;
    @FXML
    Button spostaItemButton;
    @FXML
    Button modificaItemButton;
    @FXML
    Button deleteItem;

    @FXML
    Button addItemButton;

    @FXML
    Pane itemsPane;
    @FXML
    GridPane centralPane;
    Pane emptyPane;
    boolean paneVisible = true;

    KitchenMenuManagement menuManagementController;

    public void initialize() {
        SummarySheet sh = KitchenManager.getCurrentSummarySheet();
        sh.setID();

        if (sh != null && sh.setID() != -1) {
            titleLabel.setText( "Titolo: " + sh.getTitle() );

            description_text.setText( "Description: " + ( sh.getDescription() == null ? "Da definire." : sh.getDescription() ) );
            Menu_text.setText( "Menu: " + ( sh.getM() == null ? "Da definire." : sh.getM().getTitle() ) );

        }
        KitchenManager.setCurrentSummarySheet(sh);
        refreshSectionList();

        emptyPane = new BorderPane();
        paneVisible = false;

    }

    @FXML
    public void exitButtonPressed() {
        menuManagementController.showMenuList( CatERing.getInstance().getKitchenManager().getCurrentSummarySheet() );
    }

    @FXML
    public void publishButtonPressed() {
        SummarySheet sh = CatERing.getInstance().getKitchenManager().getCurrentSummarySheet();

        try { CatERing.getInstance().getKitchenManager().publish();
        } catch (KitchenException ex) { ex.printStackTrace(); }

        menuManagementController.showMenuList( sh );
    }

    @FXML
    public void addSectionPressed() {
        TextInputDialog dial = new TextInputDialog("AddTask");
        dial.setTitle("Aggiungi un nuovo task");
        dial.setHeaderText("Scegli il nome per il tuo nuovo task:");
        Optional<String> result = dial.showAndWait();

        if (result.isPresent()) {
            try {
                SummarySheet sh = KitchenManager.getCurrentSummarySheet();
                CatERing.getInstance().getKitchenManager().defineTask(result.get());
            } catch (UseCaseLogicException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setMenuManagementController(KitchenMenuManagement menuManagement) {
        menuManagementController = menuManagement;
    }

    @FXML
    public void eventInfoButtonPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../general/events-info-dialog.fxml"));
        try {
            BorderPane pane = loader.load();
            EventsInfoDialog controller = loader.getController();

            Stage stage = new Stage();

            controller.setOwnStage(stage);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Eventi presenti nel sistema");
            stage.setScene(new Scene(pane, 600, 400));


            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void editTitleButtonPressed() {

        TextInputDialog dial = new TextInputDialog( KitchenManager.getCurrentSummarySheet().getTitle() );
        dial.setTitle("Modifica il titolo");
        dial.setHeaderText("Scegli un nuovo titolo");
        Optional<String> result = dial.showAndWait();

        if (result.isPresent()) {
            try {
                KitchenManager.getCurrentSummarySheet().setTile(result.get());
                this.titleLabel.setText(result.get());
            } catch (KitchenException e ) { e.printStackTrace(); }
        }
    }

    @FXML
    public void deleteSectionButtonPressed() {
        Task sec = sectionList.getSelectionModel().getSelectedItem();
        if (sec != null ) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancellazione di un Task");
            alert.setHeaderText("Sei sicuro di voler eliminare questo task ?");
            alert.setContentText("Se sceglierai di cancellarlo NON potrai MAI più recuperarlo.");

            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            boolean deleteItems = false;
            if (result.get() == buttonTypeOne) { deleteItems = true; }

            try { CatERing.getInstance().getKitchenManager().deleteSection(sec);
            } catch (KitchenException ex) { ex.printStackTrace(); }

            refreshSectionList();

        }
    }

    private void refreshSectionList() {
        SummarySheet sh = KitchenManager.getCurrentSummarySheet();


        sectionList.setItems( sh.getTasks(sh.getID()) );

        sectionList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        sectionList.getSelectionModel().select(null);
        sectionList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {

                DefinitionTaskBox.setPrefWidth(154);
                DefinitionTaskBox.setVisible(true);


                if (newValue != null && newValue != oldValue) {

                    // TODO: Fare in modo che sia cancellabile e tutto il resto

                    IDLabel.setText( "ID: " + newValue.getId() );
                    if ( newValue.getName().isEmpty() ) NameTextField.setPromptText("null");
                    else NameTextField.setPromptText(newValue.getName());

                    RecipeTextField.setPromptText( String.valueOf( newValue.recipe.getId() ) );
                    QuantityTextField.setPromptText( String.valueOf(newValue.quantity) );
                    TimeTextField.setPromptText( String.valueOf(newValue.time));
                    CompletedTextField.setPromptText( String.valueOf(newValue.completed) );
                    CoookTextField.setPromptText( String.valueOf( newValue.cook.getId() ) );


                    NameTextField.clear();
                    RecipeTextField.clear();
                    QuantityTextField.clear();
                    TimeTextField.clear();
                    CompletedTextField.clear();
                    CoookTextField.clear();


                    // delete
                    deleteSectionButton.setDisable(false);
                } else if (newValue == null) {

                    deleteSectionButton.setDisable(true);
                    editSectionButton.setDisable(true);
                    upSectionButton.setDisable(true);
                    downSectionButton.setDisable(true);
                }

            }
        });

    }

    public void SaveTaskChanges(ActionEvent actionEvent) {
        // controllo errori e apertura box dialogo.

        String name = NameTextField.getPromptText();
        String recipe = RecipeTextField.getPromptText();
        String quantity = QuantityTextField.getPromptText();
        String time = TimeTextField.getPromptText();
        String completed = CompletedTextField.getPromptText();
        String cook = CoookTextField.getPromptText();


        if (!NameTextField.getText().isEmpty()) name = NameTextField.getText();
        if (!RecipeTextField.getText().isEmpty()) recipe = RecipeTextField.getText();
        if (!QuantityTextField.getText().isEmpty()) quantity = QuantityTextField.getText();
        if (!TimeTextField.getText().isEmpty()) time = TimeTextField.getText();
        if (!CompletedTextField.getText().isEmpty() && (CompletedTextField.getText() == "false" || CompletedTextField.getText() == "true"))
            completed = CompletedTextField.getText();

        if (!CoookTextField.getText().isEmpty()) cook = CoookTextField.getText();


        try {
            for (int i = 0; i < quantity.length(); i++)
                if (Character.isDigit(quantity.charAt(i)) == false) throw new KitchenException();

            for (int i = 0; i < time.length(); i++)
                if (Character.isDigit(time.charAt(i)) == false) throw new KitchenException();

            if ( CompletedTextField.getText() == "false" || CompletedTextField.getText() == "true")
                throw new KitchenException();


            String query = "UPDATE task SET name=\"" + name + "\"" +
                    ", recipe= " + recipe +
                    ", quantity= " + quantity +
                    ", time= " + time +
                    ", completed= " + completed +
                    ", cook= " + cook +
                    " WHERE id= " + IDLabel.getText().substring(4);

            PersistenceManager.executeUpdate(query);

        } catch (KitchenException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);// line 1
            alert.setTitle("ERROR reporting Box");// line 2
            alert.setHeaderText("Un campo della definizione del task è errata e/o inaccettabile.");// line 3
            alert.setContentText("Controlla il tipo di dati e la virgola.\nPoi tenta di nuovo.");// line 4

            alert.showAndWait(); // line 5
        }


    }

    public void editDescriptionButton(ActionEvent actionEvent) {
        TextInputDialog dial = new TextInputDialog(  );
        dial.setTitle("Seleziona un altro menù:");
        dial.setHeaderText("Scegli un nuovo menù  inserenda il suo id:");
        Optional<String> result = dial.showAndWait();

        if (result.isPresent()) {
            try {
                KitchenManager.getCurrentSummarySheet().setDescription(result.get());
                this.description_text.setText(result.get());
            } catch (KitchenException e ) { e.printStackTrace(); }
        }
    }

    public void choseMenuButton(ActionEvent actionEvent)  {
        TextInputDialog dial = new TextInputDialog( KitchenManager.getCurrentSummarySheet().getTitle() );
        dial.setTitle("Modifica il titolo");
        dial.setHeaderText("Scegli un nuovo titolo:");
        Optional<String> result = dial.showAndWait();



        if (result.isPresent()) {

            try {
                KitchenManager.getCurrentSummarySheet().setMenu( result.get() );
                this.Menu_text.setText("Menu: "  + Menu.loadMenuID( Integer.parseInt( result.get() ) ).getTitle() );
            } catch (KitchenException e) { e.printStackTrace(); }
        }
    }

}