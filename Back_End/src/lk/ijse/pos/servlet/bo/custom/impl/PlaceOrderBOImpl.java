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
import lk.ijse.pos.servlet.dto.OrderDetailDTO;
import lk.ijse.pos.servlet.entity.Customer;
import lk.ijse.pos.servlet.entity.Item;
import lk.ijse.pos.servlet.entity.Order;
import lk.ijse.pos.servlet.entity.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceOrderBOImpl implements PlaceOrderBO {
    private final PlaceOrderDAO placeOrderDAO = (PlaceOrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER_DETAILS);
    private final OrderDAO orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER);
    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ITEM);
    @Override
    public boolean purchaseOrder(Connection connection, OrderDTO order) throws SQLException, ClassNotFoundException {

        connection.setAutoCommit(false);
        if (!orderDAO.save(connection, new Order(order.getOrderId(), order.getCusId(), order.getCost(), order.getOrderDate()))) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        for (OrderDetailDTO detailDTO : order.getOrderDetails()) {
            if (!placeOrderDAO.save(connection, new OrderDetail(detailDTO.getOrderId(), detailDTO.getItemCode(), detailDTO.getPrice(), detailDTO.getQty()))) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            ItemDTO item = searchItem(connection, detailDTO.getItemCode());
            item.setQtyOnHand(item.getQtyOnHand() - detailDTO.getQty());

            if (!itemDAO.update(connection, new Item(item.getCode(), item.getName(), item.getQtyOnHand(), item.getPrice()))) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return true;

    }

    @Override
    public CustomerDTO searchCustomer(Connection connection,String id) throws SQLException, ClassNotFoundException {
        Customer customer = customerDAO.search(connection, id);
        return new CustomerDTO(customer.getCustId(),customer.getCustName(),customer.getAddress(),customer.getSalary());
    }

    @Override
    public ItemDTO searchItem(Connection connection,String itemCode) throws SQLException, ClassNotFoundException {
        Item item = itemDAO.search(connection, itemCode);
        return new ItemDTO(item.getCode(),item.getName(),item.getQty(),item.getPrice());

    }

    @Override
    public boolean checkItemIsAvailable(String code) {
        return false;
    }

    @Override
    public boolean checkCustomerIsAvailable(String id) {
        return false;
    }

    @Override
    public String generateNewOrderID(Connection connection) throws SQLException, ClassNotFoundException {
        return orderDAO.generateNewOrderId(connection);
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
