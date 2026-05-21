package com.buevich.labs.database.repository;

import com.buevich.labs.database.entity.Team;
import java.util.List;

public interface TeamRepository {
    // Create
    int save(Team team) throws Exception;

    // Read by id
    Team findById(int id) throws Exception;

    // Read by name
    Team findByName(String name) throws Exception;

    List<Team> findAll() throws Exception;

    // Update
    boolean update(Team team) throws Exception;

    // Delete
    void deleteById(int id) throws Exception;
}