package com.todolist.todoapi.dto;

import com.todolist.todoapi.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponseDTO {

    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private String dueDate;
    private String createdAt;
    private String updatedAt;
}
