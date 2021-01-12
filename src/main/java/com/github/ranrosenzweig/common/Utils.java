package com.github.ranrosenzweig.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public  class Utils {

    public static void printHeader(String msg) {

        System.out.println("\n**************************************************************");
        System.out.println(msg);
        System.out.println("---------------------------------------------------------------");
    }

    public static void truncateTable(String tableName) throws SQLException {

        String url = "jdbc:postgresql://localhost:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","postgres");
        props.setProperty("ssl","false");
        Connection conn = DriverManager.getConnection(url, props);

        Statement st = conn.createStatement();
        System.out.println("TRUNCATE TABLE " + tableName);
        st.execute("TRUNCATE TABLE " + tableName);
        System.out.println("TABLE " + tableName + "has been truncated");
        st.close();

    }
}
