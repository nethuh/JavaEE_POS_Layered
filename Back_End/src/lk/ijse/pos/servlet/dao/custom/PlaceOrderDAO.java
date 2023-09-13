package lk.ijse.pos.servlet.dao.custom;

import lk.ijse.pos.servlet.dao.CrudDAO;
import lk.ijse.pos.servlet.entity.Order;
import lk.ijse.pos.servlet.entity.OrderDetail;

import java.sql.Connection;

public interface PlaceOrderDAO extends CrudDAO<Connection, OrderDetail,String> {
}
