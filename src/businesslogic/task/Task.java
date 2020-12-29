package businesslogic.task;

import businesslogic.recipe.Recipe;
import businesslogic.summarySheet.SummarySheet;
import businesslogic.user.User;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;
import persistence.ResultHandlerK;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Task {

    public int shID;
    public String name;
    private int id;
    public boolean completed;
    public Recipe recipe;
    public double quantity;
    public double time;
    public User cook;

    public Task() {}

    public Task(String name, Recipe recipe, double quantity, double time) {
        this.name = name;
        this.recipe = recipe;
        this.quantity = quantity;
        this.time = time;

        this.completed = false;
        this.id = -1;
        this.cook = null;
    }

//    public void addTask (Task t) { TaskList.add(t); }

    @Override
    public String toString() {
        return "Task: " + name  + ( ( id > 0 ) ? " Assegnato al turno " + id : " non ancora assegnato a nessun turno" ) + ".\n" +
                ( completed? "" : "non") + " completa\n";
    }

    public int getId() { return id; }

    public int IdLoader() {
        final int[] rtrn = {0};
        final String[] nm = {""};
        String query = "SELECT * FROM task WHERE name = \"" + this.name + "\"";

        PersistenceManager.executeQueryK(query, new ResultHandlerK() {

            @Override
            public Task handle(ResultSet rs) throws SQLException {
                rtrn[0] = rs.getInt("id");
                nm[0] = rs.getString("name");

                return null;
            }
        });

        this.name = nm[0];
        return rtrn[0];
    }

    public boolean setShiftID(int shiftID) {

        if (shiftID < 0 ) return false;
        // TODO: Decrease shift place of one unit. If false return

        this.id = shiftID;
        return true;
    }

    public String getName() { return name; }

//    public ObservableList<Task> getItems() {
//        return FXCollections.unmodifiableObservableList(this.TaskList);
//    }

    public static Task getTaskById(int id) {
        if ( id <= 0 ) return null;

        Task t = new Task();
        String query = "SELECT * FROM task WHERE id = " + id;

        PersistenceManager.executeQueryK(query, new ResultHandlerK() {

            @Override
            public Task handle(ResultSet rs) throws SQLException {
                t.name = rs.getString("name");
                t.recipe = Recipe.loadRecipeById( rs.getInt("recipe") );
                t.quantity = rs.getDouble("quantity");
                t.time = rs.getDouble("time");
                t.id = rs.getInt("id");
                t.completed = rs.getBoolean("completed");
                t.cook = User.loadUserById( rs.getInt("cook") );
                t.shID = rs.getInt("shID");

                return null;
            }

        });

        System.out.println("test t[0].getName: " + t.getName() );

        return t;
    }

}
