package com.test.challenge.repository;

import com.test.challenge.dto.InstitutionDTO;
import com.test.challenge.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    @Query(value = "SELECT i.institution_id, i.name, count(u.user_id) FROM user u inner join institution i on u.institution_id = i.institution_id WHERE i.institution_id = :id", nativeQuery = true)
    Optional<List<Object[]>> getData(@Param("id") Long id);

}
