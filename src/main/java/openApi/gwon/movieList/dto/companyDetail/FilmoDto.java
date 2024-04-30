package openApi.gwon.movieList.dto.companyDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class FilmoDto {

    @JsonProperty("companyCd")
    private String filmoCompanyCd;

    @JsonProperty("movieCd")
    private String filmoMovieCd;

    @JsonProperty("movieNm")
    private String filmoMovieNm;

    @JsonProperty("companyPartNm")
    //@JsonSerialize(using = ToStringSerializer.class) // JSON 배열을 문자열로 변환
    //@JsonDeserialize(as = List.class) // 문자열을 JSON 배열로 변환
    private String filmoCompanyPartNm; // DB 저장용 직렬화된 JSON 문자열이 필요하다면 이를 처리하는 로직 추가 필요
    //private String filmoCompanyPartNmSerialized; // DB 저장용 직렬화된 JSON 문자열

}
