package lk.ijse.pos.servlet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Customer {
    String custId;
    String custName;
    String address;
    double salary;
}
