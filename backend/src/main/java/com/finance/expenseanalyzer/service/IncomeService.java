package com.finance.expenseanalyzer.service;

import com.finance.expenseanalyzer.dto.IncomeDto;
import com.finance.expenseanalyzer.model.Income;
import com.finance.expenseanalyzer.model.User;
import com.finance.expenseanalyzer.repository.IncomeRepository;
import com.finance.expenseanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IncomeService {

    private final IncomeRepository incomeRepository;
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
    public List<IncomeDto> getAllIncome() {
        User user = getCurrentUser();
        return incomeRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public IncomeDto createIncome(IncomeDto incomeDto) {
        if (incomeDto == null) {
            throw new IllegalArgumentException("Income data cannot be null");
        }
        User user = getCurrentUser();
        Income income = Income.builder()
                .user(user)
                .source(incomeDto.getSource() != null ? incomeDto.getSource() : "Primary")
                .amount(incomeDto.getAmount() != null ? incomeDto.getAmount() : 0.0)
                .date(incomeDto.getDate() != null ? incomeDto.getDate() : LocalDate.now())
                .build();

        return mapToDto(incomeRepository.save(Objects.requireNonNull(income)));
    }

    public void deleteIncome(Long id) {
        User user = getCurrentUser();
        Income income = incomeRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new RuntimeException("Income not found"));

        if (!income.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this income record");
        }

        incomeRepository.delete(income);
    }

    private IncomeDto mapToDto(Income income) {
        return IncomeDto.builder()
                .id(income.getId())
                .source(income.getSource())
                .amount(income.getAmount())
                .date(income.getDate())
                .build();
    }
}
