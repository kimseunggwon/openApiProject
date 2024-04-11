package openApi.gwon.movieList.dto.companyDetail;

import lombok.Data;

import java.util.List;

@Data
public class CompanyDetailsDto {
    private String companyCd;
    private String companyNm;
    private String companyNmEn;
    private String ceoNm;
    private List<PartDto> parts;
    private List<FilmoDto> filmos;
}
