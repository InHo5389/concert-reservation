package concertreservation.token.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "waiting_tokens")
public class WaitingToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;

    public static WaitingToken create(Long userId, String token, int expireMinute) {
        WaitingToken waitingToken = new WaitingToken();
        waitingToken.userId = userId;
        waitingToken.token = token;
        waitingToken.status = TokenStatus.WAITING;
        LocalDateTime now = LocalDateTime.now();
        waitingToken.issuedAt = now;
        waitingToken.expiredAt = now.plusMinutes(expireMinute);
        return waitingToken;
    }

    public void updateStatus(TokenStatus tokenStatus, int expireMinute) {
        this.status = tokenStatus;
        if (tokenStatus == TokenStatus.ACTIVE) {
            this.expiredAt = LocalDateTime.now().plusMinutes(expireMinute);
        }
    }

    public boolean isExpired() {
        return this.getStatus() == TokenStatus.EXPIRED;
    }

    public boolean isWaiting() {
        return this.getStatus() == TokenStatus.WAITING;
    }
}
