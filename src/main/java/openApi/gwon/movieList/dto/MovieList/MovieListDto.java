package openApi.gwon.movieList.dto.MovieList;


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
    private String directorsJson; // JSON 문자열로 변환된 감독 리스트
    private String companiesJson; // JSON 문자열로 변환된 회사 리스트

}
