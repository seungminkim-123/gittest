package com.kshzzang44.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kshzzang44.todo.entity.TodoImageEntity;

public interface TodoImageRepository extends JpaRepository<TodoImageEntity,Long>{
    public List<TodoImageEntity> findByTiSeq(Long tiSeq);
}
