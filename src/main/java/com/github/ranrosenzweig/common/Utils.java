package com.github.ranrosenzweig.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class Utils {
  static Connection conn = null;
  static Statement st = null;

  public static void printHeader(String msg) {

    System.out.println("\n**************************************************************");
    System.out.println(msg);
    System.out.println("---------------------------------------------------------------");
  }

  public static void truncateTable(String tableName) throws IOException {

    String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    String appConfigPath = rootPath + "app.properties";
    Properties appProps = new Properties();
    appProps.load(new FileInputStream(appConfigPath));
    final String USERNAME = appProps.getProperty(AppConfig.POSTGRES_USERNAME_CONFIG);
    final String PASSWORD = appProps.getProperty(AppConfig.POSTGRES_PASSWORD_CONFIG);
    final String HOST = appProps.getProperty(AppConfig.POSTGRES_HOSTNAME_CLASS_CONFIG);
    final String PORT = appProps.getProperty(AppConfig.POSTGRES_PORT_CLASS_CONFIG);
    final String DATABASE = appProps.getProperty(AppConfig.POSTGRES_DATABASE_CLASS_CONFIG);
    final String URL = HOST + ":" + PORT + "/" + DATABASE;
    String sql = "";
    if (tableName.equalsIgnoreCase("public.national_names"))
        sql = "TRUNCATE TABLE public.national_names";
    if(tableName.equalsIgnoreCase("public.state_names"))
      sql = "TRUNCATE TABLE public.state_names";

    try {
      conn  = DriverManager.getConnection(URL, USERNAME, PASSWORD);
      st = conn.createStatement();
      st.executeQuery(sql);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        st.close();
      } catch (Exception e) { /* ignored */ }
      try {
        conn.close();
      } catch (Exception e) { /* ignored */ }

    }
  }
}
