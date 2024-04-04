package openApi.gwon.movieList.dto.weeklyBoxOfficeList;

import lombok.Data;

@Data
public class WeeklyBoxOfficeListDto {

    public String weeklyBoxOfficeId;

    private String boxofficeType; // 박스오피스 종류

    private String showRange; // 대상 상영기간

    private String yearWeekTime; // 조회일자에 해당하는 연도와 주차를 출력

    private String rnum;           // 순번
    private String rank;           // 순위
    private String rankInten;      // 순위의 증감분
    private String rankOldAndNew;  // 신규 진입 여부
    private String movieCd;        // 영화 대표 코드
    private String movieNm;        // 영화명
    private String openDt;         // 개봉일
    private String salesAmt;         // 매출액
    private String salesShare;     // 매출액 점유율
    private String salesInten;       // 매출액 증감분
    private String salesChange;    // 매출액 증감률
    private String salesAcc;         // 누적 매출액
    private String audiCnt;          // 관객수
    private String audiInten;        // 관객수 증감분
    private String audiChange;     // 관객수 증감률
    private String audiAcc;          // 누적 관객수
    private String scrnCnt;       // 상영 스크린 수
    private String showCnt;       // 상영 횟수
}
