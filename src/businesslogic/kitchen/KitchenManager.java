package businesslogic.kitchen;

import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import businesslogic.summarySheet.SummarySheet;
import businesslogic.task.Task;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class KitchenManager {

    private static Map<String, SummarySheet> loadedSummerySheet = FXCollections.observableHashMap();
    private User owner;
    private static SummarySheet currentSH;

    public static void setCurrentSummarySheet(SummarySheet sh) {
        currentSH = sh;
    }


    public static ObservableList<SummarySheet> getAllSumamarySheet() {

        ArrayList<SummarySheet> allSummarySheet = new ArrayList<>();
        String query = "SELECT * FROM `summarysheet` WHERE 1";

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public SummarySheet handle(ResultSet rs) throws SQLException {

                SummarySheet sh = new SummarySheet(rs.getString("title"), User.loadUserById( rs.getInt("owner") ) );

                if ( !loadedSummerySheet.containsKey( sh.getTitle() ) ) {

                    try { sh.setDescription(rs.getString("description")); }
                    catch (KitchenException e ) { e.printStackTrace(); }

                    sh.setPublicatedState(rs.getBoolean("public"));

                    if ( rs.getInt("id") > 0 ) sh.addTask( Task.getTaskById(rs.getInt("id")));

                    currentSH = sh;
                    loadedSummerySheet.put(sh.getTitle(), sh);

                    allSummarySheet.add(sh);
                } else {

                    if ( rs.getInt("id") > 0 ) {
                        Task t = Task.getTaskById(rs.getInt("id"));
                        if (loadedSummerySheet.get(sh.getTitle()).getTasksList( sh.setID() ).contains(t)) {
                            loadedSummerySheet.get(sh.getTitle()).getTasksList( sh.getID() ).add(t);
                        }
                    }

                }

                return null;
            }
        });
        for (SummarySheet sh : allSummarySheet) { loadedSummerySheet.put(sh.getTitle() ,sh ); }

        return FXCollections.observableArrayList(loadedSummerySheet.values());
    }

    public static void deleteSummarySheet(SummarySheet sh, User u) {
        String query = "DELETE FROM `summarysheet` WHERE ID= " + sh.setID();
        PersistenceManager.executeUpdate(query);


        // TODO delete all task connected to sh
        String queryDeleteTasks = "DELETE FROM task WHERE SHid= " +sh.getID();
        PersistenceManager.executeUpdate(queryDeleteTasks);
    }

    public static void chooseSummarySheet(SummarySheet m, User u) { currentSH = m; }

    public static SummarySheet copySummarySheet(SummarySheet m, User u) throws KitchenException {
        SummarySheet sh = new SummarySheet(m.getTitle(), u);
        m.setID();

        String query = "INSERT INTO summarysheet (title, description, public, MenuID, owner ) " +
                "VALUES (\""+m.getTitle()+"-copia\", \""+m.getDescription()+"\", "+ m.isPublicated() +
                ", "+ m.getM().getId() +", "+m.getOwner().getId()+" )";
        PersistenceManager.executeUpdate(query);

        sh.setTile( m.getTitle() + "-copia" );
        sh.setDescription( m.getDescription() );
        m.setPublicatedState( m.isPublicated() );
        sh.setM( m.getM() );
        sh.isOwner( m.getOwner() );
        sh.setID();

        currentSH = sh;
        return currentSH;
    }

    public static SummarySheet createSummarySheet(String s, User u) {
        String query = "INSERT INTO summarysheet ( title, owner) VALUES ('" + s + "'," + u.getId() + ")";

        PersistenceManager.executeUpdate( query );

        currentSH = new SummarySheet(s, u);
        return currentSH;
    }

    public static SummarySheet getCurrentSummarySheet() { return currentSH; }

    public static Task defineTask(String s) throws UseCaseLogicException {

        if (currentSH == null) throw new UseCaseLogicException();
        currentSH.setID();

        Task t = new Task();
        t.name = s;

        currentSH.addTask(t);
        notifyTaskAdd(currentSH, t);

        return t;
    }

    private static void notifyTaskAdd(SummarySheet sh, Task t) {
        String queryAdd = "INSERT INTO `task` (`name`, `shID`) VALUES (\"" + t.getName() + "\", " + sh.getID() + ")";

        PersistenceManager.executeUpdate(queryAdd);
    }


    public static void publish() throws KitchenException {
        if (currentSH == null) throw new KitchenException();
        currentSH.setPublicatedState(true);
        notyfySHPublicated();
    }

    private static void notyfySHPublicated() {
        currentSH.setID();
        String query = "UPDATE summarysheet SET public=1 WHERE ID=" + currentSH.getID();

        PersistenceManager.executeUpdate(query);
    }

    public void defineSection(int id, String s) {
        String query = "";

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public SummarySheet handle(ResultSet rs) throws SQLException {
                return null;
            }
        });
    }

    public static void deleteSection(Task sec) throws KitchenException {
        if (sec == null) throw new KitchenException();

        String query = "DELETE FROM task WHERE id=" + sec.IdLoader();

        PersistenceManager.executeUpdate(query);
    }
}