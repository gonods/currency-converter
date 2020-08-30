package com.example.converter.repository;

import com.example.converter.model.History;
import com.example.converter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByUser(User user);
}
