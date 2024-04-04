package openApi.gwon.movieList.dto.movieList;


import lombok.Data;

import java.util.List;

@Data
public class MovieListDto {
    private String movieListId;
    private String movieCd;
    private String movieNm;
    private String movieNmEn;
    private String prdtYear;
    private String openDt;
    private String typeNm;
    private String prdtStatNm;
    private String nationAlt;
    private String genreAlt;
    private String repNationNm;
    private String repGenreNm;
    private Integer totCnt;

    // DB 저장할때는 directors , companys 필드
    private String directors;
    private String companys;


    // 클라이언트에 데이터 전달할 때 파싱된 directors , companies 필드
    // MovieListDto 클래스 내에서 JSON 문자열을 저장하는 필드는 제거하고, 클라이언트로 전달할 리스트 필드만 남겨두었습니다.
    // 이렇게 하면 JSON 문자열을 DTO에 저장하는 것보다 파싱된 객체 리스트를 직접 전달하는 방식이 더 명확하고 효율적이 됩니다.
    private List<Directors> directorsList; // 파싱된 감독 목록
    private List<Company> companiesList; // 파싱된 회사 목록

}
