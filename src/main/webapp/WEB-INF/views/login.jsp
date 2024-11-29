<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
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
    </style>
</head>
<body>
<div class="login-container">
    <h1>로그인</h1>
    <form action="${pageContext.request.contextPath}/login" method="post" onsubmit="return validateLoginForm()">
        <div>
            <label for="username">아이디 ID</label>
            <input type="text" id="username" name="username"/>
        </div>
        <div>
            <label for="password">비밀번호 PW</label>
            <input type="password" id="password" name="password"/>
        </div>
        <div>
            <button type="submit">로그인</button>
        </div>
    </form>
    <p>아직 계정이 없으신가요? <a href="${pageContext.request.contextPath}/register.do">회원가입</a></p>

</div>
</body>

<script>
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


</script>

</html>
