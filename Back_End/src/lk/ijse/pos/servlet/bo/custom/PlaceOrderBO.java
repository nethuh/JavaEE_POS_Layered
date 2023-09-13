package lk.ijse.pos.servlet.bo.custom;

import lk.ijse.pos.servlet.bo.SuperBO;
import lk.ijse.pos.servlet.dto.CustomerDTO;
import lk.ijse.pos.servlet.dto.ItemDTO;
import lk.ijse.pos.servlet.dto.OrderDTO;

import java.util.ArrayList;

public interface PlaceOrderBO extends SuperBO {

    boolean placeOrder (OrderDTO dto);
    CustomerDTO searchCustomer (String id);
    ItemDTO searchItem(String code);
    boolean checkItemsAvailable(String code);
    boolean checkCustomersAvailable(String id);
    String generateNewOrderID();
    ArrayList<CustomerDTO>getAllCustomers();
    ArrayList<ItemDTO> getAllItems();
}
