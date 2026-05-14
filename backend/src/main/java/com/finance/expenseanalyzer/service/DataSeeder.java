package com.finance.expenseanalyzer.service;

import com.finance.expenseanalyzer.model.Budget;
import com.finance.expenseanalyzer.model.Expense;
import com.finance.expenseanalyzer.model.Income;
import com.finance.expenseanalyzer.model.User;
import com.finance.expenseanalyzer.repository.BudgetRepository;
import com.finance.expenseanalyzer.repository.ExpenseRepository;
import com.finance.expenseanalyzer.repository.IncomeRepository;
import com.finance.expenseanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final BudgetRepository budgetRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Seed base premium testing user
            User demoUser = User.builder()
                    .name("Demo Financial Investor")
                    .email("demo@finance.ai")
                    .password(passwordEncoder.encode("password"))
                    .build();

            userRepository.save(demoUser);

            // Seed Income
            LocalDate now = LocalDate.now();
            incomeRepository.save(Income.builder().user(demoUser).source("Primary Tech Salary").amount(85000.0).date(now.withDayOfMonth(1)).build());
            incomeRepository.save(Income.builder().user(demoUser).source("Freelance Consulting").amount(15000.0).date(now.minusDays(10)).build());
            incomeRepository.save(Income.builder().user(demoUser).source("Stock Dividend").amount(3500.0).date(now.minusDays(18)).build());
            
            // Previous month income
            incomeRepository.save(Income.builder().user(demoUser).source("Primary Tech Salary").amount(85000.0).date(now.minusMonths(1).withDayOfMonth(1)).build());
            incomeRepository.save(Income.builder().user(demoUser).source("Freelance Consulting").amount(12000.0).date(now.minusMonths(1).minusDays(5)).build());

            // Seed Budgets
            String currentMonthStr = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            String prevMonthStr = YearMonth.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            
            budgetRepository.save(Budget.builder().user(demoUser).monthlyLimit(50000.0).month(currentMonthStr).build());
            budgetRepository.save(Budget.builder().user(demoUser).monthlyLimit(45000.0).month(prevMonthStr).build());

            // Seed Expenses
            // Current Month
            expenseRepository.save(Expense.builder().user(demoUser).category("Food").amount(4500.0).description("Weekend Family Dinner at Gourmet Bistro").date(now.minusDays(2)).build());
            expenseRepository.save(Expense.builder().user(demoUser).category("Shopping").amount(8900.0).description("Bought new ergonomic chair").date(now.minusDays(4)).build());
            expenseRepository.save(Expense.builder().user(demoUser).category("Transport").amount(1800.0).description("Monthly cab top-up").date(now.minusDays(7)).build());
            expenseRepository.save(Expense.builder().user(demoUser).category("Bills").amount(3200.0).description("High speed fiber internet plan").date(now.minusDays(12)).build());
            expenseRepository.save(Expense.builder().user(demoUser).category("Entertainment").amount(3500.0).description("Cinema and game arcade night").date(now.minusDays(15)).build());
            expenseRepository.save(Expense.builder().user(demoUser).category("Food").amount(2100.0).description("Weekly organic grocery delivery").date(now.minusDays(19)).build());
            expenseRepository.save(Expense.builder().user(demoUser).category("Education").amount(5000.0).description("Advanced AI Cloud Architecture bootcamp").date(now.minusDays(22)).build());

            // Previous Month
            expenseRepository.save(Expense.builder().user(demoUser).category("Food").amount(3200.0).description("Steakhouse lunch").date(now.minusMonths(1).minusDays(2)).build());
            expenseRepository.save(Expense.builder().user(demoUser).category("Shopping").amount(6100.0).description("Summer apparel update").date(now.minusMonths(1).minusDays(6)).build());
            expenseRepository.save(Expense.builder().user(demoUser).category("Bills").amount(4500.0).description("Electricity utility cycle").date(now.minusMonths(1).minusDays(10)).build());
            expenseRepository.save(Expense.builder().user(demoUser).category("Transport").amount(2500.0).description("Flight check-in fee").date(now.minusMonths(1).minusDays(14)).build());
        }
    }
}
