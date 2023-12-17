package com.azimbabu.javapersistence.ch06.mappingvaluetypes4.converter;

import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class CurrencyConverterTest {
  @Test
  void convertBDTToUSD() {
    BigDecimal bdtToUSD = CurrencyConverter.convert(BigDecimal.valueOf(50000),
        Currency.getInstance("BDT"), Currency.getInstance("USD"));
    BigDecimal usdToBDT = CurrencyConverter.convert(bdtToUSD,
        Currency.getInstance("USD"), Currency.getInstance("BDT"));
    System.out.println(bdtToUSD);
    System.out.println(usdToBDT);
    System.out.println(CurrencyConverter.convert(BigDecimal.valueOf(100),
        Currency.getInstance("USD"), Currency.getInstance("USD")));
  }

  @Test
  void convertBDTToEUR() {
    BigDecimal bdtToEUR = CurrencyConverter.convert(BigDecimal.valueOf(50000),
        Currency.getInstance("BDT"), Currency.getInstance("EUR"));
    BigDecimal eurToBDT = CurrencyConverter.convert(bdtToEUR,
        Currency.getInstance("EUR"), Currency.getInstance("BDT"));
    System.out.println(bdtToEUR);
    System.out.println(eurToBDT);
    System.out.println(CurrencyConverter.convert(BigDecimal.valueOf(100),
        Currency.getInstance("EUR"), Currency.getInstance("EUR")));
  }

  @Test
  void convertUSDToEUR() {
    BigDecimal usdToEUR = CurrencyConverter.convert(BigDecimal.valueOf(50000),
        Currency.getInstance("USD"), Currency.getInstance("EUR"));
    BigDecimal eurToUSD = CurrencyConverter.convert(usdToEUR,
        Currency.getInstance("EUR"), Currency.getInstance("USD"));
    System.out.println(usdToEUR);
    System.out.println(eurToUSD);
  }
}