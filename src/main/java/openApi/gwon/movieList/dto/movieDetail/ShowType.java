package openApi.gwon.movieList.dto.movieDetail;

import lombok.Data;

@Data
public class ShowType implements MovieRelatedEntity{
    private String movieCd;
    private String showTypeGroupNm;
    private String showTypeNm;
    private String auditNo;
    private String watchGradeNm;
}
