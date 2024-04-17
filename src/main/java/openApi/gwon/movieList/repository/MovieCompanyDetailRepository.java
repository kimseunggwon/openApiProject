package openApi.gwon.movieList.repository;

import openApi.gwon.movieList.dto.companyDetail.CompanyDetailsDto;
import openApi.gwon.movieList.dto.companyDetail.FilmoDto;
import openApi.gwon.movieList.dto.companyDetail.PartDto;

public interface MovieCompanyDetailRepository {

    void saveMovieDetail(CompanyDetailsDto companyDetailsDto);

    void insertFilmo(FilmoDto filmoDto);

    void insertCompanyPart(PartDto partDto);
}
