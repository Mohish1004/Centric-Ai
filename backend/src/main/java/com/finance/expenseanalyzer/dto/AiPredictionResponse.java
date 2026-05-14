package com.finance.expenseanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiPredictionResponse {
    private Double predictedNextMonthExpense;
    private Double forecastedSavings;
    private String trendSummary;
}
