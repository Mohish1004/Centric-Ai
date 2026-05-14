package com.finance.expenseanalyzer.service;

import com.finance.expenseanalyzer.dto.ExpenseDto;
import com.finance.expenseanalyzer.model.Expense;
import com.finance.expenseanalyzer.model.User;
import com.finance.expenseanalyzer.repository.ExpenseRepository;
import com.finance.expenseanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
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
    public List<ExpenseDto> getAllExpenses() {
        User user = getCurrentUser();
        return expenseRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ExpenseDto createExpense(ExpenseDto expenseDto) {
        if (expenseDto == null) {
            throw new IllegalArgumentException("Expense data cannot be null");
        }
        User user = getCurrentUser();
        Expense expense = Expense.builder()
                .user(user)
                .category(expenseDto.getCategory() != null ? expenseDto.getCategory() : "General")
                .amount(expenseDto.getAmount() != null ? expenseDto.getAmount() : 0.0)
                .description(expenseDto.getDescription())
                .date(expenseDto.getDate() != null ? expenseDto.getDate() : LocalDate.now())
                .receiptUrl(expenseDto.getReceiptUrl())
                .build();

        Expense saved = expenseRepository.save(expense);
        return mapToDto(saved);
    }

    public ExpenseDto updateExpense(Long id, ExpenseDto expenseDto) {
        if (expenseDto == null) {
            throw new IllegalArgumentException("Expense data cannot be null");
        }
        User user = getCurrentUser();
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this expense");
        }

        if (expenseDto.getCategory() != null) {
            expense.setCategory(expenseDto.getCategory());
        }
        if (expenseDto.getAmount() != null) {
            expense.setAmount(expenseDto.getAmount());
        }
        if (expenseDto.getDescription() != null) {
            expense.setDescription(expenseDto.getDescription());
        }
        if (expenseDto.getDate() != null) {
            expense.setDate(expenseDto.getDate());
        }
        expense.setReceiptUrl(expenseDto.getReceiptUrl());

        return mapToDto(expenseRepository.save(expense));
    }

    public void deleteExpense(Long id) {
        User user = getCurrentUser();
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }

        expenseRepository.delete(expense);
    }

    private ExpenseDto mapToDto(Expense expense) {
        return ExpenseDto.builder()
                .id(expense.getId())
                .category(expense.getCategory())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .date(expense.getDate())
                .receiptUrl(expense.getReceiptUrl())
                .build();
    }
}
