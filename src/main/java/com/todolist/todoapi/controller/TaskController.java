package com.todolist.todoapi.controller;
import com.todolist.todoapi.dto.TaskRequestDTO;
import com.todolist.todoapi.dto.TaskResponseDTO;
import com.todolist.todoapi.entity.TaskStatus;
import com.todolist.todoapi.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@RequestBody TaskRequestDTO dto, @AuthenticationPrincipal UserDetails userDetails){
        TaskResponseDTO response = taskService.create(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> listAll(@AuthenticationPrincipal UserDetails userDetails){
        List<TaskResponseDTO> response = taskService.listAll(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskResponseDTO>> listByStatus(@RequestParam TaskStatus status, @AuthenticationPrincipal UserDetails userDetails) {
        List<TaskResponseDTO> response = taskService.listByStatus(userDetails.getUsername(), status);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Integer id, @RequestBody TaskRequestDTO dto, @AuthenticationPrincipal UserDetails userDetails){
        TaskResponseDTO response = taskService.update(id, dto, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        taskService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
