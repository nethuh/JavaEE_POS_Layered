package lk.ijse.pos.servlet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class OrderDTO {
    private String orderId;
    private String cusId;
    private String orderDate;
    private double cost;
    
    private List<OrderDetailDTO> orderDetails;

    public OrderDTO(String orderId, String orderDate, String customerId, ArrayList<OrderDetailDTO> orderDetailDTOS) {
    }
}
