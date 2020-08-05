package com.cgs.vo.forms;

import lombok.Data;

@Data
public class StockAchievementVO {
    private String stockId;
    private String stockName;
    private String achievementType;
    private String achievementTitle;
    private String profileChangeRate;
    private String profileLastYear;
    private String releaseDate;
}
