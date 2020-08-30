package com.example.converter.service;

import com.example.converter.model.Currency;
import com.example.converter.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@EnableScheduling
public class CurrencyService {
    private static ArrayList<Currency> currencies;
    private final CurrencyRepository currencyRepository;
    private final HistoryService historyService;


    @Scheduled(cron = "0 0 6,19 * * *")
    public void parseCurrency() throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        XMLHandler handler = new XMLHandler();
        currencies = new ArrayList<>();

        parser.parse("http://www.cbr.ru/scripts/XML_daily.asp", handler);
        for (Currency currency : currencies) {
            Currency find = currencyRepository.findByDateAndCharCode(currency.getDate(), currency.getCharCode());
            if (find == null) {
                currencyRepository.save(currency);
            }
        }

    }

    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    public double convert(String val1, String val2, Double amount) {
        Currency currency1 = currencyRepository.findTop1ByCharCodeOrderByDateDesc(val1);
        Currency currency2 = currencyRepository.findTop1ByCharCodeOrderByDateDesc(val2);

        Double sum;
        if (val1.equals("RUB") && !val2.equals("RUB")) {
            sum = ((amount * currency2.getNominal()) / currency2.getValue());
        } else if (!val1.equals("RUB") && val2.equals("RUB")) {
            sum = ((amount / currency1.getNominal()) * currency1.getValue());
        } else if (val1.equals("RUB") && val2.equals("RUB")) {
            sum = amount;
        } else {
            sum = (currency1.getValue() * currency1.getNominal() * amount) / (currency2.getValue() / currency2.getNominal());
        }


        return sum;
    }


    private static class XMLHandler extends DefaultHandler {
        String valuteId;
        Integer numCode;
        String charCode;
        Integer nominal;
        String name;
        Double value;
        Date date;
        String lastElementName;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            lastElementName = qName;
            if (qName.equals("ValCurs")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    date = formatter.parse(attributes.getValue("Date"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (qName.equals("Valute")) {
                valuteId = attributes.getValue("ID");
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

            if ((valuteId != null && numCode != null && charCode != null && nominal != null && name != null && value != null)) {
                Currency currency = new Currency();
                currency.setValuteId(valuteId);
                currency.setNumCode(numCode);
                currency.setCharCode(charCode);
                currency.setNominal(nominal);
                currency.setName(name);
                currency.setValue(value);
                currency.setDate(date);
                currencies.add(currency);
                valuteId = null;
                numCode = null;
                charCode = null;
                nominal = null;
                name = null;
                value = null;
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String information = new String(ch, start, length);

            information = information.replace("\n", "").trim();

            if (!information.isEmpty()) {
                if (lastElementName.equals("NumCode")) {
                    numCode = Integer.valueOf(information);
                }
                if (lastElementName.equals("CharCode")) {
                    charCode = information;
                }
                if (lastElementName.equals("Nominal")) {
                    nominal = Integer.valueOf(information);
                }
                if (lastElementName.equals("Name")) {
                    name = information;
                }
                if (lastElementName.equals("Value")) {
                    value = Double.valueOf(information.replace(",", "."));
                }
            }
        }

    }

}
