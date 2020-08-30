package com.example.converter.controller;

import com.example.converter.model.ConverterDto;
import com.example.converter.service.CurrencyService;
import com.example.converter.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RequestMapping("/api")
@RestController
public class CurrencyRestController {
    private CurrencyService currencyService;
    private HistoryService historyService;


    @Autowired
    public CurrencyRestController(CurrencyService currencyService, HistoryService historyService) {
        this.currencyService = currencyService;
        this.historyService = historyService;
    }

    @GetMapping
    public void get() throws IOException, SAXException, ParserConfigurationException {
        currencyService.parseCurrency();
    }

    @GetMapping("/converter/{value}/to/{value1}/{amount}")
    @PreAuthorize("hasAnyAuthority('developers:read')")
    public ConverterDto convert(@PathVariable("value") String val1, @PathVariable("value1") String val2,
                                @PathVariable("amount") Double amount,
                                @RequestHeader("authorization") String token) {

        ConverterDto dto = new ConverterDto();
        dto.setSum(currencyService.convert(val1, val2, amount));
        historyService.save(val1, val2, amount, dto.getSum(), token);

        return dto;
    }

}
