<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>영화사 상세정보</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/companyInfoDetail.css?v=20240402">
</head>
<body>
<div class="company-details-container">
    <h1>영화사 상세정보</h1>
    <div class="company-info">
        <p><strong>영화사 코드:</strong> <span id="companyCd"></span></p>
        <p><strong>영화사명:</strong> <span id="companyNm"></span></p>
        <p><strong>영문명:</strong> <span id="companyNmEn"></span></p>
        <p><strong>대표자명:</strong> <span id="ceoNm"></span></p>
    </div>
    <div class="company-parts">
        <h2>분류</h2>
        <ul id="parts-list"></ul>
    </div>
    <div class="company-filmography">
        <h2>필모그래피</h2>
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
</div>

<script src="script.js"></script>
</body>
</html>
