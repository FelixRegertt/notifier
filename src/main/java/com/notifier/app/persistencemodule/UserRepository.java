package com.notifier.app.persistencemodule;

import org.springframework.data.jpa.repository.JpaRepository;


public interface  UserRepository extends JpaRepository<User, String> {
}
