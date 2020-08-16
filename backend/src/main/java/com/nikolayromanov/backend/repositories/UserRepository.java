package com.nikolayromanov.backend.repositories;

import com.nikolayromanov.backend.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
}