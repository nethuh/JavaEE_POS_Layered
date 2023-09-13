package lk.ijse.pos.servlet.bo.custom.impl;

import lk.ijse.pos.servlet.bo.SuperBO;
import lk.ijse.pos.servlet.bo.custom.ItemBO;
import lk.ijse.pos.servlet.dao.DAOFactory;
import lk.ijse.pos.servlet.dao.custom.ItemDAO;
import lk.ijse.pos.servlet.dto.ItemDTO;
import lk.ijse.pos.servlet.entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl implements ItemBO, SuperBO {
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public ArrayList<ItemDTO> getAllItems(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<Item> all = itemDAO.getAll(connection);
        ArrayList<ItemDTO> allItems = new ArrayList<>();
        for (Item item :
                all) {
            allItems.add(new ItemDTO(item.getCode(), item.getName(), item.getQty(), item.getPrice()));
        }
        return allItems;
    }

    @Override
    public boolean deleteItems(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(connection, id);
    }

    @Override
    public boolean saveItem(Connection connection, ItemDTO item) throws SQLException, ClassNotFoundException {
        return itemDAO.save(connection, new Item(item.getCode(), item.getName(), item.getQtyOnHand(), item.getPrice()));
    }

    @Override
    public boolean updateItem(Connection connection, ItemDTO item) throws SQLException, ClassNotFoundException {
        return itemDAO.update(connection, new Item(item.getCode(), item.getName(), item.getQtyOnHand(), item.getPrice()));
    }

    @Override
    public boolean exitsItem(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return false;
    }
}
