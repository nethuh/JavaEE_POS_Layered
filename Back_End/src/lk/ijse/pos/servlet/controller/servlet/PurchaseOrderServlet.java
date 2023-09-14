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
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject details = reader.readObject();
        String cusId = details.getString("cusId");
        double total = Double.parseDouble(details.getString("total"));
        JsonArray items = details.getJsonArray("items");

        try (Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){

            String orderId = placeOrderBO.generateNewOrderID(connection);
            List<OrderDetailDTO> orderDetails = new ArrayList<>();
            for (JsonValue item : items) {
                JsonObject jsonObject = item.asJsonObject();
                orderDetails.add(new OrderDetailDTO(orderId, jsonObject.getString("code"), Double.parseDouble(jsonObject.getString("unitPrice")), Integer.parseInt(jsonObject.getString("qty"))));
            }
            if (placeOrderBO.purchaseOrder(connection, new OrderDTO(orderId, cusId, total, LocalDate.now().toString(), orderDetails))) {
                resp.setStatus(200);
                resp.getWriter().print(messageUtil.buildJsonObject("OK", "Order Placed", "").build());
            }
        } catch (ClassNotFoundException | SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(messageUtil.buildJsonObject("Error", e.getLocalizedMessage(), "").build());
        }
    }
}