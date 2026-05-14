package com.finance.expenseanalyzer.controller;

import com.finance.expenseanalyzer.dto.CategoryAnalyticsDto;
import com.finance.expenseanalyzer.dto.MonthlyAnalyticsDto;
import com.finance.expenseanalyzer.dto.SavingsAnalyticsDto;
import com.finance.expenseanalyzer.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyAnalyticsDto>> getMonthlyAnalytics(
            @RequestParam(defaultValue = "6") int months) {
        return ResponseEntity.ok(analyticsService.getMonthlyAnalytics(months));
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryAnalyticsDto>> getCategoryAnalytics(
            @RequestParam(required = false) String month) {
        return ResponseEntity.ok(analyticsService.getCategoryAnalytics(month));
    }

    @GetMapping("/savings")
    public ResponseEntity<SavingsAnalyticsDto> getSavingsAnalytics(
            @RequestParam(required = false) String month) {
        return ResponseEntity.ok(analyticsService.getSavingsAnalytics(month));
    }
}
