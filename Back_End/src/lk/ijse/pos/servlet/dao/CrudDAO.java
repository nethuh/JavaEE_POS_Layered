package lk.ijse.pos.servlet.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<Connection, T, ID> extends SuperDAO {
    ArrayList<T> getAll(Connection connection) throws SQLException, ClassNotFoundException;

    boolean save(Connection connection, T dto) throws SQLException, ClassNotFoundException;

    boolean update(Connection connection, T dto) throws SQLException, ClassNotFoundException;

    T search(Connection connection, ID id) throws SQLException, ClassNotFoundException;

    boolean exit(Connection connection, ID id) throws SQLException, ClassNotFoundException;

    boolean delete(Connection connection, ID id) throws SQLException, ClassNotFoundException;
}