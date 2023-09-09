package lk.ijse.pos.servlet.dao;

import lk.ijse.pos.servlet.dao.custom.impl.CustomerDAOImpl;

public class DAOFactory {
    public enum DAOTypes {
        CUSTOMER, ITEM
    }

    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            default:
                return null;
        }
    }

}
