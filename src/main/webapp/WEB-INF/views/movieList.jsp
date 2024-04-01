<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>영화 목록</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            width: 80%;
            margin: auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .search-container,
        .sorting-container {
            margin-bottom: 20px;
            background-color: #f9f9f9;
            padding: 10px;
            border: 1px solid #ddd;
        }
        .search-container:after,
        .sorting-container:after {
            content: "";
            display: table;
            clear: both;
        }
        input[type=text],
        button,
        select {
            padding: 10px;
            margin-right: 10px;
            border: 1px solid #ddd;
        }
        button {
            background-color: #5CACEE;
            color: white;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: #5599FF;
        }
        .total-count {
            float: left;
        }
        .sorting-select {
            float: right;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th,
        td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #eee;
        }
        .no-result {
            text-align: center;
            padding: 20px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="search-container">
        <input type="text" id="movie-name" placeholder="영화명">
        <input type="text" id="director-name" placeholder="감독명">
        <input type="text" id="production-year" placeholder="제작연도">
        <input type="text" id="release-date" placeholder="개봉일자">
        <button>조회</button>
        <button>초기화</button>
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
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
    $(document).ready(function () {
        function fetchMovies(page = 1, size = 10) {
            $.ajax({
                url : '/search/movie',
                type : 'GET',
                data : {
                    page : page,
                    size : size,
                },
                success : function (response) {
                    $('#total-count').text(response.totalMovies);
                    const movies = response.movies;
                    const tbody = $('#movie-list tbody');
                    tbody.empty(); // 기존 목록을 비우고 새 목록으로 채운다


                    if (movies.length === 0) {
                        $('.no-result').show();
                    } else {
                        $('.no-result').show();

                        $.each(movies, function (i,movie) {
                            // 감독 정보 파싱
                            var directorsNames = movie.directorsList.map(function(director) {
                                return director.peopleNm;
                            }).join(", ") || '';

                            // 제작사 정보 파싱
                            var companiesNames = movie.companiesList.map(function(company) {
                                return company.companyNm;
                            }).join(", ") || '';

                            // 개봉일을 YYYY.MM.DD 형식으로 변환하는 코드
                            var formattedOpenDt = movie.openDt.substring(0, 4) + '.' +
                                movie.openDt.substring(4, 6) + '.' +
                                movie.openDt.substring(6, 8);

                            const tr = $('<tr>').append(
                                $('<td>').text(movie.movieNm),
                                $('<td>').text(movie.movieNmEn),
                                $('<td>').text(movie.movieCd),
                                $('<td>').text(movie.prdtYear),
                                $('<td>').text(formattedOpenDt),
                                $('<td>').text(movie.nationAlt),
                                $('<td>').text(movie.typeNm),
                                $('<td>').text(movie.genreAlt),
                                $('<td>').text(movie.prdtStatNm),
                                $('<td>').text(directorsNames), // 여기서 directorsJson을 파싱하여 표시
                                $('<td>').text(companiesNames) // companiesJson도 마찬가지로 파싱하여 표시
                            );
                            tbody.append(tr)
                        });
                    }
                },
               error : function(xhr, status, error) {
                   console.error("Error fetching movies:", status, error);
               }
            });
        }

        // 페이지 로드 시 영화 목록을 바로 가져온다
        fetchMovies();

        //조회 버튼 클릭시 이벤트 바인딩 todo :
        $('.search-container button').click(function() {
            fetchMovies();
        });

        // 초기화 버튼 클릭시 todo :

        // 정렬 드롭다운 변경 이벤트 todo :
        $('#sorting').change(function() {
            // 여기서 선택된 정렬 옵션에 따라 fetchMovies를 호출
        });

    });
</script>

</body>
</html>
