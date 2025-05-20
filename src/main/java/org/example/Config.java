package org.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        try (InputStream is = Config.class.getResourceAsStream("/" + filename)) {
            if (is == null) {
                throw new FileNotFoundException("Resource file not found: " + filename);
            }
            props.load(is);
        }

        return new Config(
            props.getProperty("db.driver", "org.postgresql.Driver"),
            props.getProperty("db.url"),
            props.getProperty("db.user"),
            props.getProperty("db.password")
        );
    }
}
