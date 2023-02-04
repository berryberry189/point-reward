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
## Build
```
$ ./gradlew build
```
## 레디스 실행
```
$ docker-compose -f "docker-compose.yml" up -d --build
```
## Run
```
$ java -jar ./build/libs/point-reward-0.0.1-SNAPSHOT.jar
```

# API 상세
http://localhost:8080/swagger-ui.html



