package openApi.gwon.movieList.mapper;

import openApi.gwon.movieList.dto.companyDetail.CompanyDetailsDto;
import openApi.gwon.movieList.dto.companyDetail.FilmoDto;
import openApi.gwon.movieList.dto.companyDetail.PartDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MovieCompanyDetailMapper {

    void saveMovieDetail(CompanyDetailsDto companyDetailsDto);

    void insertFilmo(FilmoDto filmoDto);

    void insertCompanyPart(PartDto partDto);

    CompanyDetailsDto selectCompanyDetailsByMovieListId(@Param("movieListId") String movieListId, @Param("companyCd") String companyCd);

}
