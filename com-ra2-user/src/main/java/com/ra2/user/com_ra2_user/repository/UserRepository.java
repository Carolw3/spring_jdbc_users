package com.ra2.user.com_ra2_user.repository;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ra2.user.com_ra2_user.model.User;

@Repository

public class UserRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;


    private static final class UserRowMapper implements RowMapper<User>{

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setDescripcion(rs.getString("descripcion"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            if(rs.getTimestamp("ultimAcces") != null){
                user.setUltimAcces(rs.getTimestamp("ultimAcces").toLocalDateTime());
            }
            if(rs.getTimestamp("dataCreated") != null){
                user.setDataCreated(rs.getTimestamp("dataCreated").toLocalDateTime());
            }
            if(rs.getTimestamp("dataUpdated") != null){
                user.setDataUpdated(rs.getTimestamp("dataUpdated").toLocalDateTime());
            }
            

            return user;
        }
        
    }
    // Punt 5 . Retorna tots els usuaris
    public List<User> findAll(){
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }
    // Punt 6. Retorna un usuari segons la seva id
    public User findById(Long id){
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    }

    // Punt 7. Modifica un usuari buscant per la seva id
    public int updateUser(User user, Long id) {
        String sql = """
            UPDATE users
            SET name = ?, descripcion = ?, email = ?, password = ?, dataUpdated = ?
            WHERE id = ?
        """;

        java.time.LocalDateTime now = java.time.LocalDateTime.now(); //Agafa el moment actual per marcarlo com a data de la
        Timestamp time = Timestamp.valueOf(now);                     //ultima modificacio a la informacio de l'usuari en laa bbdd.
        return jdbcTemplate.update(sql,
                user.getName(),
                user.getDescripcion(),
                user.getEmail(),
                user.getPassword(),
                time,
                id
        );
    }

    // 8. Modifica nomes el nom de un suari nuscat per id i nom
    public int patchUser(String name, Long id) {
        String sql = """
            UPDATE users
            SET name = ?, dataUpdated = ?
            WHERE id = ?
        """;

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        Timestamp time = Timestamp.valueOf(now);
        return jdbcTemplate.update(sql,
                name,
                time,
                id
        );
    }


    public int deleteUser(Long id){
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }


    // Punt 4. Crea un nou usuari
    public int insertUser(User user){
            String sql = """
                    INSERT INTO users(name, descripcion,  email, password, ultimAcces, dataCreated, dataUpdated) VALUES (?,?,?,?,?,?,?);
                    """;

            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);

            return jdbcTemplate.update(sql,
            user.getName(),
            user.getDescripcion(),
            user.getEmail(),
            user.getPassword(),
            null,                 //Es null perque no posarem la data d'ultim acces la primera vegada que ho creem
            timestamp,            //Es la data en el moment de crear
            timestamp            // //Es la data de l'ultim canvi
            );
        }

}
