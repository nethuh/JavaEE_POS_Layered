package lk.ijse.pos.servlet.bo.custom;

import lk.ijse.pos.servlet.bo.SuperBO;
import lk.ijse.pos.servlet.dto.CustomerDTO;
import lk.ijse.pos.servlet.dto.ItemDTO;
import lk.ijse.pos.servlet.dto.OrderDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface PlaceOrderBO extends SuperBO {

    boolean purchaseOrder(Connection connection, OrderDTO dto) throws SQLException, ClassNotFoundException;

    CustomerDTO searchCustomer(Connection connection, String id) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(Connection connection, String code) throws SQLException, ClassNotFoundException;

    boolean checkItemIsAvailable(String code);

    boolean checkCustomerIsAvailable(String id);

    String generateNewOrderID(Connection connection) throws SQLException, ClassNotFoundException;

    ArrayList<CustomerDTO> getAllCustomers();

    ArrayList<ItemDTO> getAllItems();
}
