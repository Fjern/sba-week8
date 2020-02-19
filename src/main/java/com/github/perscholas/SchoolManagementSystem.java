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
        String studentDashboardInput = "";
        do {
            String smsDashboardInput = getSchoolManagementSystemDashboardInput();
            studentDashboardInput = "";
            try {
                if ("login".equals(smsDashboardInput)) {
                    StudentDao studentService = new StudentService();
                    String studentEmail = console.getStringInput("Enter your email:");
                    String studentPassword = console.getStringInput("Enter your password:");
                    Boolean isValidLogin = studentService.validateStudent(studentEmail, studentPassword);
                    if (isValidLogin) {
                    do{
                        studentDashboardInput = getStudentDashboardInput();
                        if ("register".equals(studentDashboardInput)) {
                            Integer courseId = getCourseRegistryInput();
                            studentService.registerStudentToCourse(studentEmail, courseId);
                        }
//                        studentDashboardInput = getViewInput();
                        if ("view".equals(studentDashboardInput)) {
                            List<CourseInterface> courses = studentService.getStudentCourses(studentEmail);
                            if(courses.size()==0){
                                System.out.println("You are not currently registered for any classes.\n");
                            }
                            else{
                                System.out.println("Your Courses");
                                courses.forEach(System.out::println);
                                System.out.println("\n");
                            }
                        }
                    }while(!studentDashboardInput.equals("logout"));
                    }
                    else {
                        System.out.println("Try Again");
                    }
                } else {
                    break;
                }
            } catch (NullPointerException e) {
                System.out.println("nope");
            }
        } while (!studentDashboardInput.equals("Exit"));
    }

    private String getSchoolManagementSystemDashboardInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the School Management System Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ login ], [ exit ]")
                .toString());
    }

    private String getStudentDashboardInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the Student Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ register ], [view], [ logout]")
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
                .append("\nFrom here, you can select any of the following options:\n")
                .append(makeList(listOfCoursesIds,namesOfCourses))
                .toString());
    }
    public String makeList(List<Integer> nums, List<String> classes){
        StringBuilder build = new StringBuilder();
        for(int x = 0;x<nums.size();x++){
            build.append(nums.get(x)+" - "+classes.get(x)+"\n");
        }
        return build.toString();
    }
}



