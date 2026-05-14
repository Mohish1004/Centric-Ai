package com.finance.expenseanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyAnalyticsDto {
    private String month;
    private Double totalIncome;
    private Double totalExpense;
    private Double budgetLimit;
}
