package openApi.gwon.movieList.dto.movieDetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 알려지지 않은 속성을 무시하도록 설정 ( nations = 국가 )
public class MovieDetail {
    private String movieCd;
    private String movieNm;
    private String movieNmEn;
    private String movieNmOg;
    private String prdtYear;
    private String showTm;
    private String openDt;
    private String prdtStatNm;
    private String typeNm;
    private String nationNm;
    private String genreNm;
    private List<Actor> actors;
    private List<Company> companies;
    private List<ShowType> showTypes;
    private List<Staff> staffs;


    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public List<ShowType> getShowTypes() {
        return showTypes;
    }

    public void setShowTypes(List<ShowType> showTypes) {
        this.showTypes = showTypes;
    }

    public List<Staff> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<Staff> staffs) {
        this.staffs = staffs;
    }
}
