package openApi.gwon.movieList.repository;

import lombok.RequiredArgsConstructor;
import openApi.gwon.movieList.dto.companyDetail.CompanyDetailsDto;
import openApi.gwon.movieList.dto.companyDetail.FilmoDto;
import openApi.gwon.movieList.dto.companyDetail.PartDto;
import openApi.gwon.movieList.mapper.MovieCompanyDetailMapper;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class MovieCompanyDetailImplRepository implements MovieCompanyDetailRepository{

    private final MovieCompanyDetailMapper movieCompanyDetailMapper;

    @Override
    public void saveMovieDetail(CompanyDetailsDto companyDetailsDto) {
        movieCompanyDetailMapper.saveMovieDetail(companyDetailsDto);
    }

    @Override
    public void insertFilmo(FilmoDto filmoDto) {
        movieCompanyDetailMapper.insertFilmo(filmoDto);
    }

    @Override
    public void insertCompanyPart(PartDto partDto) {
        movieCompanyDetailMapper.insertCompanyPart(partDto);
    }
}
