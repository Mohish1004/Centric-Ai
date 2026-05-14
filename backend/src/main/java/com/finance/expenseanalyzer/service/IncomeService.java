package com.finance.expenseanalyzer.service;

import com.finance.expenseanalyzer.dto.IncomeDto;
import com.finance.expenseanalyzer.model.Income;
import com.finance.expenseanalyzer.model.User;
import com.finance.expenseanalyzer.repository.IncomeRepository;
import com.finance.expenseanalyzer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    public List<IncomeDto> getAllIncome() {
        User user = getCurrentUser();
        return incomeRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public IncomeDto createIncome(IncomeDto incomeDto) {
        User user = getCurrentUser();
        Income income = Income.builder()
                .user(user)
                .source(incomeDto.getSource())
                .amount(incomeDto.getAmount())
                .date(incomeDto.getDate())
                .build();

        return mapToDto(incomeRepository.save(income));
    }

    public void deleteIncome(Long id) {
        User user = getCurrentUser();
        Income income = incomeRepository.findById(id)
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
