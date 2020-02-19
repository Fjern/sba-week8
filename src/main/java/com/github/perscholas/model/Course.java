package com.github.perscholas.model;

// TODO - Annotate and Implement respective interface and define behaviors
public class Course implements CourseInterface{
    private Integer id;
    private String name;
    private String instructor;

    public Course(Integer id, String name, String instructor) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getInstructor() {
        return instructor;
    }

    @Override
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
