package lk.ijse.pos.servlet.bo.custom.impl;

import lk.ijse.pos.servlet.bo.SuperBO;
import lk.ijse.pos.servlet.bo.custom.CustomerBO;
import lk.ijse.pos.servlet.dao.DAOFactory;
import lk.ijse.pos.servlet.dao.custom.CustomerDAO;
import lk.ijse.pos.servlet.dto.CustomerDTO;
import lk.ijse.pos.servlet.entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO, SuperBO {
    private final CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public ArrayList<CustomerDTO> getAllCustomers(Connection connection) throws SQLException, ClassNotFoundException {
        ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
        ArrayList<Customer> all = customerDAO.getAll(connection);
        for (Customer customer : all
        ) {
            allCustomers.add(new CustomerDTO(customer.getCustId(), customer.getCustName(), customer.getAddress(), customer.getSalary()));
        }
        return allCustomers;
    }

    @Override
    public boolean saveCustomer(Connection connection, CustomerDTO customer) throws SQLException, ClassNotFoundException {
        return customerDAO.save(connection, new Customer(customer.getCusId(), customer.getCusName(), customer.getAddress(), customer.getSalary()));
    }

    @Override
    public boolean updateCustomer(Connection connection, CustomerDTO customer) throws SQLException, ClassNotFoundException {
        return customerDAO.update(connection, new Customer(customer.getCusId(), customer.getCusName(), customer.getAddress(), customer.getSalary()));
    }

    @Override
    public boolean exitsCustomer(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean deleteCustomer(Connection connection, String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(connection, id);
    }

    @Override
    public String generateNewCustomerId(Connection connection) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public ArrayList<CustomerDTO> filterCustomers(Connection connection, String field, String value) throws SQLException, ClassNotFoundException {
        return null;
    }
}
