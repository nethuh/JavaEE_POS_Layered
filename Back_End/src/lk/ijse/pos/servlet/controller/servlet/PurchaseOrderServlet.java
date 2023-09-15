package lk.ijse.pos.servlet.controller.servlet;

import lk.ijse.pos.servlet.bo.BOFactory;
import lk.ijse.pos.servlet.bo.custom.PlaceOrderBO;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/placeOrder"})
public class PurchaseOrderServlet extends HttpServlet {
    private final PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.ORDER);
    MessageUtil messageUtil = new MessageUtil();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()) {
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();

            String orderId = jsonObject.getString("order_ID");
            String orderDate = jsonObject.getString("date");
            String customerId = jsonObject.getString("customer_ID");
            double total = jsonObject.getJsonNumber("total").doubleValue();
            JsonArray orderDetails = jsonObject.getJsonArray("orderDetails");

            System.out.println(orderDetails);

            List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
            for (JsonValue orderdetail : orderDetails) {
                JsonObject itemObject = orderdetail.asJsonObject();
                String itemId = itemObject.getString("code");
                String description = itemObject.getString("description");
                double unitPrice = itemObject.getJsonNumber("unitPrice").doubleValue();
                int qty = itemObject.getInt("qty");

                orderDetailDTOS.add(new OrderDetailDTO(orderId, itemId, unitPrice, qty));
            }

            OrderDTO orderDTO = new OrderDTO(orderId, orderDate, customerId, total, orderDetailDTOS);
            System.out.println(orderDTO);

            boolean isAdded = placeOrderBO.purchaseOrder(connection,orderDTO );

            if (isAdded) {
                resp.getWriter().print(messageUtil.buildJsonObject("Success", "Order Added", ""));
            } else {
                resp.getWriter().print(messageUtil.buildJsonObject("Error", "Failed to Add Order", ""));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
