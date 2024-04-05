package openApi.gwon.movieList.dto.movieDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Actor implements MovieRelatedEntity{
    private String movieCd;
    private String peopleNm;
    private String peopleNmEn;
    private String castName; // DB 에서는 컬럼명 CastName;
    private String castEn;

    // Json 파싱 시 'cast' 필드를 'castName' 으로 매핑하기 위한 설정
    // @JsonProperty :  cast 키의 값을 Actor 객체의 castName 필드에 자동으로 매핑
    @JsonProperty("cast")
    public void setCastName(String cast){
        this.castName = cast;
    }

}
