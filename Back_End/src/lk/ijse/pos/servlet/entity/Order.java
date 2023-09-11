package lk.ijse.pos.servlet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {
    private String orderId;
    private String cusId;
    private double cost;
    private String orderDate;
}
