package com.study.annotationStudy.repository;

import com.study.annotationStudy.dto.Guest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface HomeRepository {
    List<Guest> findAll();
    JdbcTemplate getJdbcTemplate();
}
