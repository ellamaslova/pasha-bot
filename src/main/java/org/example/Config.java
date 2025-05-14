package org.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@RequiredArgsConstructor
@Getter
public class Config {
    private final String dbDriver;
    private final String dbJdbcUrl;
    private final String dbUser;
    private final String dbPassword;

    static Config readConfig(String filename) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(filename)) {
            props.load(fis);
        }

        return new Config(
            props.getProperty("db.driver", "org.postgresql.Driver"),
            props.getProperty("db.user"),
            props.getProperty("db.password"),
            props.getProperty("db.url")
        );
    }
}
