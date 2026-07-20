package com.todolist.todoapi.service;

import com.todolist.todoapi.dto.TaskRequestDTO;
import com.todolist.todoapi.dto.TaskResponseDTO;
import com.todolist.todoapi.entity.Task;
import com.todolist.todoapi.entity.TaskStatus;
import com.todolist.todoapi.entity.User;
import com.todolist.todoapi.repository.TaskRepository;
import com.todolist.todoapi.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskrepository, UserRepository userRepository){
        this.taskRepository = taskrepository;
        this.userRepository = userRepository;
    }

    //converte a entidade Task para o DTO de Resposta
    private TaskResponseDTO toDTO(Task task){
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate() != null ? task.getDueDate().toString() : null,
                task.getCreatedAt().toString(),
                task.getUpdatedAt() != null ? task.getUpdatedAt().toString() : null
        );
    }

    //busca o usuário no banco pelo username
    private User getUser (String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public TaskResponseDTO create (TaskRequestDTO dto, String username){
        User user = getUser(username);

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(TaskStatus.PENDING);
        task.setDueDate(dto.getDueDate() != null ? LocalDate.parse(dto.getDueDate()) : null);
        task.setCreatedAt(LocalDate.now());
        task.setUser(user);

        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    public List<TaskResponseDTO> listAll (String username) {
        User user = getUser(username);

        return taskRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDTO) //converte cada Task para TaskResponseDTO
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> listByStatus(String username, TaskStatus status){
        User user = getUser (username);

        return taskRepository.findByUserIdAndStatus(user.getId(), status)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO update(Integer id, TaskRequestDTO dto, String username){
        User user = getUser(username);

        //busca a task garantindo que ela pertence ao usuário logado
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Task não encontrada")));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate() != null ? LocalDate.parse(dto.getDueDate()) : null);
        task.setUpdatedAt(LocalDate.now());

        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    public void delete(Integer id, String username){
        User user = getUser(username);

        //busca a task garantindo que ela pertence ao usuário logado
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Task não encontrada"));

        taskRepository.delete(task);
    }
}














