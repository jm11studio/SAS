package persistence;

import businesslogic.task.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandlerK {
    public Task handle(ResultSet rs) throws SQLException;
}
