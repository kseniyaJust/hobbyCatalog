package org.example.hobbycatalog.repository;

import org.example.hobbycatalog.entity.TypeHobbies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeHobbyRepository extends JpaRepository<TypeHobbies,Long> {
    // Поиск по имени типа
    Optional<TypeHobbies> findByTypeName(String type_name);

    // Проверка существования по имени
    boolean existsByTypeName(String type_name);

    // Поиск по количеству игроков
    List<TypeHobbies> findByCountPlayers(int countPlayers);
}
