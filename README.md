# 🎫 콘서트 예약 서비스

## 📌 프로젝트 개요
### 소개
- 대규모 콘서트 예약 시스템에서 발생하는 트래픽 부하와 동시성 문제 해결에 중점을 둔 프로젝트입니다.
- Redis 기반 대기열 시스템 구축으로 초당 1,370건의 안정적인 트래픽 처리 달성
- 1.5억 건 이상의 데이터를 효율적으로 처리하는 인덱스 설계
- 2025.01.07 ~ 2025.02.12

### 핵심 기능
- 대기열 기반의 예약 시스템 설계 및 구현
- 콘서트 좌석 예약 및 결제
- 사용자 포인트 관리
- 콘서트 및 좌석 정보 관리
- 서버 이중화 및 분산 처리
- 동시성 제어 및 성능 최적화
- 데이터베이스 인덱스 최적화
- 시스템 모니터링 및 성능 테스트

## 🛠 기술 스택
- **Backend**: Java 17, Spring Boot 3.4.1, JPA
- **Database**: MySQL, Redis
- **Infrastructure**: AWS (EC2, RDS, ELB, CloudWatch)
- **Testing**: K6

## 📊 ERD
![Image](https://github.com/user-attachments/assets/52bb0dc1-adfc-4358-a9a1-8f0775d4d7ed)

## 🔍 시스템 아키텍처
![Image](https://github.com/user-attachments/assets/24a900ed-c9b3-4e96-9072-fc3a035f3ffa)

## 📈 성능 테스트 결과

### 테스트 환경
| 구분 | 인스턴스 타입 | CPU | 메모리 |
|------|--------------|------|--------|
| K6 서버 | t3a.small | 2 vCPU | 2 GiB |
| EC2 서버 | t3a.small | 2 vCPU | 2 GiB |

| 테이블 | 데이터 건수 |
|--------|------------|
| 유저 | 1,000,000 건 |
| 콘서트 | 2,000,000 건 |
| 콘서트 스케줄 | 3,000,000 건 |
| 좌석 | 150,000,000 건 |
| 예약 | 2,000,000 건 |

테스트 기간: 5분<br/>
부하 패턴: 0명에서 시작하여 5분 동안 점진적으로 5,000명까지 증가


### 대기열 없는 기본 구현
- TPS: 766
- 응답시간: 4초
- EC2 CPU: 99.9%
- RDS CPU: 19.9%
<details>
  <summary>성능 테스트 이미지, CloudWatch 이미지 보기</summary>
  <img src="https://github.com/user-attachments/assets/1ca8fad6-f28b-4f36-8ec1-bae0605d387c"/>
  <img src="https://github.com/user-attachments/assets/fb21f80c-a584-4402-9d6f-e409f45b57a1"/>
</details>

### MySQL기반 대기열 적용 후
- TPS: 288
- 응답시간: 20초
- EC2 CPU: 99.9%
- RDS CPU: 95.8%
<details>
  <summary>성능 테스트 이미지, CloudWatch 이미지 보기</summary>
  <img src="https://github.com/user-attachments/assets/2ec0d08b-0c59-4e05-8107-132b8cf83dad"/>
  <img src="https://github.com/user-attachments/assets/5a5a4e74-3e62-4670-8bbd-29c0d6f09d96"/>
</details>

### Redis기반 대기열 적용 후 (서버 이중화 포함)
- TPS: 1,370
- 응답시간: 2초
- EC2 CPU: 99.8%
- RDS CPU: 28.8%
<details>
  <summary>성능 테스트 이미지, CloudWatch 이미지 보기</summary>
  <img src="https://github.com/user-attachments/assets/7c8b0257-d876-4356-ae6b-c014ae52e7c5"/>
  <img src="https://github.com/user-attachments/assets/268251c0-8679-4854-95f7-38ca35ae5bab"/>
</details>

## 🎯 대기열 처리량 분석 및 설계

### 1. 대기열 미적용 시 시스템 성능 분석
| 지표 | 수치 |
|------|------|
| HTTP Request Rate | 1,110 req/s |
| Request Duration | 4초 |
| 최대 동시 접속자 | 5,000명 |
| EC2 CPU | 99.9% |
| RDS CPU | 32.7% |
| EC2 Memory | 52% |

### 2. 대기열 처리량 산정
#### 시스템 처리량 기준
- 초당 처리량(TPS): 1,110
- 분당 요청 처리량: 66,600 (1,110 * 60)

#### 사용자별 API 호출
```
1. 포인트 조회
2. 포인트 충전
3. 콘서트 목록 조회
4. 예약 가능 날짜 조회
5. 좌석 조회
6. 예약
7. 결제
```

#### 실제 동시 처리 가능 사용자 산출
- 동시 처리 가능 인원: 158명 (1,110 TPS ÷ 7 API)
- 분당 처리 가능 인원: 9,480명 (158명 * 60초)

### 3. 최종 대기열 배치 설계
- 안전 마진: 70% 적용
- 최종 배치 인원: 6,636명 (9,480 * 0.7)

## 🔒 락 전략 선택 이유
### 1. 좌석 예약 - 낙관적 락(Optimistic Lock) 선택

비즈니스 특성
- 좌석 예약은 초기에 경합이 집중되고 시간이 지날수록 감소
- 이미 예약된 좌석에 대한 중복 시도는 드문 상황
- 실패 시 빠른 피드백으로 다른 좌석 선택 유도 가능


장점
- DB 자원을 효율적으로 사용 (락 대기 없음)
- 버전 충돌 시 즉각적인 실패 응답 가능
- 트랜잭션 격리 수준이 낮아도 데이터 정합성 보장

### 2. 포인트 충전 - 비관적 락(Pessimistic Lock) 선택

비즈니스 특성
- 포인트는 금전적 가치가 있어 데이터 정합성이 매우 중요
- 실패 후 재시도가 자연스러운 흐름
- 동시 요청이 성공할 것이라는 기대가 높음

장점
- 강력한 데이터 정합성 보장
- 충돌이 자주 발생하는 상황에서 효율적
- 실패 없이 순차적 처리 가능

## 💡 Technical Decision Making

### 1. MySQL vs Redis 대기열
#### MySQL 대기열의 한계
- RDS CPU 사용률 95.8%까지 증가
- 폴링으로 인한 DB 부하 증가
- 응답 시간 증가 (20초)

#### Redis 대기열 선택 이유
- 인메모리 DB 특성을 활용한 빠른 처리
- Sorted Set을 통한 효율적인 대기열 관리
- 실제 서비스(예매)와 대기열 로직의 DB 부하 분리
- **RDS CPU 사용률 95.8% → 32.7% 개선**

### 2. 서버 이중화로 스케줄러 중복 실행 방지 전략
#### ShedLock
- ShedLock을 활용한 스케줄러 동기화
    - 분산 환경에서 스케줄러 중복 실행 방지
    - 스케줄러가 1분이기에 TTL 50초 설정으로 장애 상황 자동 복구

### 3. 성능 최적화
#### 인덱스 최적화
- 예약 가능 날짜 조회 성능 개선
    - concert_schedule 테이블에 concert_id 단일 인덱스 적용
    - 조회 속도 3.89초 → 29ms로 개선
- 예약 가능 좌석 조회 성능 개선
    - seat 테이블에 (concert_schedule_id, seat_status) 복합 인덱스 적용
    - 조회 속도 4분 54초 → 747ms로 개선

#### 캐시 전략
- 콘서트 목록: Redis 캐시
- 콘서트 예약 가능 날짜 정보: Redis 캐시
- 콘서트 예약 가능 좌석 정보: Redis 캐시


## 🎯 향후 개선 사항
1. ElastiCache 도입 검토
