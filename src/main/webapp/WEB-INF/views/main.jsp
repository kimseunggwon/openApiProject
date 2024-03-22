<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>



<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>일별 박스오피스 조회</title>
    <style>
        /* 기본 스타일 설정 */
        body {
            font-family: Arial, sans-serif;
            padding: 0;
            margin: 0;
            background: #f0f0f0;
        }
        .container {
            width: 90%;
            margin: 20px auto;
            background: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        header {
            background: #333;
            color: #fff;
            padding-top: 30px;
            min-height: 70px;
            border-bottom: #0779e4 1px solid;
            text-align: center;
        }
        header h1 {
            margin: 0;
            padding: 10px 0;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 15px;
            border: 1px solid #ddd;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .footer-info {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 1em;
            font-weight: bold;
            margin-top: 20px;
        }
        .footer-info div {
            margin-right: 10px;
        }
        .left {
            text-align: left;
        }
        .right {
            text-align: right;
        }
    </style>
</head>
<body>
<header>
    <h1>일별 박스오피스 조회</h1>
</header>

<div class="container">
    <div class="date-input">
        <label for="targetDt">날짜 선택:</label>
        <input type="date" id="targetDt" name="targetDt">
        <button onclick="searchBoxOffice()">조회</button>
    </div>

<div class="container">
    <div class="footer-info">
        <div class="left">총 <span id="totalCount">0</span></div>
        <div class="right" id="searchTime">조회일시: </div>
    </div>
    <table id="boxOffice">
        <thead>
        <tr>
            <th>순위</th>
            <th>영화명</th>
            <th>개봉일</th>
            <th>당일 매출액</th>
            <th>매출액 대비 비율</th>
            <th>당일 관객수</th>
            <th>누적 관객수</th>
        </tr>
        </thead>
        <tbody>
        <!-- AJAX를 통해 여기에 영화 정보가 동적으로 추가될 것임 -->
        </tbody>
    </table>
</div>

<script>
    // AJAX 요청을 통해 박스오피스 데이터를 불러오고,
    // DOM을 사용하여 #boxOffice 내에 영화 정보 및 footer-info에 건수와 조회일시를 동적으로 추가하는 자바스크립트 코드 작성
    function searchBoxOffice() {
        var targetDt = $('#targetDt').val().replace(/-/g, ''); // 날짜를 yyyy-mm-dd 형식에서 yyyyMMdd 형식으로 변환
        $.ajax({
            url : '/search/dailyBoxOffice', // 요청 URL
            type : 'GET',
            data : {
                'targetDt' : targetDt
            },
            dataType : 'json', // 응답 데이터 타입
            success : function (data) {
                updateTable(data); // 테이블 업데이트
                console.log("data " + data);

                // 추가적인 건수와 조회일시도 업데이트
                $('#totalCount').text(data.length + '건 조회');
                $('#searchTime').text('조회일시:' + targetDt);
            },
            error:function (request,status,error) { //요청 실패했을 때 실행 함수
                alert('박스오피스 데이터를 불러오는 데 실패했습니다.');
                console.log("Error : " + error);
            }
        });
    }

    //테이블에 데이터 동적으로 추가
    function updateTable(data) {
        var tbody = $('#boxOffice tbody');
        tbody.empty(); // 기존 테이블 내용 삭제

        $.each(data,function (index , boxOffice) {
            var row = $('<tr>').append(
                $('<td>').text(boxOffice.rank),
                $('<td>').text(boxOffice.movieNm),
                $('<td>').text(boxOffice.openDt),
                $('<td>').text(boxOffice.salesAmt),
                $('<td>').text(boxOffice.salesShare),
                $('<td>').text(boxOffice.audiCnt),
                $('<td>').text(boxOffice.audiAcc)
            );
            tbody.append(row);
        });
    }

</script>
</body>
</html>
