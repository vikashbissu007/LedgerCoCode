package util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Utility {

    public static Date DateAfterDays(Date date, int addDays) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusDays(addDays);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
