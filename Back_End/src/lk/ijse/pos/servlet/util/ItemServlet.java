package lk.ijse.pos.servlet.util;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import static java.lang.Class.forName;
@WebServlet (urlPatterns = {"/item"})
public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posapi","root","1234");
             String option = req.getParameter("option");
        switch (option) {
            case "getAll":
                PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Item");
                ResultSet rst = pstm.executeQuery();
                JsonArrayBuilder allItems = Json.createArrayBuilder();
                resp.addHeader("Access-Control-Allow-Origin","*");

                while (rst.next()){
                    JsonObjectBuilder item = Json.createObjectBuilder();
                    item.add("code",rst.getString("ItemCode"));
                    item.add("name",rst.getString("ItemName"));
                    item.add("Qty",rst.getInt("ItemQty"));
                    item.add("Price",rst.getDouble("UnitPrice"));
                    allItems.add(item.build());
                }
                resp.setContentType("application/json");
                resp.getWriter().print(allItems.build());
break;

        }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }



    }
}