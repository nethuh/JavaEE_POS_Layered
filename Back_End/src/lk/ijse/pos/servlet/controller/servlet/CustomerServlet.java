package lk.ijse.pos.servlet.controller.servlet;

import lk.ijse.pos.servlet.bo.BOFactory;
import lk.ijse.pos.servlet.bo.custom.CustomerBO;
import lk.ijse.pos.servlet.dto.CustomerDTO;
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
@WebServlet (urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    private final CustomerBO customerBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.CUSTOMER);
    private final MessageUtil messageUtil = new MessageUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");
                JsonArrayBuilder allCustomers = Json.createArrayBuilder();
                ArrayList<CustomerDTO> all = customerBO.getAllCustomers(connection);

                for (CustomerDTO customerDTO: all){
                        JsonObjectBuilder customer = Json.createObjectBuilder();

                        customer.add("id", customerDTO.getCusId());
                        customer.add("name", customerDTO.getCusName());
                        customer.add("address", customerDTO.getAddress());
                        customer.add("salary", customerDTO.getSalary());
                        allCustomers.add(customer.build());
                    }
                    resp.setContentType("application/json");
                    resp.getWriter().print(allCustomers.build());
//                break;
//                    case "search":
//                        PreparedStatement pstm3 = connection.prepareStatement("select * from customer where cusID=?");
//                        pstm3.setObject(1, req.getParameter("cusID"));
//                        ResultSet rst3 = pstm3.executeQuery();
//                        resp.addHeader("Access-Control-Allow-Origin", "*");
//
//                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
//                        if (rst3.next()) {
//                            String id = rst3.getString(1);
//                            String name = rst3.getString(2);
//                            String salary= rst3.getString(3);
//                            String address  = rst3.getString(4);
//
//                            objectBuilder.add("id", id);
//                            objectBuilder.add("name", name);
//                            objectBuilder.add("salary", salary);
//                            objectBuilder.add("address", address);
//
//                        }
//                        resp.setContentType("application/json");
//                        resp.getWriter().print(objectBuilder.build());
//                        break;
//                }

            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("id");
        String cusName = req.getParameter("name");
        String cusAddress = req.getParameter("address");
        double cusSalary = Double.parseDouble(req.getParameter("salary"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");
            if (customerBO.saveCustomer(connection,new CustomerDTO(cusID,cusName,cusAddress,cusSalary))){
                resp.setStatus(200);
                resp.getWriter().print(messageUtil.buildJsonObject("OK","Successfully Added..!","").build());
            }

        } catch (SQLException | ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(messageUtil.buildJsonObject("Error", e.getLocalizedMessage(), "").build());

        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");
            if (customerBO.deleteCustomer(connection,cusID)) {
                resp.setStatus(200);
                resp.getWriter().print(messageUtil.buildJsonObject("Ok","Successfully Deleted..!","").build());
            }else {
                throw new RuntimeException("There is no Customer for that ID..!");
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
        JsonObject customer = Json.createReader(req.getReader()).readObject();

        String cusID = customer.getString("cusID");
        String cusName = customer.getString("cusName");
        String address = customer.getString("cusAddress");
        double salary = Double.parseDouble(customer.getString("cusSalary"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi", "root", "1234");

            if (customerBO.updateCustomer(connection,new CustomerDTO(cusID,cusName,address,salary))){

                resp.getWriter().print(messageUtil.buildJsonObject("Ok","Successfully Updated","").build());
            }else{
                throw new RuntimeException("Wrong ID, Please check the ID..!");
            }

        } catch (SQLException | ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(messageUtil.buildJsonObject("Error", e.getLocalizedMessage(), "").build());
        }
    }
}