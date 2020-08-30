package com.example.converter.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String valuteId;
    private Integer numCode;
    private String charCode;
    private Integer nominal;
    private String name;
    private Double value;
    @Temporal(TemporalType.DATE)
    private Date date;


}
