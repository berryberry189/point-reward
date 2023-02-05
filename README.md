# point-reward
매일 00시 00분 00초에 선착순 10명에게 포인트를 지급/관리하는 API구현

# 스펙
- Java 11
- Spring Boot 2.7.8
- Gradle
- Spring Data JPA
- H2 Database
- Lombok
- Swagger

# 실행방법
## install
```
$ git clone https://github.com/berryberry189/point-reward.git
```
## 레디스 실행
```
$ docker-compose -f "docker-compose.yml" up -d --build
```
## Build
```
$ ./gradlew build
```
## Run
```
$ java -jar ./build/libs/point-reward-0.0.1-SNAPSHOT.jar
```

# API 상세
http://localhost:8080/swagger-ui/
- **POST /point-reward** 선착순 포인트 발급 API
  - 매일 00시 00분 00초에 선착순 10명에게 보상 지급 
  - 선착순 10명에게 100 포인트의 보상이 지급 되며 10명 이후에는 지급되지 않음
  - 추가보상은 10일 까지 이어지며 그 이후 연속 보상 횟수는 1회 부터 다시 시작
  - 한명의 사용자는 같은 날 1회만 받을 수 있음
  
- **GET /point-reward** 선착순 포인트 목록 API
  - 특정 일자 별 보상 받은 사용자 내역 목록
  - 검색조건 : 날짜 (필수)
  - 정렬 조건 : 시간 오름 내림 차순
  
- **GET /point-reward/{point_reward_id}** 선착순 포인트 상세
  - 보상 데이터의 상세내역 조회
  - 연속된 보상일 이 있는 경우 직전 보상데이터id 함께 반환
  



