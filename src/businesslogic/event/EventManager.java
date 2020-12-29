package businesslogic.event;

import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.events.EventException;
import persistence.PersistenceManager;
import persistence.ResultHandlerEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class EventManager {

    Event currentEvent;

    public ObservableList<EventInfo> getEventInfo() {
        return EventInfo.loadAllEventInfo();
    }
    private static Map<String, Event> loadedEvent = FXCollections.observableHashMap();


    public void initialize() {}


    public ObservableList<Event> getAllEvent() {
        ArrayList<Event> allEvent = new ArrayList<>();
        String query = "SELECT * FROM `event` WHERE 1";

        PersistenceManager.executeQueryE(query, new ResultHandlerEvent() {
            @Override
            public Event handle(ResultSet rs) throws SQLException {

                Event event = new Event(rs.getString("name"), User.loadUserById( rs.getInt("user") ) );

                if ( !loadedEvent.containsKey( event.getName() ) ) {

                    try { event.setDescription(rs.getString("description")); }
                    catch (EventException e ) { e.printStackTrace(); }

                    event.setPublicatedState(rs.getBoolean("public"));

                    currentEvent = event;
                    loadedEvent.put(event.getName(), event);

                    allEvent.add(event);
                }

                return null;
            }
        });

        for (Event ev : allEvent) { loadedEvent.put(ev.getName() ,ev ); }

        return FXCollections.observableArrayList(loadedEvent.values());
    }

    public Event copyEvent(Event m, User u) {
        Event ev = new Event(m.getName(), u);
        m.setID();

        String query = "INSERT INTO event (name, description, date, publicated, summarySheet, user, service ) " +
                "VALUES (\""+m.getName()+"-copia\", \""+m.getDescription()+"\", \""+m.getDate()+"\", "+ m.isPublicated() +
                ", "+ (m.getSh() == null ? -1 : m.getSh().getID())+", "
                + u.getId() +", "
                + ( m.getService() == null ? -1 : m.getService().getID() ) +" )";

        PersistenceManager.executeUpdate(query);

        ev.setName( m.getName() + "-copia" );
        ev.setDescription( m.getDescription() );
        m.setPublicatedState( m.isPublicated() );
        if (m.getSh() != null ) ev.setSH( m.getSh() );
        ev.setOwner( m.getOwner() );
        ev.setID();

        currentEvent = ev;
        return currentEvent;
    }

    public Event createEvent(Event s, User u, String date) {
        String query = "INSERT INTO event ( name, date, user) " +
                "VALUES ('" + s.getName() + "', \"" + date + "\", " + u.getId() + ");";

        System.out.println("query: " + query);

        PersistenceManager.executeUpdate( query );

        currentEvent = new Event(s.getName(), u);
        currentEvent.setDate(s.getDate());
        return currentEvent;
    }

    public void deleteEvent(Event sh, User u) {
        String query = "DELETE FROM event WHERE ID= " + sh.setID();
        PersistenceManager.executeUpdate(query);
    }

}
