package openApi.gwon.movieList.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("openApi.gwon.movieList.mapper")
public class MybatisConfig {

}

/**  mapper 와 repository 차이점?
 *   repository는 mapper를 포함하고 있다.
 *   mapper는 00.xml과 같이 sql문을 정의해놓은 파일과 많이 사용한다. SQL문을 정의하고 그 결과를 정의해놓은 모델에 매핑시키는 ibatis(mybatis) 방식에서 사용하는 것으로
 *   mapper는 매핑이라는 단어에서 유추할 수 있듯이 sql문(xml)을 메소드(java)로 매핑 시켜주는 것을 의미한다.
 즉, 우리가 정의해놓은 sql와 개발할 때 사용하는 메소드를 연결하고 결과 값을 정의해놓은 타입으로 매핑 시켜주는 것이다.
 VS
 repository(dao)는 비즈니스로직에서 db의 데이터를 조회 및 조작하는 것을 비즈니스 로직과 분리하기 위한 것으로 db랑 연결하는 것이 강한
 mapper와 달리 db의 데이터를 조회 및 조작하는 것에 중점을 뒀다는 것이 제일 큰 개념이다.

 @Configuration : 설정파일 만들고, bean 등록을 하기 위한
 @MapperScan : 패키지 경로를 지정하여 위치에있는 인터페이스들은 전부 매퍼로 사용
 */