package com.ZenPack.utils;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
@Component
public class CommonFunctions {

    public String getUtcDateTime() {
        LocalDateTime myDateObj = LocalDateTime.now(Clock.systemUTC());
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate.toString();
    }
}
