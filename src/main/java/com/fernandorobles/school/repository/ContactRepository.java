package com.fernandorobles.school.repository;

import com.fernandorobles.school.model.Contact;
import com.fernandorobles.school.rowmappers.ContactRowMapper;
import com.fernandorobles.school.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ContactRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveContactMsg(Contact contact){
        String sql = "INSERT INTO CONTACT_MSG (NAME,MOBILE_NUM,EMAIL,SUBJECT,MESSAGE,STATUS," +
                "CREATED_AT,CREATED_BY) VALUES (?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(
                sql,
                contact.getName(),
                contact.getMobileNum(),
                contact.getEmail(),
                contact.getSubject(),
                contact.getMessage(),
                contact.getStatus(),
                contact.getCreatedAt(),
                contact.getCreatedBy()
        );
    }

    public List<Contact> findMessagesWithStatus(String status) {
//        String query = "SELECT * FROM CONTACT_MSG WHERE STATUS = ?";
        String query = "SELECT * FROM CONTACT_MSG WHERE STATUS = '" + status + "'";
        return jdbcTemplate.query(query,new ContactRowMapper());
//        return jdbcTemplate.query(query, new PreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps) throws SQLException {
//                ps.setString(1, status);
//            }
//        }, new ContactRowMapper());
    }

    public int updateMsgStatus(int contactId, String status, String updatedBy) {

        String query = "UPDATE CONTACT_MSG SET STATUS = ?, UPDATED_BY = ?, UPDATED_AT = ? WHERE CONTACT_ID = ?";
        return jdbcTemplate.update(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, status);
                ps.setString(2, updatedBy);
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                ps.setInt(4, contactId);
            }
        });
    }
}
