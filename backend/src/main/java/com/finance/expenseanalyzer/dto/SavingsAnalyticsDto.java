package com.finance.expenseanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingsAnalyticsDto {
    private Double totalSavings;
    private Double savingsRate; // percentage saved
    private String recommendation;
}
