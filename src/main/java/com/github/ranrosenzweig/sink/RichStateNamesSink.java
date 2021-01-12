package com.github.ranrosenzweig.sink;

import com.github.ranrosenzweig.common.AppConfig;
import com.github.ranrosenzweig.model.StateNames;

import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class RichStateNamesSink extends RichSinkFunction<StateNames> {

  String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
  String appConfigPath = rootPath + "app.properties";
  Properties appProps = new Properties();

  int batchCount;

  private Connection connection;

  private PreparedStatement statement;

  @Override
  public void open(Configuration parameters) throws Exception {
    appProps.load(new FileInputStream(appConfigPath));

    // JDBC connection information
    final String USERNAME = appProps.getProperty(AppConfig.POSTGRES_USERNAME_CONFIG);
    final String PASSWORD = appProps.getProperty(AppConfig.POSTGRES_PASSWORD_CONFIG);
    final String driverClass = appProps.getProperty(AppConfig.POSTGRES_DRIVER_CLASS_CONFIG);
    final String HOST = appProps.getProperty(AppConfig.POSTGRES_HOSTNAME_CLASS_CONFIG);
    final String PORT = appProps.getProperty(AppConfig.POSTGRES_PORT_CLASS_CONFIG);
    final String DATABASE = appProps.getProperty(AppConfig.POSTGRES_DATABASE_CLASS_CONFIG);
    final String URL = HOST + ":" + PORT + "/" + DATABASE;
    // Load jdbc driver
    Class.forName(driverClass);
    // Get the connection to the database
    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    String sql = "INSERT INTO state_names (id, name,year,gender,state,count)"
            + "values (?, ?, ?, ?, ?, ?) ";
    statement = connection.prepareStatement(sql);
    super.open(parameters);
  }

  @Override
  public void invoke(StateNames stateNames) {

    final Integer BATCH_COUNT = Integer.valueOf(appProps.getProperty(AppConfig.BATCH_CONFIG));

    try {
      statement.setInt(1, stateNames.getId());
      statement.setString(2, stateNames.getName());
      statement.setInt(3, stateNames.getYear());
      statement.setString(4, stateNames.getGender());
      statement.setString(5, stateNames.getState());
      statement.setInt(6, stateNames.getCount());
      statement.addBatch();
      batchCount++;

      //bulk sink
      if (batchCount == BATCH_COUNT) {
        statement.executeBatch();
        batchCount = 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() throws Exception {
    if (statement != null) {
      statement.close();
    }
    if (connection != null) {
      connection.close();
    }
    super.close();
  }
}
