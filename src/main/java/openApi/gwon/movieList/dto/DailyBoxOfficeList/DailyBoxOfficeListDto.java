package openApi.gwon.movieList.dto.DailyBoxOfficeList;


import lombok.Data;

@Data
public class DailyBoxOfficeListDto {

    // NumberFormatException -> integer to -> Long

    private String dailyBoxOfficeId;
    private String rnum;
    private String rank;
    private String rankInten;
    private String rankOldAndNew; // “OLD” : 기존 , “NEW” : 신규
    private String movieCd;
    private String movieNm;
    private String openDt;
    private String salesAmt;
    private String salesShare;
    private String salesInten;
    private String salesChange;
    private String salesAcc;
    private String audiCnt;
    private String audiInten;
    private String audiChange;
    private String audiAcc;
    private String scrnCnt;
    private String showCnt;


}
