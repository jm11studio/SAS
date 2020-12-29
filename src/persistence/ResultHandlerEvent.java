package persistence;

import businesslogic.event.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandlerEvent {
    public Event handle(ResultSet rs) throws SQLException;
}
