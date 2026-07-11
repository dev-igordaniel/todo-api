package com.todolist.todoapi.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 1500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)              //Define que o relacionamento é muitos pra um.
    @JoinColumn(name = "user_id", nullable = false) //O FetchType.LAZY define que os dados do user só serão carregados quando forem chamados explicitamente através desta classe
    private User user;                              //Caso contrário, ao buscar a task o JPA consultaria automaticamente no banco os dados do usuário vinculado
}
