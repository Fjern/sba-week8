package com.github.perscholas.dao;

import com.github.perscholas.model.CourseInterface;

import java.util.List;

public interface RegisterDao {
    List<CourseInterface> getStudentsRegistry(String email);
}
