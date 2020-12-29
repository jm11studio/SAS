package ui.event;

import businesslogic.CatERing;
import businesslogic.event.Event;
import businesslogic.event.EventThrowException;
import businesslogic.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.w3c.dom.events.EventException;
import persistence.PersistenceManager;
import ui.Main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;

public class EventContent {
    public VBox DefinitionTaskBox;
    public Label IDLabel;
    public Button SaveTaskChanges;
    public TextField ServiceIDTextField;
    public TextField PublicatedTextField;
    public TextField TERTextField;
    public TextField SHTextField;
    public TextField DateTextField;
    public TextField DescriptionTextField;
    public TextField NameTextField;
    public Button deleteButton;


    @FXML
    ListView<Event> sectionList = new ListView<>();

    Pane emptyPane;
    boolean paneVisible = true;
    User currentUser;
    SimpleDateFormat dataFrmt = new SimpleDateFormat("yyyy-MM-dd");

    Main EventManagementController;

    public void initialize() {
        currentUser = CatERing.getInstance().getUserManager().getCurrentUser();

        // richiamo a persistance. ottengo tutti gli eventi. Li metto in selectionList. In fine refresh con "refreshSelectionList"
        sectionList.setItems(Event.getEventList());
        refreshSectionList();

        emptyPane = new BorderPane();
        paneVisible = false;
    }


    @FXML
    public void exitButtonPressed() { EventManagementController.showStartPane(); }
    public void setMainPaneController(Main main) {
        EventManagementController = main;
    }


    @FXML
    public void copyEvent() {
        Event ev = sectionList.getSelectionModel().getSelectedItem();
        if (ev != null ) { CatERing.getInstance().getEventManager().copyEvent(ev, currentUser); }

        refreshSectionList();
    }


    @FXML
    public void newEvent() {

        TextInputDialog dial = new TextInputDialog("AddTask");
        dial.setTitle("Crea un nuovo evento");
        dial.setHeaderText("Scegli il nome per il tuo nuovo evento:");
        Optional<String> result = dial.showAndWait();

        if ( result.isPresent() ) {
            TextInputDialog dial2 = new TextInputDialog("yyyy-MM-dd");
            dial2.setTitle("Crea un nuovo evento");
            dial2.setHeaderText("Scegli il nome per il tuo nuovo evento:");
            Optional<String> result2 = dial2.showAndWait();


            try {
                if (result2.isPresent()) {

                    Event ev = new Event(result.get(), currentUser);
                    ev.setDate( dataFrmt.parse( result2.get()) );

                    CatERing.getInstance().getEventManager().createEvent(ev, currentUser, result2.get());
                    refreshSectionList();
                }
            } catch (ParseException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERROR reporting Box");
                alert.setHeaderText("Un campo della definizione dell'evento è errata e/o inaccettabile.");// line 3
                alert.setContentText("Controlla il tipo di dati e la virgola.\nPoi tenta di nuovo.");// line 4

                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR reporting Box");
            alert.setHeaderText("Inserire un nome valido.");
            alert.setContentText("Inserire un nome valido per l'evento.\nPoi tenta di nuovo.");// line 4

            alert.showAndWait();
        }

    }


    @FXML
    public void deleteEventButtonPressed() {
        Event sec = sectionList.getSelectionModel().getSelectedItem();
        if (sec != null ) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancellazione Event");
            alert.setHeaderText("Sei sicuro di voler eliminare questo evento ?");
            alert.setContentText("Se sceglierai di cancellarlo NON potrai MAI più recuperarlo.");

            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();


            if (result.get().getText().equals("Yes")) {
                try { CatERing.getInstance().getEventManager().deleteEvent(sec, null); }
                catch (EventException ex) { ex.printStackTrace(); }
            }

            refreshSectionList();
        }
    }


    private void refreshSectionList() {

        sectionList.setItems( Event.getEventList() );

        sectionList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        sectionList.getSelectionModel().select(null);
        sectionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            DefinitionTaskBox.setPrefWidth(154);
            DefinitionTaskBox.setVisible(true);

            System.out.println("newValue: " + newValue);

            if (newValue != null && newValue != oldValue) {

                IDLabel.setText( "ID: " + newValue.getID() );
                if ( newValue.getName().isEmpty() ) NameTextField.setPromptText("null");
                else NameTextField.setPromptText(newValue.getName());


                IDLabel.setText( String.valueOf( newValue.getID() ) );
                try { ServiceIDTextField.setPromptText( String.valueOf( newValue.getService().getID() ) ); } catch (Exception e ) {}
                PublicatedTextField.setPromptText( String.valueOf( newValue.isPublicated() ) );
                TERTextField.setPromptText( String.valueOf( newValue.getTimeEventRepetition() ) );
                try { SHTextField.setPromptText( String.valueOf( newValue.getSh().getID() ) );  } catch (Exception e ) {}
                DateTextField.setPromptText( String.valueOf( newValue.getDate() ) );
                DescriptionTextField.setPromptText( String.valueOf( newValue.getDescription() ) );
                NameTextField.setPromptText( String.valueOf( newValue.getName() ) );


                ServiceIDTextField.clear();
                PublicatedTextField.clear();
                TERTextField.clear();
                SHTextField.clear();
                DateTextField.clear();
                DescriptionTextField.clear();
                NameTextField.clear();

                deleteButton.setDisable(false);
            } else if (newValue == null) {

                deleteButton.setDisable(true);
            }

        });
    }


    public void SaveTaskChanges() {
        String name = NameTextField.getPromptText();
        String id = IDLabel.getText();
        String service = ServiceIDTextField.getPromptText();
        String publicated = PublicatedTextField.getPromptText();
        String TERT = TERTextField.getPromptText();
        String SH = SHTextField.getPromptText();
        String dt = DateTextField.getPromptText();
        String description = DescriptionTextField.getPromptText();

        if (!NameTextField.getText().isEmpty()) name = NameTextField.getText();
        if (!ServiceIDTextField.getText().isEmpty()) service = ServiceIDTextField.getText();
        if (!TERTextField.getText().isEmpty()) TERT = TERTextField.getText();
        if (!PublicatedTextField.getText().isEmpty() &&
                (PublicatedTextField.getText().toLowerCase(Locale.ROOT).equals("false")
                        || PublicatedTextField.getText().toLowerCase(Locale.ROOT).equals("true")))
            publicated = PublicatedTextField.getText();

        if (!SHTextField.getText().isEmpty()) SH = SHTextField.getText();
        if (!DateTextField.getText().isEmpty()) dt = DateTextField.getText();
        if (!DescriptionTextField.getText().isEmpty()) description = DescriptionTextField.getText();

        try {
            try {
                // control input is integer
                Integer.parseInt(String.valueOf(id));
                if (SH != "") Integer.parseInt(String.valueOf(TERT));
                if (SH != "") Integer.parseInt(String.valueOf(SH));
                if (SH != "") Integer.parseInt(String.valueOf(service));
            } catch ( Exception e ) { throw new EventThrowException(); }


            String query = "UPDATE event SET name=\"" + name + "\"" +
                    ( service != "" ? ", service= " + service : "" ) +
                    ", publicated= " + publicated +
                    ( TERT != "" ? ", timeEventRepetition= " + TERT : "" ) +
                    ", publicated = " + publicated +
                    ( SH != "" ? ", summarySheet= " + SH : "" ) +
                    ", date= \"" + dt + "\"" +
                    ( description != "" ? ", description= \"" + description + "\"" : "" ) +
                    " WHERE id= " + id;

            PersistenceManager.executeUpdate(query);

            refreshSectionList();

        } catch (EventThrowException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);// line 1
            alert.setTitle("ERROR reporting Box");// line 2
            alert.setHeaderText("Un capo della definizione dell'evento è errata.");
            alert.setContentText("Controlla il tipo di dati e la virgola. Poi tenta di nuovo.");

            alert.showAndWait();
        }
    }


}