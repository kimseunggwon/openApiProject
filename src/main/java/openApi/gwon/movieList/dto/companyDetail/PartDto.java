package openApi.gwon.movieList.dto.companyDetail;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PartDto {

    @JsonProperty("movieCd")
    private String flmoMovieCd;
    @JsonProperty("movieNm")
    private String flmoMovieNm;
    @JsonProperty("companyPartNm")
    private String companyPartNm;
}
