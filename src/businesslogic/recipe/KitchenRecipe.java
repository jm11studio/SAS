package businesslogic.recipe;

import businesslogic.summarySheet.SummarySheet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.KitchenPersistance;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class KitchenRecipe {
    private static Map<Integer, KitchenRecipe> all = new HashMap<>();

    private int id;
    private String name;

    private KitchenRecipe() {

    }

    public KitchenRecipe(String name) {
        id = 0;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<KitchenRecipe> loadAllRecipes() {
        String query = "SELECT * FROM Recipes";
        KitchenPersistance.executeQuery(query, new ResultHandler() {
            @Override
            public SummarySheet handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                if (all.containsKey(id)) {
                    KitchenRecipe rec = all.get(id);
                    rec.name = rs.getString("name");
                } else {
                    KitchenRecipe rec = new KitchenRecipe(rs.getString("name"));
                    rec.id = id;
                    all.put(rec.id, rec);
                }
                return null;
            }
        });
        ObservableList<KitchenRecipe> ret =  FXCollections.observableArrayList(all.values());
        Collections.sort(ret, new Comparator<KitchenRecipe>() {
            @Override
            public int compare(KitchenRecipe o1, KitchenRecipe o2) {
                return (o1.getName().compareTo(o2.getName()));
            }
        });
        return ret;
    }

    public static ObservableList<KitchenRecipe> getAllRecipes() {
        return FXCollections.observableArrayList(all.values());
    }

    public static KitchenRecipe loadRecipeById(int id) {
        if (all.containsKey(id)) return all.get(id);
        KitchenRecipe rec = new KitchenRecipe();
        String query = "SELECT * FROM Recipes WHERE id = " + id;
        KitchenPersistance.executeQuery(query, new ResultHandler() {
            @Override
            public SummarySheet handle(ResultSet rs) throws SQLException {
                    rec.name = rs.getString("name");
                    rec.id = id;
                    all.put(rec.id, rec);
                return null;
            }
        });
        return rec;
    }


}
