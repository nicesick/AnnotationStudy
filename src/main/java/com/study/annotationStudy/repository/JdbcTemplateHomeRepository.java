package com.study.annotationStudy.repository;

import com.study.annotationStudy.annotation.DynamicDataSource;
import com.study.annotationStudy.dto.Guest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcTemplateHomeRepository implements HomeRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplateHomeRepository() {
        jdbcTemplate = new JdbcTemplate();
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    @DynamicDataSource
    public List<Guest> findAll() {
        System.out.println("findAll " + this.jdbcTemplate);

        List<Guest> guests = jdbcTemplate.query("SELECT * FROM GUEST", memberRowMapper());
        return guests;
    }

    private RowMapper<Guest> memberRowMapper() {
        return (rs, rowNum) -> {
            Guest item = new Guest();

            item.setId(rs.getInt("id"));
            item.setName(rs.getString("name"));

            return item;
        };
    }
}
