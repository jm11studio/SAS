package ui.event;

import businesslogic.CatERing;
import businesslogic.event.Event;
import businesslogic.event.EventThrowException;
import businesslogic.service.Service;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class EventContent {
    public VBox DefinitionTaskBox;
    public Label IDLabel;
    public Button SaveTaskChanges;
    public TextField PublicatedTextField;
    public TextField TERTextField;
    public TextField DateTextField;
    public TextField End_HourTextField;
    public TextField Start_HourTextField;
    public TextField DescriptionTextField;
    public TextField NameTextField;
    public Button deleteButton;
    public MenuButton SHMenuButton;
    public MenuButton ServiceMenuButton;


    HashMap<String, Integer> ServicesList = new HashMap<>();
    HashMap<String, Integer> SHList = new HashMap<>();

    private final String toSelectString = "To select: ";


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



        setMenuLists();




    }


    private void setMenuLists() {

        SHList.clear();
        ServicesList.clear();
        SHList.put(toSelectString, -1);

        SHMenuButton.setText(toSelectString);

        String query1 = "SELECT * FROM summarysheet WHERE 1;";
        PersistenceManager.executeQueryS(query1, rs -> {
            MenuItem menuItem = new MenuItem( rs.getString("title") );
            SHMenuButton.getItems().add(menuItem);

            menuItem.setOnAction(event -> SHMenuButton.setText(menuItem.getText()));
            SHList.put( menuItem.getText(), rs.getInt("ID") );

            return null;
        });



        ServiceMenuButton.setText(toSelectString);

        String query2 = "SELECT * FROM services WHERE 1;";
        PersistenceManager.executeQueryS(query2, rs -> {
            MenuItem menuItem = new MenuItem( rs.getString("name") );
            ServiceMenuButton.getItems().add(menuItem);

            menuItem.setOnAction(event -> ServiceMenuButton.setText(menuItem.getText()));
            ServicesList.put( menuItem.getText(), rs.getInt("id"));

            return null;
        });



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

            if (newValue != null && newValue != oldValue) {
                IDLabel.setText( "ID: " + newValue.getID() );
                if ( newValue.getName().isEmpty() ) NameTextField.setPromptText("null");
                else NameTextField.setPromptText(newValue.getName());


                // set test into "DefinitionTaskBox"
                IDLabel.setText( String.valueOf( newValue.getID() ) );
                try { ServiceMenuButton.setText( newValue.getService().getName() ); }
                catch (Exception e) { ServiceMenuButton.setText( toSelectString ); }
                PublicatedTextField.setPromptText( String.valueOf( newValue.isPublicated() ) );
                TERTextField.setPromptText( String.valueOf( newValue.getTimeEventRepetition() ) );
                try { SHMenuButton.setText( newValue.getSh().getTitle() ); }
                catch (Exception e) { SHMenuButton.setText( toSelectString ); }
                DateTextField.setPromptText( String.valueOf( newValue.getDate() ) );
                Start_HourTextField.setPromptText( String.valueOf( newValue.getTime_start() ) );
                End_HourTextField.setPromptText( String.valueOf( newValue.getTime_end() ) );
                DescriptionTextField.setPromptText( String.valueOf( newValue.getDescription() ) );
                NameTextField.setPromptText( String.valueOf( newValue.getName() ) );


                // Clear Text field into "DefinitionTaskBox"
                PublicatedTextField.clear();
                TERTextField.clear();
                Start_HourTextField.clear();
                End_HourTextField.clear();
                DateTextField.clear();
                DescriptionTextField.clear();
                NameTextField.clear();
                if (ServiceMenuButton.getText() == null) ServiceMenuButton.setText(toSelectString);
                if (SHMenuButton.getText() == null) SHMenuButton.setText(toSelectString);

                deleteButton.setDisable(false);

            } else if (newValue == null) { deleteButton.setDisable(true); }

        });
    }


    public void SaveTaskChanges() {
        String name = NameTextField.getPromptText();
        String id = IDLabel.getText();
        String service = "";
        String publicated = PublicatedTextField.getPromptText();
        String TERT = TERTextField.getPromptText();
        String SH = "";
        String dt = DateTextField.getPromptText();
        String hS = Start_HourTextField.getPromptText();
        String hE = End_HourTextField.getPromptText();
        String description = DescriptionTextField.getPromptText();

        if (!NameTextField.getText().isEmpty()) name = NameTextField.getText();
        if (!ServiceMenuButton.getText().equals(toSelectString)) service = ServiceMenuButton.getText();
        if (!TERTextField.getText().isEmpty()) TERT = TERTextField.getText();
        if (!PublicatedTextField.getText().isEmpty() &&
                (PublicatedTextField.getText().toLowerCase(Locale.ROOT).equals("false")
                        || PublicatedTextField.getText().toLowerCase(Locale.ROOT).equals("true")))
            publicated = PublicatedTextField.getText();
        if (!Start_HourTextField.getText().isEmpty()) hS = Start_HourTextField.getText();
        if (!End_HourTextField.getText().isEmpty()) hE = End_HourTextField.getText();
        if (!SHMenuButton.getText().equals(toSelectString)) SH = SHMenuButton.getText();
        if (!DateTextField.getText().isEmpty()) dt = DateTextField.getText();
        if (!DescriptionTextField.getText().isEmpty()) description = DescriptionTextField.getText();

        try {
            int serviceID = -1;
            int SHID = -1;

            try {
                // control input is integer
                Integer.parseInt(String.valueOf(id));
                if (!TERT.equals("")) Integer.parseInt(TERT);
                if (!service.equals("")) serviceID = ServicesList.get(service);
                if (!SH.equals("")) SHID = SHList.get(SH);
                if (!hS.equals("")) Integer.parseInt(hS);
                if (!hE.equals("")) Integer.parseInt(hE);
            } catch ( Exception e ) { throw new EventThrowException(); }


            String query = "UPDATE event SET name=\"" + name + "\"" +
                    ( serviceID != -1 ? ", service= " + service : "" ) +
                    ", publicated= " + publicated +
                    (!TERT.equals("") ? ", timeEventRepetition= " + TERT : "" ) +
                    ", publicated = " + publicated +
                    ( SHID != -1 ? ", summarySheet= " + SH : "" ) +
                    (!hS.equals("") ? ", time_start= \"" + hS : "\"" ) +
                    (!hE.equals("") ? ", time_end= \"" + hE : "\"" ) +
                    ", date= \"" + dt + "\"" +
                    (!description.equals("") ? ", description= \"" + description + "\"" : "" ) +
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