package com.intask.Task.Repository;

import com.intask.Task.Entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InsuranceRepo extends JpaRepository<Insurance, Long> {
    @Query("SELECT i FROM Insurance i WHERE " +
            "(i.minAge <= :age AND i.maxAge >= :age) " +
            "AND (i.gender = :gender OR i.gender = 'Any') " +
            "AND (i.minIncome <= :income)")
    List<Insurance> findCuratedInsurances(@Param("age") int age,
                                          @Param("gender") String gender,
                                          @Param("income") double income);
}
