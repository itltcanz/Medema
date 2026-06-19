package dev.itltcanz.medema.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DbConfig {

  public static final String DB_URL = "jdbc:sqlite:medema.db";
  public static final String CHANGELOG_FILE = "db/changelog/db.changelog-master.yml";



}
