package lk.ijse.pos.servlet.dao.custom;

import lk.ijse.pos.servlet.dao.CrudDAO;
import lk.ijse.pos.servlet.entity.Customer;

import java.sql.Connection;

public interface CustomerDAO extends CrudDAO<Connection, Customer, String> {
}