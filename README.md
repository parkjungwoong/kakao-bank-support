# 중소기업은행 지자체 협약 지원 API 개발

## 목차
1. [개발 프레임워크](#1-개발-프레임워크)
2. [문제해결 방법](#2-문제해결-방법)
3. [API 명세](#3-API-전문)
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
        - 계산 가능한 데이터로 변환
            - 지원 금액, 이차보전 비율과 같은 데이터는 DB 저장시 계산 가능하도록 데이터 타입을 변경하여 저장
            - 금액 변환 : *억원, *백만원, 등 한글 숫자 표현식을 **WonUnit** enum 클래스로 관리하여 숫자로 변환에 사용
            - todo: 이차보전 데이터 변환 관련 내용 쓰기
        - 검색 용의성
            - **용도** 데이터를 별도 **USAGE_CODE** 테이블로 관리
            - 지자체 협약 지원 정보에 용도는 2개 이상 갖을 수 있으므로 **USAGE_OPT** 테이블에 지원 정보ID, 용도 ID Double PK로 저장
            - 한글로 용도 검색시 : **USAGE_CODE** 테이블에서 용도ID 조회 후 **USAGE_OPT** 에서 용도ID로 지차체 협약 정보 리스트 조회 가능             
            
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

    - 업무별 Service,Controller,Dao 분리
        - 회원 처리 업무
        - 지자체 협약정보 처리 엄무
        
- 선택 문제 todo: 관련해서 작성하기
    - 형태소 분석기 연동
    - 지역 정보 조회 연동
    - 추출된 값에 따른 점수 환산 기능 개발
    - 최종 점수 합산에 따른 지차제 정보 반환

- 단위 Test 전략
    - todo: 작성하기
                   
## 3. API 전문
- HTTP method는 GET, POST만 사용
- 사용자 -> 서버

## 4. 빌드 및 실행 방법


## todo
1. 스키마 결정하기 
2. app 기동시 svc파일을 h2에 저장
3. Spring security 설정
4. CRUD 만들기
5. 형태소 분석
6. 추천 기능

