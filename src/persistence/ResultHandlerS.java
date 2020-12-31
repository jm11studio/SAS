package persistence;

import businesslogic.service.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandlerS {
    public Service handle(ResultSet rs) throws SQLException;
}