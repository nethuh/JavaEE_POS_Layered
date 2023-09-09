package lk.ijse.pos.servlet.util;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import static java.lang.Class.forName;
@WebServlet (urlPatterns = {"/customer"})
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");
                String option = req.getParameter("option");

                switch (option){
                    case "GetAll":
                        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer");
                        ResultSet rst = pstm.executeQuery();
                        JsonArrayBuilder allCustomers = Json.createArrayBuilder();
                        resp.addHeader("Access-Control-Allow-Origin","*");

                while (rst.next()){
                    JsonObjectBuilder customer = Json.createObjectBuilder();
                    customer.add("id", rst.getString("cusID"));
                    customer.add("name",rst.getString("cusName"));
                    customer.add("address",rst.getString("cusAddress"));
                    customer.add("salary",rst.getDouble("cusSalary"));
                    allCustomers.add(customer.build());
                }
                resp.setContentType("application/json");
                resp.getWriter().print(allCustomers.build());

                break;
                    case "search":
                        PreparedStatement pstm2 = connection.prepareStatement("select * from customer where cusID=?");
                        pstm2.setObject(1,req.getParameter("cusID"));
                        ResultSet rst2 = pstm2.executeQuery();
                        JsonArrayBuilder allCustomer = Json.createArrayBuilder();
                        resp.addHeader("Access-Control-Allow-Origin","*");

                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        if (rst2.next()){
                            objectBuilder.add("id",rst2.getString(1));
                            objectBuilder.add("name",rst2.getString(2));
                            objectBuilder.add("address", rst2.getString(3));
                            objectBuilder.add("salary",rst2.getDouble(4));
                            allCustomer.add(objectBuilder.build());
                        }
                        resp.setContentType("application/jason");
                        resp.getWriter().print(objectBuilder.build());
                        break;
                }

            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("id");
        String cusName = req.getParameter("name");
        String cusAddress = req.getParameter("address");
        String cusSalary = req.getParameter("salary");
        resp.addHeader("Access-Control-Allow-Origin","*");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("insert into Customer values(?,?,?,?)");
            pstm.setObject(1,cusID);
            pstm.setObject(2,cusName);
            pstm.setObject(3,cusAddress);
            pstm.setObject(4,cusSalary);

            if (pstm.executeUpdate() > 0){
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
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(error.build());

        } catch (SQLException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(error.build());

        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        resp.setContentType("application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("delete from Customer where cusID=?");
            pstm.setObject(1,cusID);
            boolean b = pstm.executeUpdate() > 0;
            if (b) {
                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state","Ok");
                rjo.add("message","Successfully Deleted..!");
                rjo.add("data","");
                resp.getWriter().print(rjo.build());
            }else {
                throw new RuntimeException("There is no Customer for that ID..!");
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

    //    query string
//    JSON
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();
        String cusID = customer.getString("cusID");
        String cusName = customer.getString("cusName");
        String cusAddress = customer.getString("cusAddress");
        String cusSalary = customer.getString("cusSalary");
        resp.addHeader("Access-Control-Allow-Origin","*");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("update Customer set cusName=?,cusAddress=?,cusSalary=? where cusID=?");
            pstm.setObject(4,cusID);
            pstm.setObject(1,cusName);
            pstm.setObject(2,cusAddress);
            pstm.setObject(3,cusSalary);
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