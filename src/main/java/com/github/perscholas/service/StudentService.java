package com.github.perscholas.service;

import com.github.perscholas.DatabaseConnection;
import com.github.perscholas.dao.StudentDao;
import com.github.perscholas.model.CourseInterface;
import com.github.perscholas.model.Student;
import com.github.perscholas.model.StudentInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// TODO - Implement respective DAO interface
public class StudentService implements StudentDao {

    private final DatabaseConnection dbc;
    private final CourseService courseService;
    private final RegisterService registerService;

    public StudentService(DatabaseConnection dbc, CourseService courseService, RegisterService registerService) {
        this.dbc = dbc;
        this.courseService = courseService;
        this.registerService = registerService;
    }

    public StudentService() {
        this(DatabaseConnection.MANAGEMENT_SYSTEM, new CourseService(), new RegisterService());
    }

    @Override
    public List<StudentInterface> getAllStudents() {
        ResultSet result = dbc.executeQuery("SELECT * FROM Student");
        List<StudentInterface> list = new ArrayList<>();
        try {
            while (result.next()) {
                String studentEmail = result.getString("email");
                String name = result.getString("name");
                String password = result.getString("password");
                StudentInterface student = new Student(studentEmail, name, password);
                list.add(student);
            }
        } catch (SQLException se) {
            throw new Error(se);
        }

        return list;
    }

    @Override
    public StudentInterface getStudentByEmail(String studentEmail) {
        return getAllStudents()
                .stream()
                .filter(student -> student.getEmail().equals(studentEmail))
                .findFirst()
                .get();
    }

    @Override
    public Boolean validateStudent(String studentEmail, String password) {
        return getStudentByEmail(studentEmail).getPassword().equals(password);
    }

    @Override
    public void registerStudentToCourse(String studentEmail, int courseId) {
        List<CourseInterface> courses = courseService.getAllCourses();
        CourseInterface thisCourse = courses.stream()
                .filter(course -> course.getId().equals(courseId))
                .findFirst()
                .get();
        dbc.executeStatement(new StringBuilder()
                .append("insert into Register (email, id, name, instructor) values ('")
                .append(studentEmail)
                .append("','")
                .append(thisCourse.getId())
                .append("','")
                .append(thisCourse.getName())
                .append("','")
                .append(thisCourse.getInstructor())
                .append("');")
                .toString());

    }

    @Override
    public List<CourseInterface> getStudentCourses(String studentEmail) {
        return registerService.getStudentsRegistry(studentEmail);
    }
}
