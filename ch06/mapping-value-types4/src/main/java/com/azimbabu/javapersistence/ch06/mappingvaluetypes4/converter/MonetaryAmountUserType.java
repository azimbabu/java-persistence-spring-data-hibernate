package com.azimbabu.javapersistence.ch06.mappingvaluetypes4.converter;

import com.azimbabu.javapersistence.ch06.mappingvaluetypes4.model.MonetaryAmount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.ValueAccess;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.DynamicParameterizedType;

public class MonetaryAmountUserType implements CompositeUserType<MonetaryAmount>, DynamicParameterizedType {

  private Currency convertTo;

  @Override
  public Object getPropertyValue(MonetaryAmount monetaryAmount, int property) throws HibernateException {
    if (property == 0) {
      return monetaryAmount.getValue();
    } else {
      return monetaryAmount.getCurrency();
    }
  }

  @Override
  public MonetaryAmount instantiate(ValueAccess values, SessionFactoryImplementor sessionFactory) {
    BigDecimal amount = values.getValue(0, BigDecimal.class);
    Currency currency = Currency.getInstance(values.getValue(1, String.class));
    return new MonetaryAmount(amount, currency);
  }

  private MonetaryAmount convert(MonetaryAmount amount, Currency fromCurrency, Currency toCurrency) {
    return new MonetaryAmount(CurrencyConverter.convert(amount.getValue(), fromCurrency, toCurrency), toCurrency);
  }

  @Override
  public Class<?> embeddable() {
    return MonetaryAmount.class;
  }

  @Override
  public Class<MonetaryAmount> returnedClass() {
    return MonetaryAmount.class;
  }

  @Override
  public boolean equals(MonetaryAmount x, MonetaryAmount y) {
    return Objects.equals(x, y);
  }

  @Override
  public int hashCode(MonetaryAmount x) {
    return x.hashCode();
  }

  @Override
  public MonetaryAmount deepCopy(MonetaryAmount value) {
    return value;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Serializable disassemble(MonetaryAmount value) {
    return value.toString();
  }

  @Override
  public MonetaryAmount assemble(Serializable cached, Object owner) {
    return MonetaryAmount.fromString((String) cached);
  }

  @Override
  public MonetaryAmount replace(MonetaryAmount detached, MonetaryAmount managed, Object owner) {
    return detached;
  }

  @Override
  public void setParameterValues(Properties parameters) {
    String convertToParameter = parameters.getProperty("convertTo");
    this.convertTo = Currency.getInstance(convertToParameter != null ? convertToParameter : "USD");
  }
}
