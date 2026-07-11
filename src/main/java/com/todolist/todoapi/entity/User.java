package com.todolist.todoapi.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Entity                 // diz que esta classe representa uma tabela do banco
@Table(name = "users")  // diz em qual tabela essa entidade será armazenada
@Data                   // gera getters, setters, equals, hashCode, toString
@NoArgsConstructor      // construtor vazio (JPA exige)
@AllArgsConstructor     // construtor com todos os campos
@Builder                // permite criar objetos assim: User.builder().username("dev.igorfarias").build()

public class User {     // cada objeto desta classe representa uma linha na tabela "users" do banco

    @Id                                                     //Indica que o atributo é a chave primária (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //Diz ao JPA que o valor do id será gerado automaticamente pelo banco
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)   // @Column() define as propriedades da coluna
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
}
