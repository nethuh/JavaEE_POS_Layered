package lk.ijse.pos.servlet.dao.custom.impl;

import lk.ijse.pos.servlet.dao.SQLUtil;
import lk.ijse.pos.servlet.dao.custom.OrderDAO;
import lk.ijse.pos.servlet.entity.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public ArrayList<Order> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean save(Connection connection, Order dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(Connection connection, Order dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Order search(Connection connection, String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean exit(Connection connection, String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Connection connection, String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public String generateNewOrderId(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery(connection,"SELECT orderId FROM `Order`ORDER BY orderID DESC LIMIT 1");
        return rst.next() ? String.format("OID%03d", (Integer.parseInt(rst.getString("orderID").replace("OID",""))+1)):"OID001";
    }
}
