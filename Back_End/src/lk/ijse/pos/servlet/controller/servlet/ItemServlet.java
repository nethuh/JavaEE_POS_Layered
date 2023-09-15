package lk.ijse.pos.servlet.controller.servlet;

import lk.ijse.pos.servlet.bo.BOFactory;
import lk.ijse.pos.servlet.bo.custom.ItemBO;
import lk.ijse.pos.servlet.bo.custom.PlaceOrderBO;
import lk.ijse.pos.servlet.dto.ItemDTO;
import lk.ijse.pos.servlet.util.MessageUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static java.lang.Class.forName;
@WebServlet (urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    private final MessageUtil messageUtil = new MessageUtil();
    ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.ITEM);
    private final PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.ORDER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String option = req.getParameter("option");

        try (Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
            switch (option) {
                case "getAll":
                    JsonArrayBuilder allItems = Json.createArrayBuilder();
                ArrayList<ItemDTO> items = itemBO.getAllItems(connection);
                for (ItemDTO item : items) {
                    JsonObjectBuilder JsonItem = Json.createObjectBuilder();
                    JsonItem.add("code", item.getCode());
                    JsonItem.add("name", item.getName());
                    JsonItem.add("Qty", item.getQtyOnHand());
                    JsonItem.add("Price", item.getPrice());
                    allItems.add(JsonItem.build());
                }
                resp.getWriter().print(allItems.build());
                break;

            case "search":
               ItemDTO itemDTO = placeOrderBO.searchItem(connection,req.getParameter("ItemCode"));
                if (itemDTO != null) {
                    JsonObjectBuilder objectBuilder1 = Json.createObjectBuilder();

                    objectBuilder1.add("code", itemDTO.getCode());
                    objectBuilder1.add("description", itemDTO.getName());
                    objectBuilder1.add("unitPrice", itemDTO.getPrice());
                    objectBuilder1.add("qty", itemDTO.getQtyOnHand());

                    resp.setStatus(200);
                    resp.getWriter().print(objectBuilder1.build());
                }else {
                    throw new SQLException("No Such CusID");
                }

                break;
            }
        } catch (ClassNotFoundException | SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(messageUtil.buildJsonObject("Error", e.getLocalizedMessage(), "").build());

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String description = req.getParameter("description");
        double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));
        int qtyOnHand = Integer.parseInt(req.getParameter("qtyOnHand"));

        try (Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){

               if (itemBO.saveItem(connection, new ItemDTO(code,description,qtyOnHand,unitPrice))) {
                   resp.setStatus(200);
                   resp.getWriter().print(messageUtil.buildJsonObject("OK","Successfully Added", "").build());
               }
        } catch (SQLException | ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(messageUtil.buildJsonObject("Error",e.getLocalizedMessage(), "").build());

        }
    }

    //    query string
//    JSON
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        try (Connection connection =((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
            if (itemBO.deleteItems(connection, code)) {
                resp.getWriter().print(messageUtil.buildJsonObject("OK", "Successfully Deleted..!", "").build());
            } else {
                throw new RuntimeException("There is no Item for that ID..!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(messageUtil.buildJsonObject("Error", e.getLocalizedMessage(), "").build());
        }
    }

    //    query string
//    JSON
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject item = Json.createReader(req.getReader()).readObject();
        String code = item.getString("code");
        String description = item.getString("description");
        double unitPrice = Double.parseDouble(item.getString("unitPrice"));
        int qtyOnHand = item.getInt("qtyOnHand");
         try (Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbpc")).getConnection()){
              if (itemBO.updateItem(connection,new ItemDTO(code,description,qtyOnHand,unitPrice))) {
                resp.setStatus(200);
                resp.getWriter().print(messageUtil.buildJsonObject("OK","Successfully Updated","").build());
            } else {
                throw new RuntimeException("Wrong ID, Please check the ID..!");
            }

         } catch (SQLException | ClassNotFoundException e) {
             resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
             resp.getWriter().print(messageUtil.buildJsonObject("Error", e.getLocalizedMessage(), "").build());
         }
    }
}