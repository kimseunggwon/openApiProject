package openApi.gwon.movieList.cmmn;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        // 세션에 사용자 정보가 없으면 로그인 페이지로 리다이렉트
        if (session == null || session.getAttribute("user") ==null) {
            response.sendRedirect(request.getContextPath() + "/login.do");
            return false;
        }

        return true; // 세션에 사용자 정보가 있으면 요청 진행
    }

}
