package openApi.gwon.movieList.dto.movieDetail;

import lombok.Data;

@Data
public class Staff implements MovieRelatedEntity{
    private String movieCd;
    private String peopleNm;
    private String peopleNmEn;
    private String staffRoleNm;
}

