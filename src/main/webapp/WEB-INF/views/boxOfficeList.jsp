<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/boxOffice.css?v=20231121">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>박스오피스 조회</title>
</head>
<body>

<div class="title-container">
    <h1 class="main-title">박스오피스 조회</h1>
</div>

<div class="title-container1">
  <a href="${pageContext.request.contextPath}/main/movieList.do">영화목록 보러가기</a>
</div>



<div class="container">
    <div class="title">
        <h2>일별 박스오피스 조회</h2>
    </div>
    <div class="date-input">
        <label for="targetDailyDt">날짜 선택:</label>
        <input type="date" id="targetDailyDt" name="targetDailyDt">
        <button onclick="searchBoxOffice()">조회</button>
    </div>

    <div class="container">
        <div class="footer-info">
            <div class="left">총 <span id="totalCount">0</span></div>
            <div class="right" id="searchTime">조회일시:</div>
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
</div>

    <div class="container">
        <div class="title">
            <h2>주간/주말 박스오피스 조회</h2>
        </div>
        <div class="date-input">
            <label for="targetWeeklyDt">날짜 선택:</label>
            <input type="date" id="targetWeeklyDt" name="targetWeeklyDt">
            <label for="repNationCd">영화 국적:</label>
            <select id="repNationCd">
                <option value="">전체</option>
                <option value="K">한국영화</option>
                <option value="F">외국영화</option>
            </select>
            <label for="weekGb">기간 선택:</label>
            <select id="weekGb">
                <option value="0">주간</option>
                <option value="1" selected>주말</option>
                <option value="2">주중</option>
            </select>
            <button onclick="searchBoxOfficeWeekly()">조회</button>
        </div>

        <div class="container">
        <div class="footer-info">
            <div class="left">총 <span id="totalWeeklyCount">0</span>건</div>
            <div class="right" id="weeklySearchTime">조회일시: </div>
        </div>

        <table id="weeklyBoxOffice">
            <thead>
            <tr>
                <th>순위</th>
                <th>변동</th>
                <th>신규</th>
                <th>영화명</th>
                <th>개봉일</th>
                <th>매출액</th>
                <th>매출 비율</th>
                <th>관객수</th>
                <th>누적 관객수</th>
                <th>상영 스크린수</th>
            </tr>
            </thead>
            <tbody>
            <!-- AJAX를 통해 여기에 주간/주말 영화 정보가 동적으로 추가될 것임 -->
            </tbody>
        </table>
        </div>


        <script>
            // AJAX 요청을 통해 박스오피스 데이터를 불러오고,
            // DOM을 사용하여 #boxOffice 내에 영화 정보 및 footer-info에 건수와 조회일시를 동적으로 추가하는 자바스크립트 코드 작성
            function searchBoxOffice() {
                var inputDateString = $('#targetDailyDt').val();
                console.log("inputDateString " + inputDateString);
                if (isDateInFuture(inputDateString)) {
                    alert('오늘 이후의 데이터는 조회할 수 없습니다.');
                    return;
                }

                var targetDt = $('#targetDailyDt').val().replace(/-/g, ''); // 날짜를 yyyy-mm-dd 형식에서 yyyyMMdd 형식으로 변환
                $.ajax({
                    url: '/search/dailyBoxOffice', // 요청 URL
                    type: 'GET',
                    data: {
                        'targetDt': targetDt
                    },
                    dataType: 'json', // 응답 데이터 타입
                    success: function (data) {
                        updateTable(data); // 테이블 업데이트
                        console.log("data " + data);

                        // 추가적인 건수와 조회일시도 업데이트
                        $('#totalCount').text(data.length + '건 조회');
                        $('#searchTime').text('조회일시:' + targetDt);
                    },
                    error: function (request, status, error) { //요청 실패했을 때 실행 함수
                        alert('박스오피스 데이터를 불러오는 데 실패했습니다.');
                        console.log("Error : " + error);
                    }
                });
            }

            //테이블에 데이터 동적으로 추가
            function updateTable(data) {
                var tbody = $('#boxOffice tbody');
                tbody.empty(); // 기존 테이블 내용 삭제

                $.each(data, function (index, boxOffice) {
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


            function searchBoxOfficeWeekly() {
                var inputDateString = $('#targetWeeklyDt').val();
                console.log("inputDateString " + inputDateString);
                if (isDateInFuture(inputDateString)) {
                    alert('오늘 이후의 데이터는 조회할 수 없습니다.');
                    return;
                }

                var targetDt = $('#targetWeeklyDt').val().replace(/-/g,''); // 날짜를 yyyy-mm-dd 형식에서 yyyyMMdd 형식으로 변환
                var repNationCd = $('#repNationCd').val(); // 영화 국적 값 가져오기 // “K: : 한국영화 “F” : 외국영화 (default : 전체)
                var weekGb = $('#weekGb').val(); // 기간 선택 값 가져오기 // “0” : 주간 (월~일) , “1” : 주말 (금~일) (default) , “2” : 주중 (월~목)

                $.ajax({
                    url : '/search/weeklyBoxOffice',
                    type: 'GET',
                    data: {
                        'targetDt' : targetDt,
                        'weekGb' : weekGb,
                        'repNationCd' : repNationCd
                    },
                    dataType: 'json',
                    success: function (data) {
                        updateWeeklyTable(data); // 주간,주말 테이블 업데이트 함수
                        console.log("Weekly data: ", data);

                        $('#weeklySearchTime').text('조회일시 : ' +  targetDt);

                    },
                    error : function (request ,status , error) {
                        alert('주간/주말 박스오피스 데이터를 불러오는 데 실패했습니다.');
                        console.log("Error: ", error);
                    }
                });
            }

            // 주간/주말 박스오피스 테이블에 데이터 동적으로 추가하는 함수
            function updateWeeklyTable(data) {
                var weeklyList = data.boxOfficeResult.weeklyBoxOfficeList; // 주간 박스오피스 리스트 가져오기
                var tbody = $('#weeklyBoxOffice tbody');
                tbody.empty(); // 기존 테이블 삭제
                $('#totalWeeklyCount').text(weeklyList.length + '건 조회');

                $.each(weeklyList, function (index,boxOffice) {
                    var row = $('<tr>').append(
                        $('<td>').text(boxOffice.rank),
                        $('<td>').text(boxOffice.rankInten), // 증감 정보 추가
                        $('<td>').text(boxOffice.rankOldAndNew), // 신규 여부 추가
                        $('<td>').text(boxOffice.movieNm),
                        $('<td>').text(boxOffice.openDt),
                        $('<td>').text(boxOffice.salesAmt),
                        $('<td>').text(boxOffice.salesShare),
                        $('<td>').text(boxOffice.audiCnt),
                        $('<td>').text(boxOffice.audiAcc),
                        $('<td>').text(boxOffice.scrnCnt) // 상영 스크린수 추가
                    );
                    tbody.append(row);
                })

            }

            // 사용자 입력 날짜가 현재 날짜보다 미래인지 아닌지 확인 함수
            function isDateInFuture(inputDateString) {
                var inputDate = new Date(inputDateString); // 사용자가 입력 날짜
                inputDate.setHours(0,0,0,0); // 입력 날짜의 시간을 00:00:00으로 설정 -> 단순 날짜 단위로만 비교하기 위해
                var currentDate = new Date();
                currentDate.setHours(0,0,0,0); // 현재 날짜의 시간을 00:00:00으로 설정
                return inputDate > currentDate; // 입력 날짜가 미래인 경우 true 반환
            }
        </script>

</body>
</html>
