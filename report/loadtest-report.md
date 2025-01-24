## 부하 테스트

## 개별 API 부하 테스트
- 5분동안 점진적으로 5000개의 요청
- 예약, 결제 병목현상 발생
  
| 사양 | 값 |
|------|----|
| **vCPU** | 2 개 |
| **메모리 (RAM)** | 2 GiB |


## 1. 포인트 조회
- 평균 1900tps
<details>
  <summary>k6부하테스트 결과</summary>
  <img src="https://github.com/user-attachments/assets/ac1885ce-3f82-4305-92ab-4232d5c65dae">
</details>
<details>
  <summary>CloudWatch 모니터링 결과</summary>
  <img src="https://github.com/user-attachments/assets/79c9f3ea-b27e-4dbe-bd15-92f919de69f3">
</details>

## 2. 포인트 충전
- 평균 3800tps
<details>
  <summary>k6부하테스트 결과</summary>
  <img src="https://github.com/user-attachments/assets/f4ad0b1b-dbae-4893-8218-6e7dfacd5837">
</details>
<details>
  <summary>CloudWatch 모니터링 결과</summary>
  <img src="https://github.com/user-attachments/assets/dd5737c6-56f0-463e-8510-76be259d4c62">
</details>


## 3. 콘서트 예약
- 평균 1800tps
<details>
  <summary>k6부하테스트 결과</summary>
  <img src="https://github.com/user-attachments/assets/7b2b85f8-90c3-402c-9296-30ec67b91011">
</details>
<details>
  <summary>CloudWatch 모니터링 결과</summary>
  <img src="https://github.com/user-attachments/assets/362e0625-875d-4726-a1fa-bf7ddf4e6f22">
</details>


## 4. 예약 가능 날짜 조회
- 평균 2700tps
<details>
  <summary>k6부하테스트 결과</summary>
  <img src="https://github.com/user-attachments/assets/fe25390b-d868-4320-a053-e1dfaecd1000">
</details>
<details>
  <summary>CloudWatch 모니터링 결과</summary>
  <img src="https://github.com/user-attachments/assets/d3350e93-3f04-4af8-af4b-46d1bb4f6f78">
</details>


## 5. 예약 가능 좌석 조회
- 평균 2700tps
<details>
  <summary>k6부하테스트 결과</summary>
  <img src="https://github.com/user-attachments/assets/3d5394da-d1af-4327-8146-18c54ca75031">
</details>
<details>
  <summary>CloudWatch 모니터링 결과</summary>
  <img src="https://github.com/user-attachments/assets/5dea58de-c012-4a3b-8d5c-dbe97bd03e66">
</details>


## 6. 결제
- 평균 900tps
<details>
  <summary>k6부하테스트 결과</summary>
  <img src="https://github.com/user-attachments/assets/dff9a3f7-f13c-4536-9fbd-c932646f036e">
</details>
<details>
  <summary>CloudWatch 모니터링 결과</summary>
  <img src="https://github.com/user-attachments/assets/a44d06d3-54cd-4cdd-9b8e-aa98781a23ae">
</details>


## 7. 콘서트 조회 무한 스크롤
- 평균 3000tps
<details>
  <summary>k6부하테스트 결과</summary>
  <img src="https://github.com/user-attachments/assets/c10914db-fecc-45aa-ab68-fc23a3f2f085">
</details>
<details>
  <summary>CloudWatch 모니터링 결과</summary>
  <img src="https://github.com/user-attachments/assets/aba87b23-2edf-424d-b233-b7ec69306e73">
</details>
