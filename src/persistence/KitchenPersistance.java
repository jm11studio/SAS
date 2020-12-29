package persistence;

import businesslogic.kitchen.KitchenEventReciver;
import businesslogic.summarySheet.SummarySheet;
import businesslogic.task.Task;

public class KitchenPersistance implements KitchenEventReciver {

    public static void executeQuery(String query, ResultHandler name) {
    }


    // TODO: connessione al database

    @Override
    public void notifySummarySheetAdd(SummarySheet sh) {

    }

    @Override
    public void notifySummarySheetDelete(SummarySheet sh) {

    }

    @Override
    public void notifyTaskAdd(Task t) {

    }

    @Override
    public void notifyTaskDelete(Task t) {

    }

}
