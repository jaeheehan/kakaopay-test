# API 개발

### 개발 환경

- Java 11
- Maven
- SpringBoot
- JPA
- lombok
- h2
- Spring Security
- JWT
- ARIMA algorithm

## 설치방법
> github 또는 압축파일로 받은 후 IDE 에서 Maven 프로젝트로 생성

## 실행방법 
> mvn 빌드 실행 또는 
> 첨부된 [internet.jar](https://github.com/jaeheehan/) 를 다운받아 아래 명령어로 실행  
> http://localhost:8080/swagger-ui.html 접속 테스트 가능 
```
java -jar internet.jar
```

## 문제 해결 및 전략

### 데이터 등록

1. 데이터 파일을 서버가 시작될 때 스트림으로 읽어서 h2 디비에 저장함
2. 데이터 첫줄에서 디바이스를 읽어서 Device 테이블에 등록하고 년도별 기기 데이터는 Internet 테이블에 등록
3. Internet 테이블의 경우 년도(year)와 디바이스 아이디(device_id) 두 항목을 Key로 설정함 

### 테이블 설계 

> 회원 테이블 (MEMBER)

No | Column | Type | PK | Description
--------- | --------- | --------- | --------- | ---------
1 | USERNAME | VARCHAR | O | 유저 아이디  
2 | PASSWORD | VARCHAR | X | 패스워드

> 기기 테이블 (DEVICE)

No | Column | Type | PK | Description
--------- | --------- | --------- | --------- | ---------
1 | DEVICE_ID | VARCHAR | O | 기기 아이디  
2 | DEVICE_NAME | VARCHAR | X | 기기 이름 

> 통계테이블  테이블 (INTERNET)

No | Column | Type | PK | Description
--------- | --------- | --------- | --------- | ---------
1 | YEAR | INTEGER | O | 년도
2 | DEVICE_ID | VARCHAR | O | 기기 아이디 
2 | RATE | DOUBLE | X | 이용률 

### API 전략 

#### 접속 기기 목록 API    
> Device 테이블을 조회해서 보여줌 

#### 각 년도별 인터넷 뱅킹을 가장 많이 이용하는 접속 기기 API
> Internet 테이블 년도별로 사용량이 많은 기기를 랭킹을 정하고 1순위 해당하는 기기만 리스트로 가져옴  
> 쿼리 메서드가 아닌 Query 어노테이션을 통해서 실행함
```
@Query(value = "select year, i.device_id, rate \n" +
            " from internet i inner join device d on d.device_id = i.device_id\n" +
            "where  0 = (select count(*) from internet where year = i.year and rate > i.rate) " , nativeQuery = true)
``` 

#### 특정 년도 인터넷 뱅킹이 가장 많이 접속하는 기기 API
> 쿼리 메서드를 이용하여 보여줌
```
findTop1ByInternetPKYearOrderByRateDesc
```

#### 특정 기기 에서 인터넷 뱅킹을 가장 많이 접속한 년도 API
> 쿼리 메서드를 이용하여 보여줌
```
findTop1ByInternetPKDeviceOrderByRateDesc
```

#### 특정 기기 2019 년도 예측 데이터 API
> 이전 데이터값과 추세를 바탕으로 데이터를 예측하는 시계열 예측 방법인 ARIMA를 이용함 (Open Source)  
> 분기/반기/연간 단위로 다음 지표를 예측한다거나 주간/월간 단위로 지표를 예측할 수 있는 변수 제공  
> AR은 자기 상관관계, MA는 평균이동 이며 ARIMA는 두개를 결합한 예측 방법   
> ARIMA 모형은 ARIMA(1,2,1)를 사용하여 2019년도 데이터를 예측  

#### 성능을 고려하여 10000 TPS 요청 아키텍처
> 캐쉬를 통해 메모리를 이용하여 요청 처리와 비동기로 동작하는 경우 @Async 처리를 통해서 성능 향상 
```
@Cacheable(value = "deviceEachYear")
@Override
public InternetUseRowList getTopDeviceEachYear() {
```
> 로드밸런서 이용하여 부하를 분산하여 처리 (Amazon ALB, AutoScaling)

#### API 인증 처리
> JWT 통해서 token 발급  
> 패스워드는 bcryto 암호화를 이용하여 데이터 베이스에 저장함  
> 리프래쉬 토큰으로 새로운 토큰 발급   

## API 목록

No | Path | Method | Description
--------- | --------- | --------- | ---------
1 | /auth/signUp | POST | 가입하기 
2 | /auth/signIn | POST | 로그인
3 | /auth/refresh | POST | 토큰갱신 
4 | /api/devices | GET | 전체디바이스목록
5 | /api/topDeviceEachYear | GET | 년도별 최대 접속기기
6 | /api/internetUseTopByYear | GET | 특정 년도 최대이용 접속기기
7 | /api/internetUseYearTopByDevice | GET | 특정 기기 최대이용 년도
8 | /api/forecastUseByYear | GET | 특정 기기 다음 년도 예측

### 1.가입하기   
> 가입하는 유저이름과 패스워드를 입력 후 전송 토큰을 받아옴
 
키 | 타입 | 설명
--------- | --------- | ---------
username | String | 유저아이디
password | String | 패스워드 

예제  
```
curl -v -X POST "http://localhost:8080/auth/signUp" \
-H "Content-Type: application/json" \
-d '{"username" : "test" , "password" : "1234"}'
```

결과 
```
{
    "access_token":"eyJhbGciOiJIUzUxMiJ9.eyJ......",
    "refresh_token":"eyJhbGciOiJIUzUxMiJ9.eyJ....."
}
```
키 | 타입 | 설명
--------- | --------- | ---------
access_token | String | 접근 토큰
refresh_token | String | 갱신 토큰

### 2. 로그인   
> 가입한 유저아이디와 패스워드로 로그인 하고 토큰을 받아옴

키 | 타입 | 설명
--------- | --------- | ---------
username | String | 유저아이디
password | String | 패스워드 

예제  
```
curl -v -X POST "http://localhost:8080/auth/signIn" \
-H "Content-Type: application/json" \
-d '{"username" : "test" , "password" : "1234"}'
```
결과 
```
{
    "access_token":"eyJhbGciOiJIUzUxMiJ9.eyJ......",
    "refresh_token":"eyJhbGciOiJIUzUxMiJ9.eyJ....."
}
```
키 | 타입 | 설명
--------- | --------- | ---------
access_token | String | 접근 토큰
refresh_token | String | 갱신 토큰

### 3. 토큰갱신
> 리프래쉬 토큰으로 새로운 토큰을 받아오는 API 
> Authorization 헤더에 "Bearer Token" 입력 

예제  
```
curl -v -X POST "http://localhost:8080/auth/refresh" \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0Iiw...." 
```
결과 
```
{
 "access_token":"eyJhbGciOiJIUzUxMiJ9.eyJ......",
 "refresh_token":"eyJhbGciOiJIUzUxMiJ9.eyJ....."
}
```
키 | 타입 | 설명
--------- | --------- | ---------
access_token | String | 접근 토큰
refresh_token | String | 갱신 토큰

### 4. 전체디바이스목록
> 전체 디바이스 목록을 나타낸다. 

예제
```
curl -v -X GET "http://localhost:8080/api/devices" \
-H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIi......."
```
결과 
```
{
    "devices":[
        {
            "device_id":"DIS001",
            "device_name":"스마트폰"
        },
        {
            "device_id":"DIS002",
            "device_name":"데스크탑 컴퓨터"
        }
        ...
        {
            "device_id":"DIS005",
            "device_name":"스마트패드"
        }
    ]
}
```

### 5. 년도 별 최대 이용 기기 리스트 
> 각 년도별로 인터넷 뱅킹을 가장 많이 이용하는 접속기기를 리스트로 출력한다. 

예제
```
curl -v -X GET "http://localhost:8080/api/topDeviceEachYear" \
-H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIi......."
```
결과 
```
{
    "device":[
        {
            "year":2011,
            "device_id":"DIS002",
            "device_name":"데스크탑 컴퓨터",
            "rate":95.1
        },
        {
            "year":2012,
            "device_id":"DIS002",
            "device_name":"데스크탑 컴퓨터",
            "rate":93.9
        },
        ...
        {
            "year":2018,
            "device_id":"DIS001",
            "device_name":"스마트폰",
            "rate":90.5
        }
    ]
}
```

### 6. 특정 년도 최대 접속 기기 
> 특정 년도를 입력받고 최대 접속 기기의 정보를 출력한다.

키 | 타입 | 설명
--------- | --------- | ---------
year | int | 년도

예제
```
curl -v -X GET "http://localhost:8080/api/internetUseTopByYear?year=2011" \
-H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWI...."
```
결과 
```
{
    "result":{
        "year":2011,
        "device_name":"데스크탑 컴퓨터"
        ,"rate":95.1
    }
}
```

### 7. 특정 기기 최대 접속 년도 
> 특정 기기아이디를 입력받고 최대 접속 년도의 정보를 출력한다.

키 | 타입 | 설명
--------- | --------- | ---------
device_id | String | 디바이스 아이디 

예제
```
curl -v -X GET "http://localhost:8080/api/internetUseYearTopByDevice?device_id=DIS001" \
-H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWI....."
```
결과 
```
{
    "result":{
        "year":2017,
        "device_name":"스마트폰",
        "rate":90.6
    }
}
```

### 8. 특정 년도 예측 
> 특정 기기아이디를 입력받고 예측 년도의 정보를 출력한다.

키 | 타입 | 설명
--------- | --------- | ---------
device_id | String | 디바이스 아이디 

예제
```
curl -v -X GET "http://localhost:8080/api/forecastUseByYear?device_id=DIS001" \
-H "Authorization: Bearer eyJhbGciOiJ........."
```
결과 
```
{
    "year":2019,
    "device_name":"스마트폰",
    "rate":90.47168971353064
}
```
