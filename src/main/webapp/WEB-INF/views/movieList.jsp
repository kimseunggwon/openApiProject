<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<!DOCTYPE html>
<html lang="ko">
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/movieList.css?v=20240402">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>영화 목록</title>
</head>
<body>

<div class="header-container">
    <h1 class="title">영화정보</h1>
    <%--<a id="box-office-button" class="box-office-button" href="http://localhost:8080/main/boxOfficeList.do">박스오피스 보러가기</a>--%>
    <a id="box-office-button" class="box-office-button" href="${pageContext.request.contextPath}/main/boxOfficeList.do">박스오피스
        보러가기</a>
</div>

<div class="container">
    <div class="search-container">
        <input type="text" id="movie-name" placeholder="영화명">
        <input type="text" id="director-name" placeholder="감독명">
        <input type="text" id="production-year" placeholder="제작연도">
        <!-- 개봉일자 시작일 -->
        <input type="text" id="release-date-start" class="date-input" onfocus="this.type='date'"
               onblur="this.type='text'" placeholder="시작 일자">
        <!-- 개봉일자 종료일 -->
        <input type="text" id="release-date-end" class="date-input" onfocus="this.type='date'" onblur="this.type='text'"
               placeholder="종료 일자">

        <div class="button-container">
            <button id="search-button">조회</button>
            <button id="reset-button">초기화</button>
        </div>
    </div>

    <div class="sorting-container">
        <div class="total-count">
            총 <span id="total-count">0</span>건
        </div>
        <div class="sorting-select">
            정렬:
            <select id="sorting">
                <option value="update">최신업데이트순</option>
                <option value="year">제작연도순</option>
                <option value="name">영화명순 (ㄱ ~ Z)</option>
                <option value="release">개봉일순</option>
            </select>
        </div>
    </div>

    <table id="movie-list">
        <thead>
        <tr>
            <th>영화명</th>
            <th>영화명(영문)</th>
            <th>영화코드</th>
            <th>제작연도</th>
            <th>개봉일</th>
            <th>제작국가</th>
            <th>유형</th>
            <th>장르</th>
            <th>제작상태</th>
            <th>감독</th>
            <th>제작사</th>
        </tr>
        </thead>
        <tbody>
        <!-- 서버에서 영화 데이터를 받아와서 테이블 행을 추가하는 JavaScript 코드 작성 필요 -->
        </tbody>
    </table>
    <div class="no-result" style="display: none;">검색 결과가 없습니다.</div>

    <div class="pagination-container">
        <button id="prev-page">이전</button>
        <span id="current-page">1</span> / <span id="total-pages">0</span>
        <button id="next-page">다음</button>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
    $(document).ready(function () {

        var currentPage = 1; // 현재 페이지 번호
        var totalMovie = 0; // 총 영화 수
        var pageSize = 10; // 페이지당 영화 수
        var totalPages = 0; // 총 페이지 수

        // 정렬 드롭다운 변경 이벤트
        $('#sorting').change(function () {
            // 저장된 검색 조건과 현재 선택된 정렬 옵션으로 영화 목록을 다시 가져옵니다.
            fetchMovies(currentPage, pageSize, $(this).val(), currentSearchConditions.movieName, currentSearchConditions.directorName, currentSearchConditions.productionYear, currentSearchConditions.openDateStart, currentSearchConditions.openDateEnd);
        })

        //페이지네이션 버튼 이벤트 핸들러
        function handlePaginationClick(newPage) {
            currentPage = newPage;

            //현재 검색 및 정렬 조건을 유지하면서 새 페이지의 영화 목록을 가져옵니다.
            fetchMovies(currentPage, pageSize, $('#sorting').val(), currentSearchConditions.movieName, currentSearchConditions.directorName, currentSearchConditions.productionYear, currentSearchConditions.openDateStart, currentSearchConditions.openDateEnd);
        }

        //검색 조건을 저장할 변수를 전역으로 선언
        var currentSearchConditions = {
            movieName: '',
            directorName: '',
            productionYear: '',
            openDateStart: '',
            openDateEnd: '',
        }

        // 조회 버튼 클릭 이벤트
        $('#search-button').click(function () {
            currentSearchConditions.movieName = $('#movie-name').val();
            currentSearchConditions.directorName = $('#director-name').val();
            currentSearchConditions.productionYear = $('#production-year').val();
            currentSearchConditions.openDateStart = formatDateToDb($('#release-date-start').val());
            currentSearchConditions.openDateEnd = formatDateToDb($('#release-date-end').val());


            // 첫 페이지부터 조회를 시작합니다. 여기서 currentSearchConditions 객체를 사용합니다.
            fetchMovies(1, pageSize, $('#sorting').val(), currentSearchConditions.movieName, currentSearchConditions.directorName, currentSearchConditions.productionYear, currentSearchConditions.openDateStart, currentSearchConditions.openDateEnd);
        })

        // 영화 데이터 불러오는 함수
        function fetchMovies(page, size, sortOption , movieName = '', directorName = '', productionYear = '', releaseDate = '', openDateStart = '', openDateEnd = '') {
            var queryData = {
                page: page,
                size: size,
                sort: sortOption, // 정렬 옵션을 queryData에 추가
                movieName: movieName,
                directorName: directorName,
                productionYear: productionYear,
                releaseDate: releaseDate,
                openDateStart: openDateStart,
                openDateEnd: openDateEnd
            };

            $.ajax({
                url: '/search/movie',
                type: 'GET',
                data: queryData,
                success: function (response) {
                    totalMovies = response.totalMovies;
                    totalPages = Math.ceil(totalMovies / pageSize); // 총 페이지 수 계산
                    $('#total-count').text(totalMovies);
                    $('#current-page').text(currentPage);
                    $('#total-pages').text(totalPages);

                    const movies = response.movies;
                    const tbody = $('#movie-list tbody');
                    tbody.empty(); // 기존 목록을 비우고 새 목록으로 채운다

                    if (movies.length === 0) {
                        $('.no-result').show();
                    } else {
                        $('.no-result').hide();

                        $.each(movies, function (i, movie) {
                            //debugger;

                            // 서버로 받은 파싱된 리스트를 감독과 제작사 이름을 문자열로 합친다
                            // map() 함수로 directorsList와 companiesList의 각 요소를 순회하면서 감독의 이름과 제작사의 이름을 추출하여 새로운 배열을 만들고 단일 문자열로 결합
                            var directorsNames = movie.directorsList.map(director => director.peopleNm).join(", ");
                            var companiesNames = movie.companiesList.map(company => company.companyNm).join(", ");
                            var companiesInfo = movie.companiesList.map(company => company.companyNm + " (" + company.companyCd + ")").join(", ");


                            // 개봉일을 YYYY.MM.DD 형식으로 변환하는 코드
                            var formattedOpenDt = movie.openDt && movie.openDt.length === 8 ?
                                movie.openDt.substring(0, 4) + '.' + movie.openDt.substring(4, 6) + '.' + movie.openDt.substring(6, 8) :
                                ''; // openDt가 유효하지 않으면 빈 문자열을 사용

                            // 영화 제목에 클릭 이벤트를 추가하여 상세 페이지로 리다이렉션할 수 있도록 함
                            const movieTitleLink = $('<a>')
                                .attr('href', 'javascript:void(0);')
                                .text(movie.movieNm)
                                .on('click', function () {
                                    // 상세 정보 페이지로 리다이렉트하는 로직
                                    window.location.href = 'new/movieList/Detail.do?movieCd=' + movie.movieCd;
                                });

                            // todo : 완
                            const companiesInfoLink = movie.companiesList.map(company => {
                                return $('<a>')
                                    .attr('href', 'javascript:void(0);')
                                    .text(company.companyNm + " (" + company.companyCd + ")")
                                    .on('click', function () {
                                        // 상세 정보 페이지로 리다이렉트하는 로직
                                        window.location.href = '/company/info.do?companyCd=' + company.companyCd + '&movieListId=' + movie.movieListId;
                                    });
                            });
                            console.log("companiesInfoLink aa" + companiesInfoLink)


                            const tr = $('<tr>').append(
                                $('<td>').append(movieTitleLink),
                                $('<td>').text(movie.movieNmEn),
                                $('<td>').text(movie.movieCd),
                                $('<td>').text(movie.prdtYear),
                                $('<td>').text(formattedOpenDt),
                                $('<td>').text(movie.nationAlt),
                                $('<td>').text(movie.typeNm),
                                $('<td>').text(movie.genreAlt),
                                $('<td>').text(movie.prdtStatNm),
                                $('<td>').text(directorsNames), // 여기서 directorsJson을 파싱하여 표시
                                $('<td>').append(companiesInfoLink) // companiesJson도 마찬가지로 파싱하여 표시
                            );
                            tbody.append(tr)
                            $('#movie-list tbody').append(tr);
                        });
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching movies:", status, error);
                    $('.no-result').show(); // 에러 발생 시에도 메시지를 보여줌
                }
            });
        }

        // 초기화 버튼 클릭 이벤트
        $('#reset-button').click(function () {
            // 모든 입력 필드 초기화
            $('#movie-name').val('');
            $('#director-name').val('');
            $('#production-year').val('');
            $('#release-date-start').val('');
            $('#release-date-end').val('');

            // 검색 조건 및 정렬 조건 초기화
            currentSearchConditions = {
                movieName: '',
                directorName: '',
                productionYear: '',
                openDateStart: '',
                openDateEnd: '',
            };
            $('#sorting').val('update'); // 드롭다운을 '최신업데이트순'으로 설정
            currentPage = 1; //현재 페이지를 1로 설정
            $('#current-page').text(currentPage) // 페이지네이션을 첫 페이지로 업데이트

            // 필터 없이 첫 페이지부터 다시 로드
            fetchMovies(currentPage, pageSize , 'update');
        });

        // fetchMovies 함수 외부에 있어야한다
        // 이전 페이지 버튼
        $('#prev-page').click(function () {
            if (currentPage > 1) {
                handlePaginationClick(currentPage - 1);
            }
        });

        // 다음 페이지 버튼
        $('#next-page').click(function () {
            if (currentPage < totalPages) {
                handlePaginationClick(currentPage + 1);
            }
        });

        // 페이지 로드 시 첫 페이지의 영화 목록을 바로 가져온다
        fetchMovies(currentPage, pageSize);

        //조회 버튼 클릭시 이벤트 바인딩 todo :
        $('.search-container button').click(function () {
            fetchMovies();
        });

    });
</script>
<script>
    //날짜 형식 : YYYMMDD
    function formatDateToDb(dateString) {
        return dateString.replace(/-/g, '');
    }

    /*   // 박스오피스 이동
       $(document).ready(function() {
           // 박스오피스 보러가기 버튼 클릭 이벤트 핸들러
           $('#box-office-button').click(function() {
               // boxOfficeList.jsp 페이지로 이동합니다.
               window.location.href = '/main/boxOfficeList.do';
           });
       });*/
</script>

</body>
</html>
