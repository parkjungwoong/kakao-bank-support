-- 지원 정보 원장
DROP TABLE IF EXISTS LOCAL_BANK_SUPPORT;
CREATE TABLE LOCAL_BANK_SUPPORT (LBS_ID VARCHAR PRIMARY KEY, REGION_ID VARCHAR, TARGET VARCHAR, USAGE VARCHAR, SUPPORT_LIMIT VARCHAR, RATE VARCHAR, INSTITUTE VARCHAR, MGMT VARCHAR, RECEPTION VARCHAR);

-- 지원 정보 상세
DROP TABLE IF EXISTS LOCAL_BANK_SUPPORT_DTL;
CREATE TABLE LOCAL_BANK_SUPPORT_DTL (LBS_DTL_ID VARCHAR PRIMARY KEY, LBS_ID VARCHAR, SUPPORT_LIMIT NUMBER(12), RATE_MIN NUMBER(5,2), RATE_AVG NUMBER(5,2), RATE_MAX NUMBER(5,2));

-- 용도 코드 테이블
DROP TABLE IF EXISTS USAGE_CODE;
CREATE TABLE USAGE_CODE (USAGE_CODE_ID VARCHAR PRIMARY KEY, VAL VARCHAR, USE_YN VARCHAR);

-- 용도 옵션 테이블
DROP TABLE IF EXISTS USAGE_OPT;
CREATE TABLE USAGE_OPT (USAGE_CODE_ID VARCHAR, LBS_ID VARCHAR, USE_YN VARCHAR, PRIMARY KEY(USAGE_CODE_ID,LBS_ID));

-- 지자체 정보
DROP TABLE IF EXISTS REGION_MAS;
CREATE TABLE REGION_MAS (REGION_ID VARCHAR PRIMARY KEY, NAME VARCHAR, MAP_ID VARCHAR);

-- 지역 정보
DROP TABLE IF EXISTS REGION_MAP;
CREATE TABLE REGION_MAP (MAP_ID VARCHAR PRIMARY KEY, NAME VARCHAR);

-- 사용자 정보
DROP TABLE IF EXISTS USER_INFO;
CREATE TABLE USER_INFO (USER_NO VARCHAR PRIMARY KEY, USER_ID VARCHAR, USER_PW VARCHAR, INST_ID VARCHAR, INST_DTM DATETIME, UPDT_ID VARCHAR, UPDT_DTM DATETIME, USE_YN VARCHAR);
INSERT INTO USER_INFO (USER_NO, USER_ID, USER_PW , INST_ID, INST_DTM, USE_YN) VALUES ('1234', 'myuoong' ,'1234', 'myuoong', SYSDATE, 'Y');

-- 인증 내역
DROP TABLE IF EXISTS AUTH_TRD;
CREATE TABLE AUTH_TRD (AUTH_TRD_NO VARCHAR PRIMARY KEY, USER_NO VARCHAR, INST_DTM DATETIME);

-- 시퀀스
CREATE SEQUENCE SEQ_LBS_ID;
CREATE SEQUENCE SEQ_LBS_DTL_ID;
CREATE SEQUENCE SEQ_USAGE_CODE_ID;
CREATE SEQUENCE SEQ_REGION_ID;
CREATE SEQUENCE SEQ_MAP_ID;
CREATE SEQUENCE SEQ_USER_NO;
CREATE SEQUENCE SEQ_AUTH_TRD_NO;
