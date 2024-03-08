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
    private Long salesAmt;
    private String salesShare;
    private Long salesInten;
    private String salesChange;
    private Long salesAcc;
    private Long audiCnt;
    private Long audiInten;
    private String audiChange;
    private Long audiAcc;
    private Long scrnCnt;
    private Long showCnt;
}
