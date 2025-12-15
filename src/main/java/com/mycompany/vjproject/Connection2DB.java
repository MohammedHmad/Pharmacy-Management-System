package com.mycompany.vjproject;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Connection2DB {
    
    public static Connection ConneectorDB(){  
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = null;
            String db_url="jdbc:mysql://localhost:3306/pharmacyDB";
            String db_username="root";
            String db_password="okayletsGO#55";
            conn = DriverManager.getConnection(db_url,db_username,db_password);

            return conn;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
}
