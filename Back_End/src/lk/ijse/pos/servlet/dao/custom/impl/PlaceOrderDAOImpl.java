package lk.ijse.pos.servlet.dao.custom.impl;

import lk.ijse.pos.servlet.dao.SQLUtil;
import lk.ijse.pos.servlet.dao.custom.PlaceOrderDAO;
import lk.ijse.pos.servlet.entity.Order;
import lk.ijse.pos.servlet.entity.OrderDetail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceOrderDAOImpl implements PlaceOrderDAO {

    @Override
    public ArrayList<OrderDetail> getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery(connection, "SELECT * FROM Order_Detail");
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        while (rst.next()){
            orderDetails.add(new OrderDetail(rst.getString(1), rst.getString(2),rst.getDouble(3),rst.getInt(4)));
        }
        return  orderDetails;
    }

    @Override
    public boolean save(Connection connection, OrderDetail entity) throws SQLException, ClassNotFoundException {

        return SQLUtil.executeUpdate(connection, "INSERT INTO Order_Detail VALUES (?,?,?,?)",entity.getOrderId(),entity.getCode(),entity.getPrice(),entity.getQty());
    }

    @Override
    public boolean update(Connection connection, OrderDetail dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetail search(Connection connection, String s) throws SQLException, ClassNotFoundException {
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
}
