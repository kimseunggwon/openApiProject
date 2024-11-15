<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            padding: 100px;
        }
        .register-container {
            background-color: #fff;
            padding: 90px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            max-width: 300px;
            margin: auto;
        }
        input[type=text], input[type=password], input[type=email], input[type=date], input[type=tel] {
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
<div class="register-container">
    <h1>회원가입</h1>
    <form action="${pageContext.request.contextPath}/register.do" method="post">
        <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>--%>
        <div>
            <label for="username">ID:</label>
            <input type="text" id="username" name="username" required/>
        </div>
        <div>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required/>
        </div>
        <div>
            <label for="name">Name:</label>
            <input type="text" id="name" name="name" required/>
        </div>
        <div>
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required/>
        </div>
        <div>
            <label for="birthdate">Birthdate:</label>
            <input type="date" id="birthdate" name="birthdate" required/>
        </div>
        <div>
            <label for="phone">Phone:</label>
            <input type="tel" id="phone" name="phone" required/>
        </div>
        <div>
            <button type="submit">가입하기</button>
            <br>
            <br>
            <br>
            <button type="button" onclick="window.location.href='${pageContext.request.contextPath}/login.do'">뒤로가기</button>
        </div>
    </form>
</div>
</body>
</html>
