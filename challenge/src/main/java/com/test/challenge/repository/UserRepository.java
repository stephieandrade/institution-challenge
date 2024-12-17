package com.test.challenge.repository;

import com.test.challenge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u.* FROM user u inner join institution i on u.institution_id = i.institution_id WHERE i.institution_id = :id", nativeQuery = true)
    Optional<List<User>> findUsersByInstitution(@Param("id") Long id);

    @Query(value = "SELECT u.* FROM user u WHERE u.institution_id = :id", nativeQuery = true)
    Optional<List<User>> findUsersByPrimaryInstitution(@Param("id") Long institutionId);

}
