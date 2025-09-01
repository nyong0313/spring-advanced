package org.example.expert.domain.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
public class AdminAccessLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURI();
        String method = request.getMethod();
        String userId = String.valueOf(request.getAttribute("userId"));
        String userRole = String.valueOf(request.getAttribute("userRole"));
        LocalDateTime startTime = (LocalDateTime) request.getAttribute("startTime");

        log.info("[Admin 접근 로그] userId={}, role={}, method={}, url={}, time={}",
                userId, userRole, method, url, startTime);

        return true;
    }
}
