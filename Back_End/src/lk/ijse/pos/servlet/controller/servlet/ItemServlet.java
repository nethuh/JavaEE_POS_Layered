package lk.ijse.pos.servlet.controller.servlet;

import lk.ijse.pos.servlet.bo.BOFactory;
import lk.ijse.pos.servlet.bo.custom.ItemBO;
import lk.ijse.pos.servlet.dto.ItemDTO;
import lk.ijse.pos.servlet.util.MessageUtil;

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
        try {
            forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi","root","1234");
            ArrayList<ItemDTO> items = itemBO.getAllItems(connection);
                for (ItemDTO item : items){
                    JsonObjectBuilder Jitem = Json.createObjectBuilder();
                    Jitem.add("code",item.getCode());
                    Jitem.add("name",item.getName());
                    Jitem.add("Qty",item.getQty());
                    Jitem.add("Price",item.getPrice());
                    allItems.add(Jitem.build());
                }
            resp.setStatus(200);
                resp.getWriter().print(messageUtil.buildJsonObject("OK","Successfully Loaded..!",allItems).build());


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
        String unitPrice = req.getParameter("unitPrice");
        String qtyOnHand = req.getParameter("qtyOnHand");
        resp.addHeader("Access-Control-Allow-Origin","*");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("insert into Item values(?,?,?,?)");
            pstm.setObject(1,code);
            pstm.setObject(2,description);
            pstm.setObject(3,unitPrice);
            pstm.setObject(4,qtyOnHand);
            boolean b = pstm.executeUpdate() > 0;
            if (b){
                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state","Ok");
                responseObject.add("message","Successfully added..!");
                responseObject.add("data","");
                resp.getWriter().print(responseObject.build());
            }
        } catch (ClassNotFoundException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Ok");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
//            resp.setStatus(500);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(error.build());
        }catch (SQLException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
//            resp.setStatus(400);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(error.build());
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
            }else {
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
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject item = reader.readObject();
        String code = item.getString("code");
        String description = item.getString("description");
        String unitPrice = item.getString("unitPrice");
        String qtyOnHand = item.getString("qtyOnHand");
        resp.addHeader("Access-Control-Allow-Origin","*");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("update Item set ItemName=?,UnitPrice=?,ItemQty=? where ItemCode=?");
            pstm.setObject(4,code);
            pstm.setObject(1,description);
            pstm.setObject(2,unitPrice);
            pstm.setObject(3,qtyOnHand);
            boolean b = pstm.executeUpdate() > 0;
            if (b){
                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state","Ok");
                responseObject.add("message","Successfully Updated..!");
                responseObject.add("data","");
                resp.getWriter().print(responseObject.build());
            }else{
                throw new RuntimeException("Wrong ID, Please check the ID..!");
            }

        } catch (RuntimeException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());
        }catch (ClassNotFoundException | SQLException e){
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","DELETE,PUT");
        resp.addHeader("Access-Control-Allow-Headers","content-type");
    }
}