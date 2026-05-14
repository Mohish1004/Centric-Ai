package com.finance.expenseanalyzer.controller;

import com.finance.expenseanalyzer.dto.AiInsightsResponse;
import com.finance.expenseanalyzer.dto.AiPredictionResponse;
import com.finance.expenseanalyzer.dto.OcrScanResponse;
import com.finance.expenseanalyzer.service.AiIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiIntegrationController {

    private final AiIntegrationService aiIntegrationService;

    @GetMapping("/insights")
    public ResponseEntity<AiInsightsResponse> getInsights() {
        return ResponseEntity.ok(aiIntegrationService.getInsights());
    }

    @GetMapping("/predict")
    public ResponseEntity<AiPredictionResponse> getPredictions() {
        return ResponseEntity.ok(aiIntegrationService.getPredictions());
    }

    @PostMapping("/ocr")
    public ResponseEntity<OcrScanResponse> scanReceipt(@RequestBody Map<String, String> payload) {
        String base64Image = payload.get("image");
        String fileName = payload.get("fileName");
        return ResponseEntity.ok(aiIntegrationService.scanReceipt(base64Image, fileName));
    }
}
