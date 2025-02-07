package concertreservation.token.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

    @InjectMocks
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenProvider, "secretKey", "dasdsadasdasdasdasdsadasdasdasdasdasdsadasdas");
    }

    @Test
    @DisplayName("토큰 생성 시 유효한 토큰이 생성되어야 한다")
    void createToken() {
        //given
        Long userId = 1L;
        Long concertId = 1L;
        //when
        String token = tokenProvider.createToken(userId,concertId);
        //then
        assertThat(token).isNotNull();
        assertThat(tokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("토큰을 생성하면 그 토큰에는 발급된 userId를 추출할수 있어야 한다.")
    void createTokenEqualUserId() {
        //given
        Long userId = 1L;
        Long concertId = 1L;
        //when
        String token = tokenProvider.createToken(userId,concertId);
        //then
        assertThat(token).isNotNull();
        assertThat(tokenProvider.getUserIdFromToken(token)).isEqualTo(userId);
    }

    @Test
    @DisplayName("유효하지 않은 토큰 검증 시 false를 반환해야 한다")
    void validateInvalidToken() {
        //given
        String invalidToken = "invalidToken";
        //when
        boolean result = tokenProvider.validateToken(invalidToken);
        //then
        assertThat(result).isFalse();
    }

}