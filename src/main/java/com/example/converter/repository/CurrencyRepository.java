package com.example.converter.repository;

import com.example.converter.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByDateAndCharCode(Date date,String name);

    Currency findTop1ByCharCodeOrderByDateDesc(String name);

}
