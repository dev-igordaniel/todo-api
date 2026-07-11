package com.todolist.todoapi.dto;

import com.todolist.todoapi.entity.TaskStatus;
import lombok.Data;

@Data
public class TaskRequestDTO {

    private String title;
    private String description;
    private TaskStatus status;
    private String dueDate;
}
