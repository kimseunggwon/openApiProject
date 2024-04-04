package openApi.gwon.movieList.dto.movieDetail;

import lombok.Data;

@Data
public class Actor implements MovieRelatedEntity{
    private String movieCd;
    private String peopleNm;
    private String peopleNmEn;
    private String cast; // DB 에서는 컬럼명 CastName;
    private String castEn;


}
