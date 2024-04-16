package openApi.gwon.movieList.dto.companyDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FilmoDto {
    private Integer filmoid;

    @JsonProperty("companyCd")
    private String filmoCompanyCd;

    @JsonProperty("movieCd")
    private String filmoMovieCd;

    @JsonProperty("movieNm")
    private String filmoMovieNm;

    @JsonProperty("companyPartNm")
    private String filmoCompanyPartNm;
}
