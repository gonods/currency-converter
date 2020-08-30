package com.example.converter.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class HistoryDto {
    private String fromVal;
    private String toVal;
    private Double amount;
    private Double sum;
    private Date date;

    public static HistoryDto fromHistory(History history) {
        return new HistoryDto().setFromVal(history.getFromVal())
                          .setToVal(history.getToVal())
                          .setAmount(history.getAmount())
                          .setSum(history.getSum())
                          .setDate(history.getDate());
    }
}
