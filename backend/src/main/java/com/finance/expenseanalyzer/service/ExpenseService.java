package com.finance.expenseanalyzer.service;

import com.finance.expenseanalyzer.dto.ExpenseDto;
import com.finance.expenseanalyzer.model.Expense;
import com.finance.expenseanalyzer.model.User;
import com.finance.expenseanalyzer.repository.ExpenseRepository;
import com.finance.expenseanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    public List<ExpenseDto> getAllExpenses() {
        User user = getCurrentUser();
        return expenseRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ExpenseDto createExpense(ExpenseDto expenseDto) {
        User user = getCurrentUser();
        Expense expense = Expense.builder()
                .user(user)
                .category(expenseDto.getCategory())
                .amount(expenseDto.getAmount())
                .description(expenseDto.getDescription())
                .date(expenseDto.getDate())
                .receiptUrl(expenseDto.getReceiptUrl())
                .build();

        Expense saved = expenseRepository.save(expense);
        return mapToDto(saved);
    }

    public ExpenseDto updateExpense(Long id, ExpenseDto expenseDto) {
        User user = getCurrentUser();
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this expense");
        }

        expense.setCategory(expenseDto.getCategory());
        expense.setAmount(expenseDto.getAmount());
        expense.setDescription(expenseDto.getDescription());
        expense.setDate(expenseDto.getDate());
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
