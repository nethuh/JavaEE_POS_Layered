package lk.ijse.pos.servlet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class OrderDTO {
    private String orderId;
    private String cusId;
    private double cost;
    private String orderDate;
    private List<OrderDetailDTO> orderDetails;
}
