package com.notifier.app.db;

import org.springframework.data.jpa.repository.JpaRepository;


public interface  UserRepository extends JpaRepository<User, String> {
}
