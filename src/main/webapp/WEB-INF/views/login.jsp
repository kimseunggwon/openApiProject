<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            padding: 100px;
        }

        .login-container {
            background-color: #fff;
            padding: 90px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 300px;
            margin: auto;
        }

        input[type=text], input[type=password] {
            padding: 10px;
            margin-bottom: 10px;
            width: 100%;
            border: 1px solid #ddd;
        }

        button {
            width: 100%;
            padding: 10px;
            background-color: #5CACEE;
            color: white;
            border: none;
            cursor: pointer;
        }

        button:hover {
            background-color: #5599FF;
        }

        .findId {
            width: auto;
            padding: 5px 15px;
            background-color: #FFA07A; /* ID, PW 찾기 버튼 색상 변경 */
            color: white;
            border: none;
            cursor: pointer;
            margin-right: 5px;
        }

        .findId:hover {
            background-color: #FF6347;
        }

        .popup {
            display: none;
            position: fixed;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
            background-color: white;
            padding: 50px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
            width: 500px;
        }

        .popup button {
            background-color: #FF6347;
        }

        .popup button:hover {
            background-color: #FF4500;
        }


        .overlay {
            display: none;
            position: fixed;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
        }
    </style>
</head>
<body>
<div class="login-container">
    <h1>로그인</h1>
    <form action="${pageContext.request.contextPath}/login.do" method="post" onsubmit="return validateLoginForm()">
        <div>
            <label for="username">아이디 ID</label>
            <input type="text" id="username" name="username"/>
        </div>
        <div>
            <label for="password">비밀번호 PW</label>
            <input type="password" id="password" name="password"/>
        </div>
        <div>
            <button type="submit" id="loginButton">로그인</button>
        </div>
    </form>
    <p>아직 계정이 없으신가요? <a href="${pageContext.request.contextPath}/register.do">회원가입</a></p>
    <div>
        <button class="findId" onclick="openPopup('findIdPopup')">ID 찾기</button>
        <button class="findId" onclick="openPopup('findPwPopup')">PW 찾기</button>
    </div>
</div>


<!-- ID 찾기 팝업 -->
<div id="findIdPopup" class="popup">
    <h3>ID 찾기</h3>
    <form action="${pageContext.request.contextPath}/findId.do" method="post" onsubmit="return validateFindIdForm()">
        <div class="form-group">
            <label for="username">이름:</label>
            <input type="text" name="id_name" id="id_name" placeholder="이름">
        </div>
        <div class="form-group">
            <label for="username">이메일:</label>
            <input type="text" name="id_email" id="id_email" placeholder="이메일">
        </div>
        <button type="submit">아이디 찾기</button>
    </form>
    <button onclick="closePopup('findIdPopup')">닫기</button>
</div>

<!-- ID 찾기 성공 팝업 -->
<div id="idFindSuccessPopup" class="popup" style="display: none;">
    <h3>회원님의 아이디는 다음과 같습니다:</h3>
    <p><strong>${username}</strong></p>
    <div class="form-group">
        <button onclick="window.location.href='${pageContext.request.contextPath}/login.do'">로그인하기</button>
        <button onclick="openPopup('findPwPopup')">비밀번호 찾기</button>
    </div>
</div>

<div id="idFindFailPopup" class="popup" style="display: none;">
    <h3>회원님의 정보를 찾을 수 없습니다.</h3>
    <div class="form-group">
        <button onclick="window.location.href='${pageContext.request.contextPath}/register.do'">회원가입</button>
        <button onclick="window.location.href='${pageContext.request.contextPath}/login.do'">로그인하기</button>
    </div>
</div>



<!-- PW 찾기 팝업 -->
<div id="findPwPopup" class="popup">
    <h3>PW 찾기</h3>
    <form>
        <div class="form-group">
            <label>아이디:</label>
            <input type="text" name="pw_id" id="pw_id" placeholder="아이디">
        </div>
        <div class="form-group">
            <label>이메일:</label>
            <input type="text" name="pw_email" id="pw_email" placeholder="이메일">
        </div>

        <button type="button" id="emailVerificationButton" onclick="sendEmailVerification()">이메일 인증</button>

        <div class="form-group" id="verificationCodeField" style="display: none;">
            <label>이메일 인증 코드:</label>
            <input type="text" id="verificationCode" placeholder="인증 코드">
        </div>
        <button type="button" id="verifyCodeButton" style="display: none;" onclick="verifyCode()">인증 확인</button>
    </form>
    <button onclick="closePopup('findPwPopup')">닫기</button>
</div>


<!-- PW 찾기 성공 팝업 -->
<div id="pwFindSuccessPopup" class="popup" style="display: none;">
    <h3>비밀번호 변경</h3>
    <form id="restPwForm" onsubmit="return resetPassword()">
        <div class="form-group">
            <label>새 비밀번호:</label>
            <input type="password" id="newPassword" placeholder="새 비밀번호">
        </div>
        <div class="form-group">
            <label>새 비밀번호 확인:</label>
            <input type="password" id="confirmPassword" placeholder="비밀번호 확인">
        </div>
        <button type="submit">비밀번호 변경</button>
    </form>
</div>
</body>

<script>

    // PW 찾기 검증 시작

    // 이메일 인증 전송
    function sendEmailVerification() {

        const pwId = $('#pw_id').val().trim();
        const pwEmail = $('#pw_email').val().trim();

        if (!pwId || !pwEmail){
            alert("아이디와 이메일을 입력해주세요.");
            return;
        }

        $.ajax({
            url : '/findPw.do',
            type: 'POST',
            data: {
                pw_id :pwId,
                pw_email : pwEmail
            },
            success: function (response) {
                if (response.success){
                    alert("인증 코드가 이메일로 전송되었습니다.")
                    $('#verificationCodeField').show();
                    $('#verifyCodeButton').show();
                } else {
                    alert(response.message);
                    //closePopup('findPwPopup'); // 실패 시 팝업 닫기
                }
            },
            error : function () {
                alert("오류가 발생했습니다.")
            }
        });
    }

    // 인증 코드 확인
    function verifyCode() {
        const verificationCode = $('#verificationCode').val().trim();
        const pwEmail = $('#pw_email').val().trim();

        if(!verificationCode) {
            alert("인증 코드를 입력해주세요.");
            return;
        }

        $.ajax({
            url : '/verifyCode',
            type : 'POST',
            data : {
                verificationCode : verificationCode,
                email : pwEmail
            },
            success: function (response) {
                if (response.success) {
                    alert("인증이 완료되었습니다.");
                    closePopup('findPwPopup');
                    $('#pwFindSuccessPopup').show();
                } else {
                    alert(response.message);
                }
            },
            error:function (){
                alert("오류가 발생했습니다.");
            }
        });
    }

    //비밀번호 변경
    function resetPassword() {
        const newPassword = $('#newPassword').val().trim();
        const confirmPassword = $('#confirmPassword').val().trim();

        if (!newPassword || !confirmPassword){
            alert("비밀번호 확인을 입력해주세요.");
            return false;
        }

        if (newPassword !== confirmPassword){
            alert("비밀먼호가 일치하지 않습니다.");
            return false;
        }

        $.ajax({
            url : '/resetPassword',
            type : 'POST',
            data : {newPassword},
            success : function (response) {
                if (response.success) {
                    alert(response.message);
                    closePopup('pwFindSuccessPopup');
                    //window.location.href = "/login.do"; // 비밀번호 변경 후 로그인 페이지로 이동 // 팝업 닫히면 로그인 페이지
                } else {
                    alert(response.message);
                }
            },
            error : function (){
                alert("비밀번호 변경 중 오류가 발생했습니다.");
            }
        });
        return false;
    }


</script>

<script>

    // ID 찾기 및 ID 검증
    $(document).ready(function (){
        const idFindResult = "${idFindResult}";
        if (idFindResult === "success") {
            $('#idFindSuccessPopup').show();
        } else if (idFindResult === "fail"){
            $('#idFindFailPopup').show();
        }
    })

    // 로그인 검증
    function validateLoginForm() {
        const username = $('#username').val();
        const password = $('#password').val();

        if (!username) {
            alert('ID를 입력해주세요.');
            return false;
        }

        if (!password) {
            alert('비밀번호를 입력해주세요.');
            return false;
        }

        return true; // 모든 조건이 충족되면 true 반환
    }

    // 서버에서 전달된 로그인 오류 메시지 처리
    $(document).ready(function () {
        const error = "${error}"; //서버에서 전달된 오류 메시지 값

        if (error && error !== "null") {
            alert(error);
        }
    });

    function openPopup(popupId) {
        $('#' + popupId).show();
        $('#overlay').show();
    }

    function closePopup(popupId) {
        $('#' + popupId).hide();
        $('#overlay').hide();
    }


    // ID 찾기 검증
    function validateFindIdForm() {
        const name = document.getElementById('id_name').value.trim();
        const email = document.getElementById('id_email').value.trim();

        if (!name){
            alert("이름을 입력해주세요.");
            return false;
        }

        if (!email){
            alert("이메일을 입력해주세요.");
            return false;
        }

        const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
        if(!emailPattern.test(email)) {
            alert("올바른 이메일 형식을 입력해주세요.");
            return false;
        }

        return true;

    }

</script>

</html>
