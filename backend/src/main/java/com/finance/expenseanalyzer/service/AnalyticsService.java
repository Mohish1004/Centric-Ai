package com.finance.expenseanalyzer.service;

import com.finance.expenseanalyzer.dto.CategoryAnalyticsDto;
import com.finance.expenseanalyzer.dto.MonthlyAnalyticsDto;
import com.finance.expenseanalyzer.dto.SavingsAnalyticsDto;
import com.finance.expenseanalyzer.model.Budget;
import com.finance.expenseanalyzer.model.Expense;
import com.finance.expenseanalyzer.model.Income;
import com.finance.expenseanalyzer.model.User;
import com.finance.expenseanalyzer.repository.BudgetRepository;
import com.finance.expenseanalyzer.repository.ExpenseRepository;
import com.finance.expenseanalyzer.repository.IncomeRepository;
import com.finance.expenseanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
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

    public List<MonthlyAnalyticsDto> getMonthlyAnalytics(int monthsCount) {
        User user = getCurrentUser();
        List<MonthlyAnalyticsDto> results = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();
        int count = monthsCount > 0 ? monthsCount : 6;

        for (int i = count - 1; i >= 0; i--) {
            YearMonth targetMonth = currentMonth.minusMonths(i);
            LocalDate startDate = targetMonth.atDay(1);
            LocalDate endDate = targetMonth.atEndOfMonth();
            String monthStr = targetMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));

            List<Income> incomes = incomeRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);
            double totalIncome = incomes.stream()
                    .filter(inc -> inc.getAmount() != null)
                    .mapToDouble(Income::getAmount)
                    .sum();

            List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);
            double totalExpense = expenses.stream()
                    .filter(exp -> exp.getAmount() != null)
                    .mapToDouble(Expense::getAmount)
                    .sum();

            Optional<Budget> budget = budgetRepository.findByUserIdAndMonth(user.getId(), monthStr);
            double budgetLimit = budget.map(b -> b.getMonthlyLimit() != null ? b.getMonthlyLimit() : 0.0).orElse(0.0);

            results.add(MonthlyAnalyticsDto.builder()
                    .month(monthStr)
                    .totalIncome(totalIncome)
                    .totalExpense(totalExpense)
                    .budgetLimit(budgetLimit)
                    .build());
        }

        return results;
    }

    public List<CategoryAnalyticsDto> getCategoryAnalytics(String monthStr) {
        User user = getCurrentUser();
        YearMonth yearMonth;
        try {
            if (monthStr == null || monthStr.trim().isEmpty()) {
                yearMonth = YearMonth.now();
            } else {
                yearMonth = YearMonth.parse(monthStr.trim());
            }
        } catch (Exception e) {
            yearMonth = YearMonth.now();
        }

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Object[]> rawCategories = expenseRepository.findCategoryWiseSpending(user.getId(), startDate, endDate);
        double totalExpense = rawCategories.stream()
                .filter(row -> row != null && row.length > 1 && row[1] != null)
                .mapToDouble(row -> ((Number) row[1]).doubleValue())
                .sum();

        List<CategoryAnalyticsDto> dtos = new ArrayList<>();
        for (Object[] row : rawCategories) {
            if (row != null && row.length > 1) {
                String cat = row[0] != null ? (String) row[0] : "Other";
                double amt = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
                double pct = totalExpense > 0 ? (amt / totalExpense) * 100 : 0.0;
                dtos.add(CategoryAnalyticsDto.builder()
                        .category(cat)
                        .amount(amt)
                        .percentage(Math.round(pct * 100.0) / 100.0)
                        .build());
            }
        }

        return dtos;
    }

    public SavingsAnalyticsDto getSavingsAnalytics(String monthStr) {
        User user = getCurrentUser();
        YearMonth yearMonth;
        try {
            yearMonth = (monthStr == null || monthStr.trim().isEmpty()) ? YearMonth.now() : YearMonth.parse(monthStr.trim());
        } catch (Exception e) {
            yearMonth = YearMonth.now();
        }

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        double totalIncome = incomeRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate)
                .stream()
                .filter(inc -> inc.getAmount() != null)
                .mapToDouble(Income::getAmount)
                .sum();

        double totalExpense = expenseRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate)
                .stream()
                .filter(exp -> exp.getAmount() != null)
                .mapToDouble(Expense::getAmount)
                .sum();

        double totalSavings = totalIncome - totalExpense;
        double savingsRate = totalIncome > 0 ? (totalSavings / totalIncome) * 100 : 0.0;

        String recommendation;
        if (totalSavings < 0) {
            recommendation = "Warning: You are running a deficit! Consider immediate cutbacks on discretionary spending like Entertainment and Shopping.";
        } else if (savingsRate < 20.0) {
            recommendation = "Good effort, but try to optimize standard costs to reach the ideal 20% target savings benchmark.";
        } else {
            recommendation = "Excellent! You have a highly healthy savings cushion this period. Keep maintaining this consistency.";
        }

        return SavingsAnalyticsDto.builder()
                .totalSavings(totalSavings)
                .savingsRate(Math.round(savingsRate * 100.0) / 100.0)
                .recommendation(recommendation)
                .build();
    }
}
