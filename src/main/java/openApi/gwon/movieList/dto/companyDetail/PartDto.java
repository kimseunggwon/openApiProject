package openApi.gwon.movieList.dto.companyDetail;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PartDto {

    private Integer partId;

    @JsonProperty("companyCd")
    private String partCompanyCd;

    @JsonProperty("companyPartNm")
    private String partCompanyPartNm;
}
