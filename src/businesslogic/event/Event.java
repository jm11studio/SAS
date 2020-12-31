package businesslogic.event;

import businesslogic.CatERing;
import businesslogic.service.Service;
import businesslogic.summarySheet.SummarySheet;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandlerE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class Event {

    private String name;
    private String description;
    private java.util.Date date;
    private Time time_start;
    private Time time_end;
    private SummarySheet sh;
    private int timeEventRepetition;
    private boolean publicated;
    private User user;
    private Service service;
    private int id = -1;

    public Event(String name, User owner) {
        this.name = name;
        this.user = owner;
    }

    public static ObservableList<Event> getEventList() {
        ObservableList<Event> eventList = FXCollections.observableArrayList();
        String query = "SELECT * FROM event WHERE 1;";

        PersistenceManager.executeQueryE(query, new ResultHandlerE() {
            @Override
            public Event handle(ResultSet rs) throws SQLException {
                Event ev = new Event(rs.getString("name"), User.loadUserById(rs.getInt("user")) );

                ev.setDescription(rs.getString("description"));
                ev.setDate( rs.getDate("date") );
                ev.setTime_start( rs.getTime("time_start") );
                ev.setTime_end( rs.getTime("time_end") );

                if (  rs.getInt("summarySheet") >= 1 ) ev.setSH( SummarySheet.getByID(rs.getInt("summarySheet")) );
                else ev.setSH( null );

                ev.setTimeEventRepetition( rs.getInt("timeEventRepetition") );
                ev.setPublicatedState( rs.getBoolean("publicated") );
                ev.setService( Service.getById(rs.getInt("service")) );
                ev.setID( rs.getInt("id") );

                eventList.add(ev);


                return null;
            }
        });

        return eventList;
    }


    public String getName() { return name; }


    public int setID() {
        final int[] sID = {-1};

        if (id == -1 && date != null) {
            // chiede al database il proprio ID
            String query = "SELECT id FROM event WHERE name =\"" + name + "\" AND" +
                    "user = " + CatERing.getInstance().getUserManager().getCurrentUser() + ";";


            PersistenceManager.executeQueryE(query, new ResultHandlerE() {
                @Override
                public Event handle(ResultSet rs) throws SQLException {
                    sID[0] = rs.getInt("id");
                    return null;
                }
            });
        }
        this.id = sID[0];

        return this.id;
    }


    @Override
    public String toString() { return name; }

    public void setID(int id) { this.id = id; }
    public int getID() { return id; }

    public void setName(String s) { this.name = name; }

    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return description; }

    public Date getDate() { return this.date; }
    public void setDate(Date date) { this.date = date; }

    public Time getTime_start() { return time_start; }
    public void setTime_start(Time time_start) { this.time_start = time_start; }

    public Time getTime_end() { return time_end; }
    public void setTime_end(Time time_end) { this.time_end = time_end; }

    public int getTimeEventRepetition() { return this.timeEventRepetition; }
    public void setTimeEventRepetition(int ter) { this.timeEventRepetition = ter; }

    public void setPublicatedState(boolean aPublic) { this.publicated = aPublic; }
    public boolean isPublicated() { return publicated; }

    public void setSH(SummarySheet sh) { this.sh = sh; }
    public SummarySheet getSh() { return this.sh; }

    public void setOwner(User owner) { this.user = owner; }
    public User getOwner() { return this.user; }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

}
