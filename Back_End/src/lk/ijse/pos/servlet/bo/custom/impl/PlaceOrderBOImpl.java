package lk.ijse.pos.servlet.bo.custom.impl;

import lk.ijse.pos.servlet.bo.custom.PlaceOrderBO;
import lk.ijse.pos.servlet.dao.DAOFactory;
import lk.ijse.pos.servlet.dao.custom.CustomerDAO;
import lk.ijse.pos.servlet.dao.custom.ItemDAO;
import lk.ijse.pos.servlet.dao.custom.OrderDAO;
import lk.ijse.pos.servlet.dao.custom.PlaceOrderDAO;
import lk.ijse.pos.servlet.dto.CustomerDTO;
import lk.ijse.pos.servlet.dto.ItemDTO;
import lk.ijse.pos.servlet.dto.OrderDTO;

import java.util.ArrayList;

public class PlaceOrderBOImpl implements PlaceOrderBO {
    private final PlaceOrderDAO placeOrderDAO = (PlaceOrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER);
    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ITEM);
    @Override
    public boolean placeOrder(OrderDTO dto) {
        return false;
    }

    @Override
    public CustomerDTO searchCustomer(String id) {
        return null;
    }

    @Override
    public ItemDTO searchItem(String code) {
        return null;
    }

    @Override
    public boolean checkItemsAvailable(String code) {
        return false;
    }

    @Override
    public boolean checkCustomersAvailable(String id) {
        return false;
    }

    @Override
    public String generateNewOrderID() {
        return null;
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() {
        return null;
    }

    @Override
    public ArrayList<ItemDTO> getAllItems() {
        return null;
    }
}
