package com.finance.expenseanalyzer.service;

import com.finance.expenseanalyzer.dto.BudgetDto;
import com.finance.expenseanalyzer.model.Budget;
import com.finance.expenseanalyzer.model.User;
import com.finance.expenseanalyzer.repository.BudgetRepository;
import com.finance.expenseanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("No authenticated user found in context");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    @Transactional(readOnly = true)
    public List<BudgetDto> getAllBudgets() {
        User user = getCurrentUser();
        return budgetRepository.findByUserIdOrderByMonthDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public BudgetDto saveOrUpdateBudget(BudgetDto budgetDto) {
        if (budgetDto == null || budgetDto.getMonth() == null || budgetDto.getMonth().trim().isEmpty()) {
            throw new IllegalArgumentException("Budget month is required");
        }
        User user = getCurrentUser();
        Double limit = budgetDto.getMonthlyLimit() != null ? budgetDto.getMonthlyLimit() : 0.0;
        Optional<Budget> existing = budgetRepository.findByUserIdAndMonth(user.getId(), budgetDto.getMonth());

        Budget budget;
        if (existing.isPresent()) {
            budget = existing.get();
            budget.setMonthlyLimit(limit);
        } else {
            budget = Budget.builder()
                    .user(user)
                    .monthlyLimit(limit)
                    .month(budgetDto.getMonth())
                    .build();
        }

        return mapToDto(budgetRepository.save(Objects.requireNonNull(budget)));
    }

    @Transactional(readOnly = true)
    public BudgetDto getBudgetForMonth(String month) {
        if (month == null || month.trim().isEmpty()) {
            throw new IllegalArgumentException("Month parameter cannot be empty");
        }
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
