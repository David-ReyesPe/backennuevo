package com.backendfunda.backendfunda.repository;

import com.backendfunda.backendfunda.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User,Long> {
}
