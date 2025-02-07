//package concertreservation.token.service;
//
//import concertreservation.common.exception.CustomGlobalException;
//import concertreservation.common.exception.ErrorType;
//import concertreservation.token.entity.TokenStatus;
//import concertreservation.token.entity.WaitingToken;
//import concertreservation.token.service.response.TokenIssueResponse;
//import concertreservation.token.service.response.TokenStatusResponse;
//import concertreservation.token.util.TokenUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class WaitingTokenService {
//
//    private final TokenProvider tokenProvider;
//    private final WaitingTokenRepository waitingTokenRepository;
//
//    @Transactional
//    public TokenIssueResponse issueToken(Long userId) {
//
//        // 이렇게 해주지 않으면 대기 순서를 waiting 개수로 계산하는데 새로 발급받는 것이 계속 waiting 상태로 계속 쌓여서
//        // 기존것을 expired 시켜줘야함
//        waitingTokenRepository.findByUserIdAndStatusIn(
//                userId,
//                List.of(TokenStatus.ACTIVE, TokenStatus.WAITING)
//        ).ifPresent(token -> {
//            token.updateStatus(TokenStatus.EXPIRED, 0);
//            waitingTokenRepository.save(token);
//        });
//
//        String jwtToken = tokenProvider.createToken(userId);
//        WaitingToken waitingToken = waitingTokenRepository.save(WaitingToken.create(userId, jwtToken, TokenUtil.EXPIRE_MINUTE));
//
//        return TokenIssueResponse.from(jwtToken, waitingToken.getStatus());
//    }
//
//    @Transactional(readOnly = true)
//    public TokenStatusResponse getTokenStatus(String token) {
//
//        WaitingToken waitingToken = waitingTokenRepository.findByToken(token)
//                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_TOKEN));
//
//        if (waitingToken.isExpired()) {
//            return TokenStatusResponse.from(waitingToken, null);
//        }
//
//        if (waitingToken.isWaiting()) {
//            long waitingStatusSize = waitingTokenRepository.countByStatusAndIdLessThan(
//                    TokenStatus.WAITING,
//                    waitingToken.getId()
//            );
//            Long waitingNumber = waitingStatusSize + 1;
//            return TokenStatusResponse.from(waitingToken, waitingNumber);
//        }
//
//        return TokenStatusResponse.from(waitingToken, 0L);
//    }
//
//    @Transactional(readOnly = true)
//    public WaitingToken getToken(String token){
//        return waitingTokenRepository.findByToken(token)
//                .orElseThrow(() -> new CustomGlobalException(ErrorType.NOT_FOUND_TOKEN));
//    }
//
//    @Transactional
//    public void updateExpiredTokenStatus() {
//
//        List<WaitingToken> expiredTokens = waitingTokenRepository.findByStatusInAndExpiredAtBefore(
//                Arrays.asList(TokenStatus.ACTIVE),
//                LocalDateTime.now()
//        );
//
//        for (WaitingToken token : expiredTokens) {
//            token.updateStatus(TokenStatus.EXPIRED, 0);
//        }
//    }
//
//    @Transactional
//    public void updateActivateFromWaiting() {
//        int currentActiveCount = waitingTokenRepository.countByStatus(TokenStatus.ACTIVE);
//        int availableSlots = TokenUtil.MAX_ACTIVE_TOKENS - currentActiveCount;
//
//        if (availableSlots <= 0) {
//            return;
//        }
//
//        List<WaitingToken> waitingTokens = waitingTokenRepository.findByStatusOrderByIdAsc(TokenStatus.WAITING)
//                .stream()
//                .limit(availableSlots)
//                .toList();
//
//        for (WaitingToken token : waitingTokens) {
//            token.updateStatus(TokenStatus.ACTIVE, TokenUtil.EXPIRE_MINUTE);
//            waitingTokenRepository.save(token);
//        }
//    }
//
//    @Transactional
//    public void removeExpiredTokens() {
//        waitingTokenRepository.deleteByStatus(TokenStatus.EXPIRED);
//    }
//}
