package lk.ijse.pos.servlet.dao.custom;

import lk.ijse.pos.servlet.dao.CrudDAO;
import lk.ijse.pos.servlet.entity.Item;

import java.sql.Connection;

public interface ItemDAO extends CrudDAO<Connection, Item, String> {
}
