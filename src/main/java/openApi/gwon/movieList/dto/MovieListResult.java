package openApi.gwon.movieList.dto;


import lombok.Data;

import java.util.List;

@Data
public class MovieListResult {

    private int totCnt;
    private String source;
    private List<MovieListDto> movieList;


}
