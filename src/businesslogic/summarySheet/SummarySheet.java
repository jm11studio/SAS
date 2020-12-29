package businesslogic.summarySheet;

import businesslogic.kitchen.KitchenException;
import businesslogic.menu.Menu;
import businesslogic.task.Task;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;
import persistence.ResultHandlerK;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SummarySheet {

    private  int ID;
    private String title;
    private String description;
    private ArrayList<Task> tasksList;
    private boolean publicated;
    private Menu m;
    private User owner;
    private static ObservableList<Task> Tasks = FXCollections.observableArrayList();


    public SummarySheet(String title, User owner) {
        this.title = title;
        this.description = "";
        this.owner = owner;

        ID = -1;
        m = null;
        tasksList = new ArrayList<Task>();
        publicated = false;

    }

    public static SummarySheet getByID(int sh) {
        String query = "SELECT * FROM summarySheet Where ID="+sh+";";
        final SummarySheet[] smht = {null};


        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public SummarySheet handle(ResultSet rs) throws SQLException {
                smht[0] = new SummarySheet(rs.getString("title"), User.loadUserById(rs.getInt("owner")));
                smht[0].setPublicatedState( rs.getBoolean("publicated") );

                return null;
            }
        });

        return smht[0];
    }

    public void addTask( Task t) {
        tasksList.add(t);
        Tasks.add(t);
    }

    public Task getTask(String name) {
        for( Task t: tasksList) if ( t.name == name ) return t;
        return null;
    }

    public int setID() {
        final int[] id = {-1};
        final Menu[] m1 = new Menu[1];

        String query = "SELECT * FROM summarysheet WHERE title='" + title +  "'";

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public SummarySheet handle(ResultSet rs) throws SQLException {
                id[0] = rs.getInt("ID");
                m1[0] = Menu.loadMenuID( rs.getInt("MenuID") );
                return null;
            }
        });

        m = m1[0];
        ID = id[0];
        return id[0];
    }


    public int getID() { return ID; }


    public ArrayList<Task> getTasksList(int i) {
        String query = "SELECT * FROM task WHERE shID=" + i;

        tasksList.clear();
        Tasks.clear();

        PersistenceManager.executeQueryK(query, new ResultHandlerK() {
            @Override
            public Task handle(ResultSet rs) throws SQLException {
                Task t = Task.getTaskById( rs.getInt("id") );
                addTask(t);

                return null;
            }
        });

        return tasksList;
    }


    public boolean isPublicated() { return publicated; }
    public void setPublicatedState( boolean publicatedStatus ) { publicated = publicatedStatus; }


    @Override
    public String toString() {
        return title + " ( Autore: "+owner.getUserName()+" )\t\t" +
                ( publicated ? "" :"non " ) + "pubblicato.";
    }


    public String getTitle() { return title; }

    public void setM(Menu mn) { m = mn; }
    public Menu getM() { return m; }

    public User getOwner() { return owner; }
    public boolean isOwner(User u) { return u.equals(owner); }

    public String getDescription() {
        return description;
    }

    public ObservableList<Task> getTasks(int i) { getTasksList(i); return this.Tasks; }

    public void setTile(String s) throws KitchenException {
        if (s == null) throw new KitchenException();

        int currentID = setID();
        title = s;

        String query = "UPDATE summarysheet SET title= \"" + s + "\" WHERE ID= " + currentID;

        PersistenceManager.executeUpdate(query);
    }

    public void setDescription(String description) throws KitchenException {
        if (description == null) throw new KitchenException();

        int currentID = setID();
        this.description = description;

        String query = "UPDATE summarysheet SET description= \"" + description + "\" " +
                "WHERE id= " + currentID;

        PersistenceManager.executeUpdate(query);
    }

    public Menu setMenu(String s) throws KitchenException {
        if (s == null ) throw new KitchenException();
        for (char c : s.toCharArray()) { if (!Character.isDigit(c)) throw new KitchenException(); }

        int s1 = Integer.parseInt( s );

        int currentID = setID();
        this.m = Menu.loadMenuID( s1 );

        String query = "UPDATE summarysheet SET MenuID= \"" + s + "\" WHERE id= " + currentID;

        PersistenceManager.executeUpdate(query);
        return null;
    }

}
