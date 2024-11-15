<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>영화사 상세정보</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/companyInfoDetail.css?v=20240402">
</head>
<body>
<button id="back-button" class="back-button">뒤로 가기</button>


<div class="company-details-container">
    <h1 class="title">영화사 상세정보</h1>
    <table class="company-info-table">
        <tr>
            <td><strong>영화사 코드:</strong></td>
            <td id="companyCd"></td>
        </tr>
        <tr>
            <td><strong>영화사명:</strong></td>
            <td id="companyNm"></td>
        </tr>
        <tr>
            <td><strong>영문명:</strong></td>
            <td id="companyNmEn"></td>
        </tr>
        <tr>
            <td><strong>대표자명:</strong></td>
            <td id="ceoNm"></td>
        </tr>
    </table>

    <h1>분류</h1>
    <table id="parts-list" class="parts-list-table">
        <tbody></tbody>
    </table>

    <h1>필모그래피</h1>
    <table id="filmography">
        <thead>
        <tr>
            <th>영화 코드</th>
            <th>영화명</th>
            <th>참여 분류명</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>

    $(document).ready(function () {
        var companyCd = "${companyCd}";
        var movieListId = "${movieListId}";

        $('#companyCd').text(companyCd);

        //호출
        $.ajax({
            url: '/search-company',
            type: 'GET',
            data: {
                companyCd: companyCd,
                movieListId: movieListId
            },
            success: function (response) {
                if (response) {
                    $('#companyNm').text(response.companyNm);
                    $('#companyNmEn').text(response.companyNmEn);
                    $('#ceoNm').text(response.ceoNm);

                    var partsList = response.parts.map(function (part) {
                        return '<tr>'
                            + '<td>' + part.companyPartNm + '</td>'
                            + '</tr>';
                    }).join('');
                    $('#parts-list tbody').html(partsList);

                    var filmographyList = response.filmos.map(function (filmo) {
                        return '<tr>'
                            + '<td>' + filmo.movieCd + '</td>'
                            + '<td>' + filmo.movieNm + '</td>'
                            + '<td>' + filmo.companyPartNm + '</td>'
                            + '</tr>';
                    }).join('');
                    $('#filmography tbody').html(filmographyList);
                }
            },
            error: function(xhr, status, error) {
                console.error("Error fetching company details:", status, error);
            }
        });
    });

    $('#back-button').on('click', function() {
        window.history.back();
    });

</script>

</body>
</html>
