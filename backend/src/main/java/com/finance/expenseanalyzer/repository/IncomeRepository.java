package com.finance.expenseanalyzer.repository;

import com.finance.expenseanalyzer.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUserIdOrderByDateDesc(Long userId);

    @Query("SELECT i FROM Income i WHERE i.user.id = :userId AND i.date >= :startDate AND i.date <= :endDate ORDER BY i.date DESC")
    List<Income> findByUserIdAndDateBetween(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
