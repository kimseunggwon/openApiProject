package openApi.gwon.movieList.dto.movieDetail;


import lombok.Data;

@Data
public class Company implements MovieRelatedEntity{
    private String movieCd;
    private String companyCd;
    private String companyNm;
    private String companyNmEn;
    private String companyPartNm;
}
