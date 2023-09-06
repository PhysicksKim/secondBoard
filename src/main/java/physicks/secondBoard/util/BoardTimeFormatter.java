package physicks.secondBoard.util;

import java.time.LocalDateTime;

public class BoardTimeFormatter {
    static public String forPostRead(LocalDateTime createdTime) {
        LocalDateTime now = LocalDateTime.now();

        // 날짜가 동일한 경우 시:분 으로 표기 (14:52)
        if (now.toLocalDate().equals(createdTime.toLocalDate())) {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
            return createdTime.format(formatter);
        } else { // 날짜가 다른 경우 월/일 로 표기 (04/23)
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM/dd");
            return createdTime.format(formatter);
        }
    }
}
