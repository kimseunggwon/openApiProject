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

<script>
    // 여기에 JavaScript를 사용하여 조회 및 초기화 버튼 이벤트를 처리하고, 정렬 기능을 구현합니다.
</script>

</body>
</html>
