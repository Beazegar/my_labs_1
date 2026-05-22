package com.buevich.labs.database.repository;

import com.buevich.labs.database.entity.Team;
import com.buevich.labs.database.factory.ConnectionFactory;
import com.buevich.labs.database.errors.LabotoryRuntimeException;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;

public class TeamRepositoryImpl implements TeamRepository {
    private final ConnectionFactory connectionFactory;
    private final String schema;
    private final String table;

    public TeamRepositoryImpl(ConnectionFactory connectionFactory, String schema, String table) throws Exception {
        this.connectionFactory = connectionFactory;
        this.schema = schema;
        this.table = table;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws Exception {
        String createTableSql = """
                CREATE TABLE IF NOT EXISTS %s.%s (
                    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    name VARCHAR(100) NOT NULL UNIQUE,
                    score INT NOT NULL DEFAULT 0
                )
                """.formatted(schema, table);

        try (Connection conn = connectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException e) {
            throw new LabotoryRuntimeException("Failed to create table", e);
        }
    }

    @Override
    public int save(Team team) throws Exception {
        String sql = "INSERT INTO " + schema + "." + table + " (name, score) VALUES (?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, team.getName());
            ps.setInt(2, team.getScore());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new LabotoryRuntimeException("Creating team failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    team.setId(id);
                    return id;
                } else {
                    throw new LabotoryRuntimeException("Creating team failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new LabotoryRuntimeException("Failed to save team", e);
        }
    }

    @Override
    public Team findById(int id) throws Exception {
        String sql = "SELECT id, name, score FROM " + schema + "." + table + " WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeam(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new LabotoryRuntimeException("Failed to find team by id: " + id, e);
        }
    }

    @Override
    public Team findByName(String name) throws Exception {
        String sql = "SELECT id, name, score FROM " + schema + "." + table + " WHERE name = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeam(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new LabotoryRuntimeException("Failed to find team by name: " + name, e);
        }
    }

    @Override
    public List<Team> findAll() throws Exception {
        String sql = "SELECT id, name, score FROM " + schema + "." + table;
        List<Team> teams = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                teams.add(mapResultSetToTeam(rs));
            }
            return teams;
        } catch (SQLException e) {
            throw new LabotoryRuntimeException("Failed to fetch all teams", e);
        }
    }

    @Override
    public boolean update(Team team) throws Exception {
        if (team.getId() == null) {
            throw new LabotoryRuntimeException("Cannot update team without ID");
        }

        String sql = "UPDATE " + schema + "." + table + " SET name = ?, score = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, team.getName());
            ps.setInt(2, team.getScore());
            ps.setInt(3, team.getId());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new LabotoryRuntimeException("Failed to update team with id: " + team.getId(), e);
        }
    }

    @Override
    public void deleteById(int id) throws Exception {
        String sql = "DELETE FROM " + schema + "." + table + " WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new LabotoryRuntimeException("Failed to delete team with id: " + id, e);
        }
    }

    private Team mapResultSetToTeam(ResultSet rs) throws SQLException {
        return new Team(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("score")
        );
    }
}