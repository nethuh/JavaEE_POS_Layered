package lk.ijse.pos.servlet.dao;

import lk.ijse.pos.servlet.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.pos.servlet.dao.custom.impl.ItemDAOImpl;
import lk.ijse.pos.servlet.dao.custom.impl.OrderDAOImpl;
import lk.ijse.pos.servlet.dao.custom.impl.PlaceOrderDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {

    }

    public static DAOFactory getInstance() {
        return daoFactory == null ? new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {
        CUSTOMER, ITEM , ORDER ,ORDER_DETAILS
    }

    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDER_DETAILS:
                return  new PlaceOrderDAOImpl();
            default:
                return null;
        }
    }

}
