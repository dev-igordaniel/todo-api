package com.todolist.todoapi.repository;

import com.todolist.todoapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {  //User é a entidade que o repository vai manipular
                                                                        //Integer é o tipo da chave primária (id) da entidade
    Optional<User> findByUsername(String username);                     //Quando eu chamar o repository o JPA vai fazer a consulta declara

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
