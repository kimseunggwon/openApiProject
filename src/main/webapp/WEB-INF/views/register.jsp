<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

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
            padding: 40px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            max-width: 400px;
            margin: auto;
        }
        input[type=text], input[type=password], input[type=email], input[type=date], input[type=tel] {
            padding: 10px;
            margin-bottom: 20px; /* 간격 늘리기 */
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
        .small-button {
            width: auto;
            padding: 5px 10px;
            background-color: #FFA500; /* 중복 확인 버튼 색 변경 */
            color: white;
            border: none;
            cursor: pointer;
        }
        .small-button:hover {
            background-color: #FF8C00;
        }
        .warning-text {
            color: darkblue; /* 사용 가능한 아이디 문구 색 변경 */
        }
        .back-button {
            width: auto;
            padding: 5px 20px;
            background-color: #808080; /* 뒤로가기 버튼 색 변경 */
            color: white;
            border: none;
            cursor: pointer;
        }
        .back-button:hover {
            background-color: #696969;
        }
        .password-info {
            font-size: 12px;
            color: #FF6347; /* 빨간색 안내 문구 */
            margin-top: -10px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="register-container">
    <h1>회원가입</h1>
    <form action="${pageContext.request.contextPath}/register.do" method="post" onsubmit="return validateForm()">
        <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>--%>
        <div>
            <label for="username">ID:</label>
            <button type="button" class="small-button" onclick="checkUsername()">중복 확인</button>
            <input type="text" id="username" name="username" required/>
            <span id ="usernameCheckResult" class="warning-text"></span>
        </div>
        <div>
            <%-- todo : Spring Security PasswordEncoder 사용해서 pw를 암호화 해서 저장하는게 좋다 --%>
            <%-- todo : 회원가입 폼에서 CSRF 토큰을 사용하여 CSRF(Cross-Site Request Forgery) 공격을 방지. --%>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required/>
            <p class="password-info">* 비밀번호 숫자 및 문자 포함, 6자리 이상이어야 합니다.</p>
        </div>
        <div>
            <label for="confirmPassword">Confirm Password:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required/>
            <p class="password-info">* 비밀번호를 다시 입력해주세요.</p>
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
            <br><br>
            <button type="button" class="back-button" onclick="window.location.href='${pageContext.request.contextPath}/login.do'">뒤로가기</button>
        </div>
    </form>
</div>
</body>


<script>
    let isUsernameAvailable = false;

    function checkUsername() {
        const username = $('#username').val();
        $.ajax({
            url: '${pageContext.request.contextPath}/checkUsername.do',
            type: 'GET',
            data: { username: username },
            success: function(response) {
                if (response.available) {
                    $('#usernameCheckResult').text('사용 가능한 아이디입니다.');
                    isUsernameAvailable = true; // 사용 가능 상태 설정
                } else {
                    $('#usernameCheckResult').text('이미 사용 중인 아이디입니다.');
                    isUsernameAvailable = false; // 사용 불가 상태 설정
                }
            },
            error: function() {
                $('#usernameCheckResult').text('아이디 확인 중 오류가 발생했습니다.');
                isUsernameAvailable = false; // 오류 시 사용 불가 상태로 간주
            }
        });
    }

    function validateForm() {
        // 가입하기 버튼 클릭 시 최종 확인
        const username = $('#username').val();
        const password = $('#password').val();
        const confirmPassword = $('#confirmPassword').val();
        const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,}$/;

        if (!isUsernameAvailable) {
            alert('아이디 중복 확인을 해주세요.');
            return false; // 폼 제출 막기
        } else if (!passwordPattern.test(password)) {
            alert('비밀번호는 최소 6자리이며, 숫자와 문자가 포함되어야 합니다.');
            return false; // 폼 제출 막기
        } else if (password !== confirmPassword) {
            alert('비밀번호가 일치하지 않습니다. 다시 입력해주세요.');
            return false; // 폼 제출 막기
        } else {
            alert('회원가입이 완료되었습니다.');
        }

        // 이메일 형식 및 모든 필드가 브라우저 기본 유효성 검사를 통과하는지 확인
        return true; // 폼이 제출되도록 허용
    }

</script>



</html>
