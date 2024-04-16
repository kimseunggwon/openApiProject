package openApi.gwon.movieList.dto.companyDetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDetailsDto {

    @JsonProperty("companyCd")
    private String companyDetailCd;

    @JsonProperty("companyNm")
    private String companyDetailNm;

    @JsonProperty("companyNmEn")
    private String companyDetailNmEn;
    @JsonProperty("ceoNm")
    private String ceoNm;

    @JsonProperty("movieListId")
    private String movieListId;

    private List<PartDto> parts;
    private List<FilmoDto> filmos;
}
