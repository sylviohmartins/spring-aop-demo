package br.com.sylviomartins.spring.aop.demo.domain.converter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class CustomConverter {

    private List<Converter<?>> converters;

    public CustomConverter() {
        converters = initializeConverters();
    }

    public Object convertToValue(Object value, Class<?> targetType) {
        for (Converter<?> converter : converters) {
            if (converter.supports(targetType)) {
                return converter.convert(value);
            }
        }
        return value;
    }

    private List<Converter<?>> initializeConverters() {
        List<Converter<?>> converters = new ArrayList<>();
        converters.add(new StringConverter());
        converters.add(new IntegerConverter());
        converters.add(new LongConverter());
        converters.add(new DoubleConverter());
        converters.add(new BooleanConverter());
        converters.add(new LocalDateConverter());
        converters.add(new BigDecimalConverter());
        // Adicione mais conversores conforme necessário
        return converters;
    }

    public interface Converter<T> {
        boolean supports(Class<?> targetType);

        T convert(Object value);
    }

    @Component
    public static class StringConverter implements Converter<String> {
        @Override
        public boolean supports(Class<?> targetType) {
            return targetType.equals(String.class);
        }

        @Override
        public String convert(Object value) {
            return (String) value;
        }
    }

    @Component
    public static class IntegerConverter implements Converter<Integer> {
        @Override
        public boolean supports(Class<?> targetType) {
            return targetType.equals(Integer.class) || targetType.equals(int.class);
        }

        @Override
        public Integer convert(Object value) {
            if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            throw new IllegalArgumentException("Cannot convert value to Integer: " + value);
        }
    }

    @Component
    public static class LongConverter implements Converter<Long> {
        @Override
        public boolean supports(Class<?> targetType) {
            return targetType.equals(Long.class) || targetType.equals(long.class);
        }

        @Override
        public Long convert(Object value) {
            if (value instanceof String) {
                return Long.parseLong((String) value);
            } else if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            throw new IllegalArgumentException("Cannot convert value to Long: " + value);
        }
    }

    @Component
    public static class DoubleConverter implements Converter<Double> {
        @Override
        public boolean supports(Class<?> targetType) {
            return targetType.equals(Double.class) || targetType.equals(double.class);
        }

        @Override
        public Double convert(Object value) {
            if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            throw new IllegalArgumentException("Cannot convert value to Double: " + value);
        }
    }

    @Component
    public static class BooleanConverter implements Converter<Boolean> {
        @Override
        public boolean supports(Class<?> targetType) {
            return targetType.equals(Boolean.class) || targetType.equals(boolean.class);
        }

        @Override
        public Boolean convert(Object value) {
            if (value instanceof String) {
                return Boolean.parseBoolean((String) value);
            }
            throw new IllegalArgumentException("Cannot convert value to Boolean: " + value);
        }
    }

    @Component
    public static class LocalDateConverter implements Converter<LocalDate> {
        private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

        @Override
        public boolean supports(Class<?> targetType) {
            return targetType.equals(LocalDate.class);
        }

        @Override
        public LocalDate convert(Object value) {

            if (value instanceof LocalDate) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
                    return LocalDate.parse(value.toString(), formatter);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Cannot convert value to LocalDate: " + value);
                }
            }
            throw new IllegalArgumentException("Cannot convert value to LocalDate: " + value);
        }
    }

    @Component
    public static class BigDecimalConverter implements Converter<BigDecimal> {
        @Override
        public boolean supports(Class<?> targetType) {
            return targetType.equals(BigDecimal.class);
        }

        @Override
        public BigDecimal convert(Object value) {
            if (value instanceof String) {
                return new BigDecimal((String) value);
            } else if (value instanceof Number) {
                return new BigDecimal(value.toString());
            }
            throw new IllegalArgumentException("Cannot convert value to BigDecimal: " + value);
        }
    }

}
