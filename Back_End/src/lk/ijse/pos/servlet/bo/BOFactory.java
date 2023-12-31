package lk.ijse.pos.servlet.bo;

import lk.ijse.pos.servlet.bo.custom.impl.CustomerBOImpl;
import lk.ijse.pos.servlet.bo.custom.impl.ItemBOImpl;
import lk.ijse.pos.servlet.bo.custom.impl.PlaceOrderBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {

    }

    public static BOFactory getInstance() {

        return boFactory == null ? new BOFactory() : boFactory;
    }

    public enum BOTypes {
        CUSTOMER, ITEM , ORDER
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case ORDER:
                return new PlaceOrderBOImpl();
            default:
                return null;
        }
    }

}