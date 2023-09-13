package lk.ijse.pos.servlet.controller.listerners;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyListerners implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/posapi");
        bds.setPassword("1234");
        bds.setUsername("root");
        bds.setMaxTotal(5);
        bds.setInitialSize(5);

        servletContext.setAttribute("dbcp",bds);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
