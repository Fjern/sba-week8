package com.github.perscholas;

import com.github.perscholas.utils.DirectoryReference;
import com.github.perscholas.utils.FileReader;
import org.mariadb.jdbc.Driver;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class JdbcConfigurator {
    static {
        // Attempt to register JDBC Driver
        try {
            DriverManager.registerDriver(Driver.class.newInstance());
        } catch (InstantiationException | IllegalAccessException | SQLException e1) {
            throw new Error(e1);
        }
    }

    private static final DatabaseConnection dbc = DatabaseConnection.MANAGEMENT_SYSTEM;

    public static void initialize() {
        dbc.drop();
        dbc.create();
        dbc.use();
        createTable("courses.create-table.sql");
        createTable("students.create-table.sql");
        createTable("register.create-table.sql");
        populateTable("courses.populate-table.sql");
        populateTable("students.populate-table.sql");
    }

    private static void createTable(String fileName) {
        File creationStatementFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory(fileName);
        FileReader fileReader = new FileReader(creationStatementFile.getAbsolutePath());
        String creationStatement = fileReader.toString();
        dbc.executeStatement(creationStatement);
    }

    private static void populateTable(String fileName){
        File populateFile = DirectoryReference.RESOURCE_DIRECTORY.getFileFromDirectory(fileName);
        FileReader fileReader = new FileReader(populateFile.getAbsolutePath());
        String[] seeds = fileReader.toString().split(";");
        Arrays.stream(seeds).forEach(DatabaseConnection.MANAGEMENT_SYSTEM::executeStatement);
    }
}
