package lk.ijse.pos.servlet.dao.custom;

import lk.ijse.pos.servlet.dao.CrudDAO;
import lk.ijse.pos.servlet.entity.Order;

import java.sql.Connection;
import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Connection, Order , String> {
    String generateNewOrderId(Connection connection) throws SQLException, ClassNotFoundException;
}
