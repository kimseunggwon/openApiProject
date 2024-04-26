package openApi.gwon.movieList.dto.companyDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

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
    @JsonSerialize(using = ToStringSerializer.class) // JSON 배열을 문자열로 변환
    @JsonDeserialize(as = List.class) // 문자열을 JSON 배열로 변환
    private List<String> filmoCompanyPartNms;


}
