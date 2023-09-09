package lk.ijse.pos.servlet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class CustomerDTO {
    String cusId;
    String cusName;
    String address;
    double salary;
}
