package persistence;

import businesslogic.event.Event;
import businesslogic.summarySheet.SummarySheet;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandler {
    public SummarySheet handle(ResultSet rs) throws SQLException;
}
