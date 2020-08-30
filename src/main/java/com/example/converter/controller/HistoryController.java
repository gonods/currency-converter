package com.example.converter.controller;

import com.example.converter.model.HistoryDto;
import com.example.converter.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1")
@RestController("/api")
public class HistoryController {
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyAuthority('developers:read')")
    public List<HistoryDto> getHistory(@RequestHeader("authorization") String token) {
        return historyService.getAll(token);
    }
}
