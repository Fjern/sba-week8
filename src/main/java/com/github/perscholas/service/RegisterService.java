package com.github.perscholas.service;

import com.github.perscholas.DatabaseConnection;
import com.github.perscholas.dao.RegisterDao;
import com.github.perscholas.model.Course;
import com.github.perscholas.model.CourseInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegisterService implements RegisterDao {
    private final DatabaseConnection dbc;

    public RegisterService(DatabaseConnection dbc) {
        this.dbc = dbc;
    }

    public RegisterService() {
        this(DatabaseConnection.MANAGEMENT_SYSTEM);
    }
    @Override
    public List<CourseInterface> getStudentsRegistry(String email) {
        ResultSet result = dbc.executeQuery("SELECT * FROM Register WHERE email = '"+email+"'");
        List<CourseInterface> list = new ArrayList<>();
        try {
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                String instructor = result.getString("instructor");
                CourseInterface course = new Course(id, name, instructor);
                list.add(course);
            }
        } catch(SQLException se) {
            throw new Error(se);
        }

        return list;
    }
}
