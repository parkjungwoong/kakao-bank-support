# 중소기업은행 지자체 협약 지원 API 개발

## 목차
1. [개발 프레임워크](#1-개발-프레임워크)
2. [문제해결 방법](#2-문제해결-방법)
3. [API 전문](#3-API-전문)
4. [빌드 및 실행 방법](#4-빌드-및-실행-방법)

## 1. 개발 프레임워크
- JDK 1.8
- Spring Boot 2.1.7
    - SpringSecurity
- H2
- Mybatis

## 2. 문제해결 방법
- 데이터 핸들링
    - App 기동시 properties파일 내 initDataFilePath 경로의 CSV 파일을 읽어 DB에 저장
        - CSV 데이터 -> DTO 변환
            - CSV 데이터 검사 및 변환을 위한 추상 클래스 **CsvMatcher**
            - **CsvMatcher** 추상 클래스를 상속하여 CSV 데이터 검사 로직과 반환 타입만 지정하면 DTO로 변환되도록 함
        - 조회 가능한 데이터로 변환
            - 지원 금액, 이차보전 비율과 같은 데이터는 DB 저장시 조회 가능하도록 데이터 타입을 변경하여 저장
            - 금액 변환 : *억원, *백만원, 등 한글 숫자 표현식을 **WonUnit** enum 클래스로 관리하여 숫자로 변환에 사용
            - 이차보전 : 최소, 최대값 파싱, 평균값 계산 후 DB에 저장
        - 검색 용의성
            - **용도** 데이터를 별도 **USAGE_CODE** 테이블로 관리
            - 지자체 협약 지원 정보는 용도를 2개 이상 갖을 수 있으므로 **USAGE_OPT** 테이블에 지원 정보ID, 용도 ID Double PK로 저장             
            
- Rest API 개발 : 관심사에 따른 코드 분리를 함
    - Exception 처리
        - **ComException**, **ErrorCode** 클래스로 에러에 대한 응답형태 분리
        - **ExceptionController** 클래스로 에러 응답 처리 분리
        
    - Security 처리
        - **SpringSecurity** 설정으로 보안 관련 처리 분리, url 단위 권한 검사 처리
        - **xxFilter** 클래스로 요청 형태 검사 로직 분리
        - **xxProvider** 클래스로 인가 로직 분리
        - **SuccessHandler** 클래스로 보안 검사 성공 처리 분리
        - **FailHandler** 클래스로 보안 검사 실패 처리 분리

    - 조회 API
        - 조회 조건이 정렬, 칼럼명, 특정 개수 처럼 세분화 되어있어 하나의 URL에 검색 옵션을 파라미터로 받아 처리하도록함
        ```http request
        POST
        Content-Type: application/json
        
        {"match":{"usage":"시설"}, #검색 옵션
        "limit":2, #로우 수
        "sort":[{"supportLimit":"desc"},{"rate":""}], #정렬 옵션
        "fields":["regionId","usage","institute"]} #반환할 필드
        ``` 
    
    - 업무별 Service,Controller,Dao 분리
        - 회원 처리 업무
        - 지자체 협약정보 처리 엄무
                           
## 3. API 전문
- HTTP method는 GET, POST만 사용
- [구글독스링크](https://docs.google.com/spreadsheets/d/1ZnxKO5wJ3yPNPG4FwsU91dSTbr1_SiIGvRb_1SlECeM/edit?usp=sharing)

## 4. 빌드 및 실행 방법
```
#maven 테스트시 파일 경로 관련 오류 발생시 application.properties 파일의 com.initDataFilePath 값에 파일 경로 지정
maven package

java -jar target/bank-support-0.0.1.jar 테스트csv파일 경로
```
