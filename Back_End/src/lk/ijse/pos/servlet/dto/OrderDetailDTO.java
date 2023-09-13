package lk.ijse.pos.servlet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class OrderDetailDTO {
    private String orderId;
    private String itemCode;
    private double price;
    private int qty;
}
