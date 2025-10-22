package com.dailystatus.dailyupdate.controller;

import com.dailystatus.dailyupdate.service.ReportAutomationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/automation")
public class AutomationController {

    private final ReportAutomationService automationService;

    public AutomationController(ReportAutomationService automationService) {
        this.automationService = automationService;
    }

    @PostMapping("/run")
    public String runAutomationManually() {
        automationService.runAutomation();
        return "✅ Report automation executed successfully!";
    }
}
