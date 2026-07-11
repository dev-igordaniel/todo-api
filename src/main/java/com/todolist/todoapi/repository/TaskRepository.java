package com.todolist.todoapi.repository;

import com.todolist.todoapi.entity.Task;
import com.todolist.todoapi.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>{

    List<Task> findByUserId(Integer userId);

    List<Task> findByUserIdAndStatus(Integer userId, TaskStatus status);

    Optional<Task> findByIdAndUserId(Integer id, Integer userId);

}
