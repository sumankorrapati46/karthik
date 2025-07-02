package com.farmer.Form.Mapper;

import org.mapstruct.Named;
 
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
 
public class DateMapper {
 
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 
    @Named("asString")
    public String asString(LocalDate date) {
        return date != null ? FORMATTER.format(date) : null;
    }
 
    @Named("asDate")
    public LocalDate asDate(String date) {
        return date != null ? LocalDate.parse(date, FORMATTER) : null;
    }
}
