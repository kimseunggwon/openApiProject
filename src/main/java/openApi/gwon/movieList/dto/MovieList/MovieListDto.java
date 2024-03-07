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
    private List<Directors> directorsList;
    private List<Company> companyList;

    private Integer totCnt;
}
