package lk.ijse.pos.servlet.controller.servlet;

import lk.ijse.pos.servlet.bo.BOFactory;
import lk.ijse.pos.servlet.bo.custom.ItemBO;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonArrayBuilder allItems = Json.createArrayBuilder();
        try (Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
              ArrayList<ItemDTO> items = itemBO.getAllItems(connection);
            for (ItemDTO item : items) {
                JsonObjectBuilder JsonItem = Json.createObjectBuilder();
                JsonItem.add("code", item.getCode());
                JsonItem.add("name", item.getName());
                JsonItem.add("Qty", item.getQtyOnHand());
                JsonItem.add("Price", item.getPrice());
                allItems.add(JsonItem.build());
            }
            resp.setStatus(200);
            resp.getWriter().print(allItems.build());

//            case "search":
//                String code1 = req.getParameter("ItemCode");
//                PreparedStatement pstm2 = connection.prepareStatement("select * from item where ItemCode=?");
//                pstm2.setObject(1, code1);
//                ResultSet rst2 = pstm2.executeQuery();
//                resp.addHeader("Access-Control-Allow-Origin", "*");
//
//                JsonObjectBuilder objectBuilder1 = Json.createObjectBuilder();
//                if (rst2.next()) {
//                    String ids = rst2.getString(1);
//                    String description = rst2.getString(2);
//                    String unitPrice = rst2.getString(3);
//                    String qty = rst2.getString(4);
//
//
//                    objectBuilder1.add("code", ids);
//                    objectBuilder1.add("description", description);
//                    objectBuilder1.add("unitPrice", unitPrice);
//                    objectBuilder1.add("qty", qty);
//
//
//                }
//                resp.setContentType("application/json");
//                resp.getWriter().print(objectBuilder1.build());
//
//                break;

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
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");
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
         try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");
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