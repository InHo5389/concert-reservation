package concertreservation.token.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import concertreservation.common.exception.CustomGlobalException;
import concertreservation.common.exception.ErrorResponse;
import concertreservation.common.exception.ErrorType;
import concertreservation.token.service.TokenProvider;
import concertreservation.token.service.WaitingTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class WaitingTokenInterceptor implements HandlerInterceptor {

    private final TokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        WaitingTokenRequired annotation = handlerMethod.getMethodAnnotation(WaitingTokenRequired.class);

        if (annotation == null) {
            return true;
        }

        try {
            String token = extractToken(request);
            validateQueueToken(token);
            return true;
        } catch (CustomGlobalException e) {
            handleException(response, e);
            return false;
        }
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("WaitingToken");
        if (token == null || token.isEmpty()) {
            throw new CustomGlobalException(ErrorType.NOT_FOUND_TOKEN);
        }
        return token;
    }

    private void validateQueueToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new CustomGlobalException(ErrorType.INVALID_TOKEN);
        }
    }

    private void handleException(HttpServletResponse response, CustomGlobalException e) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorType());
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
