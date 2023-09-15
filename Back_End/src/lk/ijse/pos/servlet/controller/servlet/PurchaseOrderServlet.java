package lk.ijse.pos.servlet.controller.servlet;

import lk.ijse.pos.servlet.bo.BOFactory;
import lk.ijse.pos.servlet.bo.custom.PlaceOrderBO;
import lk.ijse.pos.servlet.dto.CustomerDTO;
import lk.ijse.pos.servlet.dto.ItemDTO;
import lk.ijse.pos.servlet.dto.OrderDTO;
import lk.ijse.pos.servlet.dto.OrderDetailDTO;
import lk.ijse.pos.servlet.util.MessageUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/placeOrder"})
public class PurchaseOrderServlet extends HttpServlet {
    private final PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.ORDER);
    MessageUtil messageUtil = new MessageUtil();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
            JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        String orderId = jsonObject.getString("order_ID");
        String orderDate = jsonObject.getString("date");
        String customerId = jsonObject.getString("customer_ID");
            Double total = jsonObject.getJsonNumber("total").doubleValue();
            JsonArray orderDetails = jsonObject.getJsonArray("orderDetails");

            System.out.println(orderDetails);

            ArrayList<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
            for (JsonValue orderdetail : orderDetails){
                String itemId = orderdetail.asJsonObject().getString("code");
                String description = orderdetail.asJsonObject().getString("description");
                Double unitPrice = orderdetail.asJsonObject().getJsonNumber("unitPrice").doubleValue();
                int qty = orderdetail.asJsonObject().getInt("qty");

                orderDetailDTOS.add(new OrderDetailDTO(orderId, itemId, unitPrice, qty));
            }
            OrderDTO orderDTO = new OrderDTO(orderId, orderDate, customerId, total, orderDetailDTOS);
            System.out.println(orderDTO);

            boolean isAdded = placeOrderBO.purchaseOrder((Connection) orderDTO, (OrderDTO) connection);

            if (isAdded){
                resp.getWriter().print(messageUtil.buildJsonObject("Success", "Orders Added",""));

            }else {
                resp.getWriter().print(messageUtil.buildJsonObject("Error", "Failed to Add Order", ""));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    }
