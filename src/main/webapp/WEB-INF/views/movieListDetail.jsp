<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>영화상세정보</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/movieListDetail.css?v=20240402">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<button id="back-button" class="back-button">뒤로 가기</button>

<div id="movie-details" class="movie-details">
    <h2 id="movie-title"></h2>
    <p><strong>제작연도:</strong> <span id="production-year"></span></p>
    <p><strong>개봉일:</strong> <span id="release-date"></span></p>
    <p><strong>장르:</strong> <span id="genre"></span></p>
    <p><strong>국가:</strong> <span id="country"></span></p>
    <p><strong>상태:</strong> <span id="status"></span></p>
    <p><strong>상영시간:</strong> <span id="running-time"></span>분</p>

    <h3>배우</h3>
    <ul id="actors-list"></ul>

    <h3>제작사</h3>
    <ul id="companies-list"></ul>

    <h3>상영 유형</h3>
    <ul id="showtypes-list"></ul>

    <h3>스태프</h3>
    <ul id="staffs-list"></ul>
</div>




<script>
    $(document).ready(function (){

        var movieCd = '${movieCd}';

        $.ajax({
            url:'/find/movieDetail',
            type:'GET',
            data:{ movieCd : movieCd },
            success : function (response) {
                // 응답 데이터
                console.log(response)

                // 응답 데이터를 HTML 요소에 설정
                $('#movie-title').text(response.movieNm + " (" + response.movieNmEn + ")");
                $('#production-year').text(response.prdtYear);
                $('#release-date').text(response.openDt);
                $('#genre').text(response.genreNm);
                $('#country').text(response.nationNm);
                $('#status').text(response.prdtStatNm);
                $('#running-time').text(response.showTm)

                // 배우 목록에 항목 추가
                response.actors.forEach(function(actor) {
                    var actorInfo = '<span class="actor-name">' + actor.peopleNm + '</span>';
                    if (actor.peopleNmEn) actorInfo += " (" + actor.peopleNmEn + ")";
                    if (actor.cast || actor.castEn) {
                        actorInfo += ' - <span class="actor-role">배역: ' + (actor.cast ? actor.cast : '') + (actor.castEn ? " (" + actor.castEn + ")" : '') + '</span>';
                    }
                    $('#actors-list').append($('<li>').html(actorInfo));
                });

                 //제작사 목록에 항목 추가
                response.companies.forEach(function(company) {
                    var companyInfo = '<span class="company-name">' + company.companyNm + '</span>';
                    if (company.companyNmEn) companyInfo += " (" + company.companyNmEn + ")";
                    companyInfo += ' - <span class="company-code">제작사 코드: ' + company.companyCd + '</span>';
                    $('#companies-list').append($('<li>').html(companyInfo));
                });

                // 상영 유형 목록 추가
                response.showTypes.forEach(function(showType) {
                    var showTypeInfo = showType.showTypeGroupNm + " - " + showType.showTypeNm;
                    if (showType.watchGradeNm) showTypeInfo += " - " + showType.watchGradeNm;
                    if (showType.auditNo) showTypeInfo += " (" + showType.auditNo + ")";
                    $('#showtypes-list').append($('<li>').text(showTypeInfo));
                });

                // 스태프 목록 추가
                response.staffs.forEach(function(staff) {
                    var staffInfo = staff.peopleNm;
                    if (staff.peopleNmEn) staffInfo += " (" + staff.peopleNmEn + ")";
                    if (staff.staffRoleNm) staffInfo += " - " + staff.staffRoleNm;
                    $('#staffs-list').append($('<li>').text(staffInfo));
                });


            },
            error : function (status , error) {
                console.error('영화 상세 정보를 가져오는데 실패했습니다.', status, error);
            }

        })

        $('#back-button').on('click', function() {
            window.history.back();
        });
    })

</script>
</body>
</html>
