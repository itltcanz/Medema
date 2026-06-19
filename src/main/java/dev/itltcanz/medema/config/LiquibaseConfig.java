package dev.itltcanz.medema.config;

import static dev.itltcanz.medema.constant.DbConfig.CHANGELOG_FILE;
import static dev.itltcanz.medema.constant.DbConfig.DB_URL;

import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

@Singleton
@SuppressWarnings("unused")
public class LiquibaseConfig {

  public LiquibaseConfig() throws SQLException, LiquibaseException {
    runLiquibase();
  }

  public void runLiquibase() throws SQLException, LiquibaseException {
    try (Connection connection = DriverManager.getConnection(DB_URL)) {
      // Включаем поддержку внешних ключей в SQLite для текущего соединения
      try (Statement statement = connection.createStatement()) {
        statement.execute("PRAGMA foreign_keys = ON;");
      }

      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));

      try (Liquibase liquibase = new Liquibase(
          CHANGELOG_FILE,
          new ClassLoaderResourceAccessor(),
          database
      )) {
        liquibase.update("");
      }
    }
  }

}
