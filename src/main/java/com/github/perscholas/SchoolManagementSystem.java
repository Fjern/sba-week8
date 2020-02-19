package com.github.perscholas;

import com.github.perscholas.dao.StudentDao;
import com.github.perscholas.model.CourseInterface;
import com.github.perscholas.service.CourseService;
import com.github.perscholas.service.StudentService;
import com.github.perscholas.utils.IOConsole;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SchoolManagementSystem implements Runnable {
    private static final IOConsole console = new IOConsole();

    @Override
    public void run() {
        String smsDashboardInput = getSchoolManagementSystemDashboardInput();
        String studentDashboardInput = "";
        do {
            try {
                if ("login".equals(smsDashboardInput)) {
                    StudentDao studentService = new StudentService();
                    String studentEmail = console.getStringInput("Enter your email:");
                    String studentPassword = console.getStringInput("Enter your password:");
                    Boolean isValidLogin = studentService.validateStudent(studentEmail, studentPassword);
                    if (isValidLogin) {
                        studentDashboardInput = getStudentDashboardInput();
                        if ("register".equals(studentDashboardInput)) {
                            Integer courseId = getCourseRegistryInput();
                            studentService.registerStudentToCourse(studentEmail, courseId);
                        }
                        if ("logout".equals(smsDashboardInput)) {
                            break;
                        }
                        studentDashboardInput = getViewInput();
                        if("view".equals(studentDashboardInput)){
                            List<CourseInterface> courses = studentService.getStudentCourses(studentEmail);
                            System.out.println("Your Courses");
                            courses.forEach(System.out::println);
                        }
                        if ("logout".equals(smsDashboardInput)) {
                            break;
                        }
                    }
                } else {
                    break;
                }
            } catch (NullPointerException e) {
                System.out.println("nope");
            }
        } while (true);
    }

    private String getViewInput() {
        return console.getStringInput(new StringBuilder()
        .append("View courses?")
        .append("\n[View], [logout]")
        .toString());
    }

    private String getSchoolManagementSystemDashboardInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the School Management System Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ login ], [ logout ]")
                .toString());
    }

    private String getStudentDashboardInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the Student Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ register ], [ logout]")
                .toString());
    }


    private Integer getCourseRegistryInput() {
        CourseService courseService = new CourseService();
        List<Integer> listOfCoursesIds = courseService.getAllCourses().stream().map(c -> c.getId()).collect(Collectors.toList());
        List<String> namesOfCourses = courseService.getAllCourses()
                .stream().map(c->c.getName())
                .collect(Collectors.toList());
        return console.getIntegerInput(new StringBuilder()
                .append("Welcome to the Course Registration Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n" + namesOfCourses.toString())
                .append("\n\t" + listOfCoursesIds.toString())
                .toString());
    }
}
