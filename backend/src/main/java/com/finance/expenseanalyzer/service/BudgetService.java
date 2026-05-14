package com.finance.expenseanalyzer.service;

import com.finance.expenseanalyzer.dto.BudgetDto;
import com.finance.expenseanalyzer.model.Budget;
import com.finance.expenseanalyzer.model.User;
import com.finance.expenseanalyzer.repository.BudgetRepository;
import com.finance.expenseanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    public List<BudgetDto> getAllBudgets() {
        User user = getCurrentUser();
        return budgetRepository.findByUserIdOrderByMonthDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public BudgetDto saveOrUpdateBudget(BudgetDto budgetDto) {
        User user = getCurrentUser();
        Optional<Budget> existing = budgetRepository.findByUserIdAndMonth(user.getId(), budgetDto.getMonth());

        Budget budget;
        if (existing.isPresent()) {
            budget = existing.get();
            budget.setMonthlyLimit(budgetDto.getMonthlyLimit());
        } else {
            budget = Budget.builder()
                    .user(user)
                    .monthlyLimit(budgetDto.getMonthlyLimit())
                    .month(budgetDto.getMonth())
                    .build();
        }

        return mapToDto(budgetRepository.save(budget));
    }

    public BudgetDto getBudgetForMonth(String month) {
        User user = getCurrentUser();
        return budgetRepository.findByUserIdAndMonth(user.getId(), month)
                .map(this::mapToDto)
                .orElse(BudgetDto.builder().month(month).monthlyLimit(0.0).build());
    }

    private BudgetDto mapToDto(Budget budget) {
        return BudgetDto.builder()
                .id(budget.getId())
                .monthlyLimit(budget.getMonthlyLimit())
                .month(budget.getMonth())
                .build();
    }
}
