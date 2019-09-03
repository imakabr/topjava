package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final RowMapper<Role> ROLE_ROW_MAPPER = (rs, rowNum) -> Role.valueOf(rs.getString("role"));

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        List<Role> roles = List.copyOf(user.getRoles());
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(newKey.intValue(), roles);
            return user;
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        deleteRoles(user.getId());
        insertRoles(user.getId(), roles);
        return user;
    }

    @Transactional
    public void deleteRoles(int id) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
    }

    @Transactional
    public void insertRoles(int id, List<Role> roles) {
        jdbcTemplate.batchUpdate("insert into user_roles (user_id, role) values(?,?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, id);
                        ps.setString(2, roles.get(i).name());
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                }
        );
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u WHERE u.id=?", getCustomUserRowMapper(), id);
        return DataAccessUtils.singleResult(users);
//        List<User> users = jdbcTemplate.query("SELECT * FROM users u WHERE u.id=?", USER_ROW_MAPPER, id);
//        List<Role> roles = jdbcTemplate.query("SELECT * FROM user_roles ur WHERE ur.user_id=?", ROLE_ROW_MAPPER, id);
//        User user = DataAccessUtils.singleResult(users);
//        user.setRoles(roles);
//        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users u WHERE email=?", getCustomUserRowMapper(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        class RoleWrapper {
            private int id;
            private Role role;

            private int getId() {
                return id;
            }

            private Role getRole() {
                return role;
            }
        }
        List<RoleWrapper> roles = jdbcTemplate.query("SELECT * FROM user_roles", (rs, rowNum) -> {
            RoleWrapper roleWrapper = new RoleWrapper();
            roleWrapper.role = Role.valueOf(rs.getString("role"));
            roleWrapper.id = rs.getInt("user_id");
            return roleWrapper;
        });
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", USER_ROW_MAPPER);
        Map<Integer, List<Role>> groupingByRoles = roles.stream()
                .collect(Collectors.groupingBy(RoleWrapper::getId, Collectors.mapping(RoleWrapper::getRole, Collectors.toList())));
        users.forEach(user -> user.setRoles(groupingByRoles.get(user.getId())));
        return users;
    }

    private RowMapper<User> getCustomUserRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRegistered(rs.getDate("registered"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setCaloriesPerDay(rs.getInt("calories_per_day"));
            List<Role> roles = jdbcTemplate.query("SELECT * FROM user_roles ur WHERE ur.user_id=?", ROLE_ROW_MAPPER, user.getId());
            user.setRoles(roles);
            return user;
        };
    }
}
