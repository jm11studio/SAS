package businesslogic.kitchen;

import businesslogic.summarySheet.SummarySheet;
import businesslogic.task.Task;

public interface KitchenEventReciver {

    public void notifySummarySheetAdd(SummarySheet sh);
    public void notifySummarySheetDelete(SummarySheet sh);

    public void notifyTaskAdd(Task t);
    public void notifyTaskDelete(Task t);
}
