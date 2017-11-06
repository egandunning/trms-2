/*A SCRIPT TO CREATE THE DATABASE FOR THE TRMS ACCORDING
 *TO THE SPECIFICATION IN /documentation/trms-database.png*/
/*FOR ORACLE DATABASES*/

DROP USER trms CASCADE;



CREATE USER trms
IDENTIFIED BY rvtr11740
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA 10M ON users;

GRANT connect to trms;
GRANT resource to trms;
GRANT create session TO trms;
GRANT create table TO trms;
GRANT create view TO trms;

conn trms/rvtr11740

/*CREATE TABLES*/
CREATE TABLE EMPLOYEE (
    EMPLOYEE_ID         NUMBER NOT NULL,
    EMPLOYEE_FIRSTNAME  VARCHAR2(30) NOT NULL,
    EMPLOYEE_LASTNAME   VARCHAR2(30) NOT NULL,
    EMPLOYEE_ADDRESS    NUMBER NOT NULL,
    EMPLOYEE_SUPER_ID   NUMBER,
    EMPLOYEE_DEPARTMENT NUMBER NOT NULL,
    EMPLOYEE_EMAIL      VARCHAR2(50),
    EMPLOYEE_PASSWORD   RAW(128),
    EMPLOYEE_TITLE      NUMBER,
    CONSTRAINT PK_EMPLOYEE PRIMARY KEY (EMPLOYEE_ID)
);

CREATE TABLE REQUEST(
    REQUEST_ID              NUMBER NOT NULL,
    REQUEST_EMPLOYEE_ID     NUMBER NOT NULL,
    REQUEST_COST            NUMBER(10,2) NOT NULL,
    REQUEST_COURSE_DATE     DATE NOT NULL,
    REQUEST_STATUS          NUMBER NOT NULL,
    REQUEST_ADDRESS         NUMBER NOT NULL,
    REQUEST_DESCRIPTION     VARCHAR2(500) NOT NULL,
    REQUEST_EVENT_TYPE      NUMBER NOT NULL,
    REQUEST_GRADING_FORMAT  NUMBER NOT NULL,
    REQUEST_DAYS_MISSED     NUMBER,
    REQUEST_JUSTIFICATION   VARCHAR2(500) NOT NULL,
    CONSTRAINT PK_REQUEST PRIMARY KEY (REQUEST_ID)
);

CREATE TABLE DEPARTMENT(
    DEPARTMENT_ID   NUMBER NOT NULL,
    DEPARTMENT_NAME VARCHAR2(40) NOT NULL,
    CONSTRAINT PK_DEPARTMENT PRIMARY KEY (DEPARTMENT_ID)
);

CREATE TABLE EMPLOYEE_TITLE(
    EMPLOYEE_TITLE_ID   NUMBER NOT NULL,
    EMPLOYEE_TITLE_NAME VARCHAR2(60),
    CONSTRAINT PK_EMPLOYEE_TITLE PRIMARY KEY (EMPLOYEE_TITLE_ID)
);

CREATE TABLE ADDRESS(
    ADDRESS_ID              NUMBER NOT NULL,
    ADDRESS_STREET_ADDRESS  VARCHAR2(60) NOT NULL,
    ADDRESS_CITY            VARCHAR2(30) NOT NULL,
    ADDRESS_STATE           VARCHAR2(2) NOT NULL,
    ADDRESS_ZIP             VARCHAR2(10) NOT NULL,
    CONSTRAINT PK_ADDRESS PRIMARY KEY (ADDRESS_ID)
);

CREATE TABLE EVENT_TYPE(
    EVENT_TYPE_ID               NUMBER NOT NULL,
    EVENT_TYPE_NAME             VARCHAR2(60) NOT NULL,
    EVENT_TYPE_REIMBURSE_PER    NUMBER(8,5) NOT NULL,
    CONSTRAINT PK_EVENT_TYPE PRIMARY KEY (EVENT_TYPE_ID)
);

CREATE TABLE ATTACHMENT(
    ATTACHMENT_ID               NUMBER NOT NULL,
    ATTACHMENT_FILENAME         VARCHAR2(100) NOT NULL,
    ATTACHMENT_DIRECTORY        VARCHAR2(100) NOT NULL,
    ATTACHMENT_REQUEST_ID       NUMBER NOT NULL,
    ATTACHMENT_APPROVAL_TYPE    NUMBER, 
    CONSTRAINT PK_ATTACHMENT PRIMARY KEY (ATTACHMENT_ID)
);

CREATE TABLE GRADING_FORMAT(
    GRADING_FORMAT_ID           NUMBER NOT NULL,
    GRADING_FORMAT_NAME         VARCHAR2(40) NOT NULL,
    GRADING_FORMAT_PASS_GRADE   NUMBER(8,5) NOT NULL,
    CONSTRAINT PK_GRADING_FORMAT PRIMARY KEY (GRADING_FORMAT_ID)
);

CREATE TABLE REQUEST_STATUS(
    REQUEST_STATUS_ID   NUMBER NOT NULL,
    REQUEST_STATUS_NAME VARCHAR2(60) NOT NULL,
    CONSTRAINT PK_REQUEST_STATUS PRIMARY KEY (REQUEST_STATUS_ID)
);

/*UNIQUENESS*/
ALTER TABLE EMPLOYEE ADD CONSTRAINT UNIQUE_EMAIL
    UNIQUE (EMPLOYEE_EMAIL);

ALTER TABLE ADDRESS ADD CONSTRAINT UNIQUE_ADDRESS
    UNIQUE(ADDRESS_STREET_ADDRESS, ADDRESS_CITY, ADDRESS_STATE, ADDRESS_ZIP);

ALTER TABLE DEPARTMENT ADD CONSTRAINT UNIQUE_DEPARTMENT
    UNIQUE (DEPARTMENT_NAME);

ALTER TABLE EMPLOYEE_TITLE ADD CONSTRAINT UNIQUE_TITLE
    UNIQUE (EMPLOYEE_TITLE_NAME);
    
ALTER TABLE REQUEST_STATUS ADD CONSTRAINT UNIQUE_NAME
    UNIQUE (REQUEST_STATUS_NAME);
    
ALTER TABLE ATTACHMENT ADD CONSTRAINT UNIQUE_FILENAME
    UNIQUE (ATTACHMENT_FILENAME);

/*ADD FOREIGN KEYS*/
ALTER TABLE EMPLOYEE ADD CONSTRAINT FK_EMPLOYEE_SUPER
    FOREIGN KEY (EMPLOYEE_SUPER_ID) REFERENCES
    EMPLOYEE (EMPLOYEE_ID)
    ON DELETE SET NULL;

ALTER TABLE EMPLOYEE ADD CONSTRAINT FK_EMPLOYEE_ADDRESS
    FOREIGN KEY (EMPLOYEE_ADDRESS) REFERENCES
    ADDRESS (ADDRESS_ID);

ALTER TABLE EMPLOYEE ADD CONSTRAINT FK_EMPLOYEE_TITLE
    FOREIGN KEY (EMPLOYEE_TITLE) REFERENCES
    EMPLOYEE_TITLE (EMPLOYEE_TITLE_ID);

ALTER TABLE REQUEST ADD CONSTRAINT FK_REQUEST_EMPLOYEE
    FOREIGN KEY (REQUEST_EMPLOYEE_ID) REFERENCES
    EMPLOYEE (EMPLOYEE_ID)
    ON DELETE SET NULL;

ALTER TABLE REQUEST ADD CONSTRAINT FK_REQUEST_ADDRESS
    FOREIGN KEY (REQUEST_ADDRESS) REFERENCES
    ADDRESS (ADDRESS_ID);

ALTER TABLE REQUEST ADD CONSTRAINT FK_REQUEST_STATUS
    FOREIGN KEY (REQUEST_STATUS) REFERENCES
    REQUEST_STATUS (REQUEST_STATUS_ID);

ALTER TABLE REQUEST ADD CONSTRAINT FK_REQUEST_GRADING_FORMAT
    FOREIGN KEY (REQUEST_GRADING_FORMAT) REFERENCES
    GRADING_FORMAT (GRADING_FORMAT_ID);

ALTER TABLE ATTACHMENT ADD CONSTRAINT FK_ATTACHMENT_REQUEST
    FOREIGN KEY (ATTACHMENT_REQUEST_ID) REFERENCES
    REQUEST (REQUEST_ID);

ALTER TABLE ATTACHMENT ADD CONSTRAINT FK_ATTACHMENT_REQUEST_STATUS
    FOREIGN KEY (ATTACHMENT_APPROVAL_TYPE) REFERENCES
    REQUEST_STATUS (REQUEST_STATUS_ID);

/*CREATE SEQUENCES*/

CREATE SEQUENCE EMPLOYEE_ID_SEQ MINVALUE 1 MAXVALUE 999999999999
    INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;

CREATE SEQUENCE REQUEST_ID_SEQ MINVALUE 1 MAXVALUE 999999999999999
    INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;

CREATE SEQUENCE ID_SEQ MINVALUE 1 MAXVALUE 999999999999999
    INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE;

/*CREATE TRIGGERS*/

CREATE OR REPLACE TRIGGER EMPLOYEE_INSERT
    BEFORE INSERT ON EMPLOYEE
    FOR EACH ROW
    BEGIN
        SELECT EMPLOYEE_ID_SEQ.NEXTVAL INTO :NEW.EMPLOYEE_ID FROM DUAL;
    END;
/

CREATE OR REPLACE TRIGGER REQUEST_INSERT
    BEFORE INSERT ON REQUEST
    FOR EACH ROW
    BEGIN
        SELECT REQUEST_ID_SEQ.NEXTVAL INTO :NEW.REQUEST_ID FROM DUAL;
    END;
/

CREATE OR REPLACE TRIGGER ADDRESS_INSERT
    BEFORE INSERT ON ADDRESS
    FOR EACH ROW
    BEGIN
        SELECT ID_SEQ.NEXTVAL INTO :NEW.ADDRESS_ID FROM DUAL;
    END;
/

CREATE OR REPLACE TRIGGER ATTACHMENT_INSERT
    BEFORE INSERT ON ATTACHMENT
    FOR EACH ROW
    BEGIN
        SELECT ID_SEQ.NEXTVAL INTO :NEW.ATTACHMENT_ID FROM DUAL;
    END;
/

/*STORED PROCEDURES*/

--------------------------------------------------------------------------------
-- READ ALL ROWS FROM TABLE
--------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE READ_ALL_EMPLOYEES(OUT_CURS OUT SYS_REFCURSOR) AS
    BEGIN
        OPEN OUT_CURS FOR SELECT
        E.EMPLOYEE_ID, --1
        E.EMPLOYEE_FIRSTNAME, --2
        E.EMPLOYEE_LASTNAME, --3
        ADDRESS_STREET_ADDRESS, --4
        ADDRESS_CITY, --5
        ADDRESS_STATE, --6
        ADDRESS_ZIP, --7
        SUPER.EMPLOYEE_ID, --8
        SUPER.EMPLOYEE_FIRSTNAME, --9
        SUPER.EMPLOYEE_LASTNAME, --10
        DEPARTMENT_NAME, --11
        DEPARTMENT_ID, --12
        E.EMPLOYEE_EMAIL, --13
        EMPLOYEE_TITLE_NAME --14
        FROM EMPLOYEE E
        INNER JOIN ADDRESS A ON E.EMPLOYEE_ADDRESS = A.ADDRESS_ID
        INNER JOIN EMPLOYEE SUPER ON E.EMPLOYEE_SUPER_ID = SUPER.EMPLOYEE_ID
        INNER JOIN EMPLOYEE_TITLE T ON E.EMPLOYEE_TITLE = T.EMPLOYEE_TITLE_ID
        INNER JOIN DEPARTMENT D ON E.EMPLOYEE_DEPARTMENT = D.DEPARTMENT_ID;
    END;
/

CREATE OR REPLACE PROCEDURE READ_EMPLOYEES_BY_DEPT(
    ID IN NUMBER,
    OUT_CURS OUT SYS_REFCURSOR)
    AS
    BEGIN
        OPEN OUT_CURS FOR SELECT
        E.EMPLOYEE_ID, --1
        E.EMPLOYEE_FIRSTNAME, --2
        E.EMPLOYEE_LASTNAME, --3
        ADDRESS_STREET_ADDRESS, --4
        ADDRESS_CITY, --5
        ADDRESS_STATE, --6
        ADDRESS_ZIP, --7
        SUPER.EMPLOYEE_ID, --8
        SUPER.EMPLOYEE_FIRSTNAME, --9
        SUPER.EMPLOYEE_LASTNAME, --10
        DEPARTMENT_NAME, --11
        DEPARTMENT_ID, --12
        E.EMPLOYEE_EMAIL, --13
        EMPLOYEE_TITLE_NAME --14
        FROM EMPLOYEE E
        INNER JOIN ADDRESS A ON E.EMPLOYEE_ADDRESS = A.ADDRESS_ID
        INNER JOIN EMPLOYEE SUPER ON E.EMPLOYEE_SUPER_ID = SUPER.EMPLOYEE_ID
        INNER JOIN EMPLOYEE_TITLE T ON E.EMPLOYEE_TITLE = T.EMPLOYEE_TITLE_ID
        INNER JOIN DEPARTMENT D ON E.EMPLOYEE_DEPARTMENT = D.DEPARTMENT_ID
        WHERE E.EMPLOYEE_DEPARTMENT = ID;
    END;
/

CREATE OR REPLACE PROCEDURE READ_EMPLOYEES_BY_DEPT_NAME(
    NAME IN VARCHAR2,
    OUT_CURS OUT SYS_REFCURSOR)
    AS
    BEGIN
        OPEN OUT_CURS FOR SELECT
        E.EMPLOYEE_ID, --1
        E.EMPLOYEE_FIRSTNAME, --2
        E.EMPLOYEE_LASTNAME, --3
        ADDRESS_STREET_ADDRESS, --4
        ADDRESS_CITY, --5
        ADDRESS_STATE, --6
        ADDRESS_ZIP, --7
        SUPER.EMPLOYEE_ID, --8
        SUPER.EMPLOYEE_FIRSTNAME, --9
        SUPER.EMPLOYEE_LASTNAME, --10
        DEPARTMENT_NAME, --11
        DEPARTMENT_ID, --12
        E.EMPLOYEE_EMAIL, --13
        EMPLOYEE_TITLE_NAME --14
        FROM EMPLOYEE E
        INNER JOIN ADDRESS A ON E.EMPLOYEE_ADDRESS = A.ADDRESS_ID
        INNER JOIN EMPLOYEE SUPER ON E.EMPLOYEE_SUPER_ID = SUPER.EMPLOYEE_ID
        INNER JOIN EMPLOYEE_TITLE T ON E.EMPLOYEE_TITLE = T.EMPLOYEE_TITLE_ID
        INNER JOIN DEPARTMENT D ON E.EMPLOYEE_DEPARTMENT = D.DEPARTMENT_ID
        WHERE E.EMPLOYEE_DEPARTMENT = 
        (SELECT DEPARTMENT_ID FROM DEPARTMENT WHERE DEPARTMENT_NAME = NAME);
    END;
/

CREATE OR REPLACE PROCEDURE READ_EMPLOYEE_BY_EMAIL(
    EMAIL IN VARCHAR2,
    OUT_CURS OUT SYS_REFCURSOR)
    AS
    BEGIN
        OPEN OUT_CURS FOR SELECT
        E.EMPLOYEE_ID, --1
        E.EMPLOYEE_FIRSTNAME, --2
        E.EMPLOYEE_LASTNAME, --3
        ADDRESS_STREET_ADDRESS, --4
        ADDRESS_CITY, --5
        ADDRESS_STATE, --6
        ADDRESS_ZIP, --7
        SUPER.EMPLOYEE_ID, --8
        SUPER.EMPLOYEE_FIRSTNAME, --9
        SUPER.EMPLOYEE_LASTNAME, --10
        DEPARTMENT_NAME, --11
        DEPARTMENT_ID, --12
        E.EMPLOYEE_EMAIL, --13
        EMPLOYEE_TITLE_NAME --14
        FROM EMPLOYEE E
        INNER JOIN ADDRESS A ON E.EMPLOYEE_ADDRESS = A.ADDRESS_ID
        INNER JOIN EMPLOYEE SUPER ON E.EMPLOYEE_SUPER_ID = SUPER.EMPLOYEE_ID
        INNER JOIN EMPLOYEE_TITLE T ON E.EMPLOYEE_TITLE = T.EMPLOYEE_TITLE_ID
        INNER JOIN DEPARTMENT D ON E.EMPLOYEE_DEPARTMENT = D.DEPARTMENT_ID
        WHERE E.EMPLOYEE_EMAIL = EMAIL;
    END;
/

CREATE OR REPLACE PROCEDURE READ_EMPLOYEE_BY_ID(
    ID IN NUMBER,
    OUT_CURS OUT SYS_REFCURSOR)
    AS
    BEGIN
        OPEN OUT_CURS FOR SELECT
        E.EMPLOYEE_ID, --1
        E.EMPLOYEE_FIRSTNAME, --2
        E.EMPLOYEE_LASTNAME, --3
        ADDRESS_STREET_ADDRESS, --4
        ADDRESS_CITY, --5
        ADDRESS_STATE, --6
        ADDRESS_ZIP, --7
        SUPER.EMPLOYEE_ID, --8
        SUPER.EMPLOYEE_FIRSTNAME, --9
        SUPER.EMPLOYEE_LASTNAME, --10
        DEPARTMENT_NAME, --11
        DEPARTMENT_ID, --12
        E.EMPLOYEE_EMAIL, --13
        EMPLOYEE_TITLE_NAME --14
        FROM EMPLOYEE E
        INNER JOIN ADDRESS A ON E.EMPLOYEE_ADDRESS = A.ADDRESS_ID
        INNER JOIN EMPLOYEE SUPER ON E.EMPLOYEE_SUPER_ID = SUPER.EMPLOYEE_ID
        INNER JOIN EMPLOYEE_TITLE T ON E.EMPLOYEE_TITLE = T.EMPLOYEE_TITLE_ID
        INNER JOIN DEPARTMENT D ON E.EMPLOYEE_DEPARTMENT = D.DEPARTMENT_ID
        WHERE E.EMPLOYEE_ID = ID;
    END;
/

CREATE OR REPLACE PROCEDURE READ_ALL_REQUESTS(OUT_CURS OUT SYS_REFCURSOR) AS
    BEGIN
        OPEN OUT_CURS FOR SELECT 
        REQUEST_ID,
        EMPLOYEE_ID,
        EMPLOYEE_FIRSTNAME,
        EMPLOYEE_LASTNAME,
        DEPARTMENT_NAME,
        REQUEST_COST,
        REQUEST_COURSE_DATE,
        REQUEST_STATUS_NAME,
        ADDRESS_STREET_ADDRESS,
        ADDRESS_CITY,
        ADDRESS_STATE,
        ADDRESS_ZIP,
        REQUEST_DESCRIPTION,
        EVENT_TYPE_NAME,
        EVENT_TYPE_REIMBURSE_PER,
        GRADING_FORMAT_NAME,
        GRADING_FORMAT_PASS_GRADE,
        REQUEST_DAYS_MISSED,
        REQUEST_APPROVAL_LEVEL,
        REQUEST_JUSTIFICATION
        FROM REQUEST
        INNER JOIN EMPLOYEE ON REQUEST_EMPLOYEE_ID = EMPLOYEE_ID
        INNER JOIN DEPARTMENT ON EMPLOYEE_DEPARTMENT = DEPARTMENT_ID
        INNER JOIN ADDRESS ON REQUEST_ADDRESS = ADDRESS_ID
        INNER JOIN EVENT_TYPE ON REQUEST_EVENT_TYPE = EVENT_TYPE_ID
        INNER JOIN GRADING_FORMAT ON REQUEST_GRADING_FORMAT = GRADING_FORMAT_ID
        INNER JOIN REQUEST_STATUS ON REQUEST_STATUS = REQUEST_STATUS_ID;
    END;
/

CREATE OR REPLACE PROCEDURE READ_ALL_ATTACHMENTS(OUT_CURS OUT SYS_REFCURSOR) AS
    BEGIN
        OPEN OUT_CURS FOR
        SELECT
        A.ATTACHMENT_ID,
        A.ATTACHMENT_FILENAME,
        A.ATTACHMENT_DIRECTORY,
        A.ATTACHMENT_REQUEST_ID,
        R.REQUEST_STATUS_NAME
        FROM ATTACHMENT A JOIN REQUEST_STATUS R
            ON REQUEST_STATUS_ID = ATTACHMENT_APPROVAL_TYPE;
    END;
/

CREATE OR REPLACE PROCEDURE READ_ATTACHMENTS_BY_EMPLOYEE(
    ID IN NUMBER,
    OUT_CURS OUT SYS_REFCURSOR)
    AS
    BEGIN
        OPEN OUT_CURS FOR
        SELECT
        A.ATTACHMENT_ID,
        A.ATTACHMENT_FILENAME,
        A.ATTACHMENT_DIRECTORY,
        A.ATTACHMENT_REQUEST_ID,
        R.REQUEST_STATUS_NAME
        FROM ATTACHMENT A JOIN REQUEST_STATUS R
            ON REQUEST_STATUS_ID = ATTACHMENT_APPROVAL_TYPE
        WHERE ATTACHMENT_REQUEST_ID = 
            (SELECT REQUEST_ID FROM REQUEST WHERE REQUEST_EMPLOYEE_ID = ID);
    END;
/

CREATE OR REPLACE PROCEDURE READ_REQUESTS_BY_EMPLOYEE(
    ID IN NUMBER,
    OUT_CURS OUT SYS_REFCURSOR)
   AS
    BEGIN
        OPEN OUT_CURS FOR SELECT 
        REQUEST_ID,
        EMPLOYEE_ID,
        EMPLOYEE_FIRSTNAME,
        EMPLOYEE_LASTNAME,
        DEPARTMENT_NAME,
        REQUEST_COST,
        REQUEST_COURSE_DATE,
        REQUEST_STATUS_NAME,
        ADDRESS_STREET_ADDRESS,
        ADDRESS_CITY,
        ADDRESS_STATE,
        ADDRESS_ZIP,
        REQUEST_DESCRIPTION,
        EVENT_TYPE_NAME,
        EVENT_TYPE_REIMBURSE_PER,
        GRADING_FORMAT_NAME,
        GRADING_FORMAT_PASS_GRADE,
        REQUEST_DAYS_MISSED,
        REQUEST_JUSTIFICATION
        FROM REQUEST
        INNER JOIN EMPLOYEE ON REQUEST_EMPLOYEE_ID = EMPLOYEE_ID
        INNER JOIN DEPARTMENT ON EMPLOYEE_DEPARTMENT = DEPARTMENT_ID
        INNER JOIN ADDRESS ON REQUEST_ADDRESS = ADDRESS_ID
        INNER JOIN EVENT_TYPE ON REQUEST_EVENT_TYPE = EVENT_TYPE_ID
        INNER JOIN GRADING_FORMAT ON REQUEST_GRADING_FORMAT = GRADING_FORMAT_ID
        INNER JOIN REQUEST_STATUS ON REQUEST_STATUS = REQUEST_STATUS_ID
        WHERE REQUEST_EMPLOYEE_ID = ID;
    END;
/

CREATE OR REPLACE PROCEDURE READ_SUB_REQUESTS_BY_EMPLOYEE(
    ID IN NUMBER,
    OUT_CURS OUT SYS_REFCURSOR)
   AS
    BEGIN
        OPEN OUT_CURS FOR SELECT 
        REQUEST_ID,
        E.EMPLOYEE_ID,
        E.EMPLOYEE_FIRSTNAME,
        E.EMPLOYEE_LASTNAME,
        DEPARTMENT_NAME,
        REQUEST_COST,
        REQUEST_COURSE_DATE,
        REQUEST_STATUS_NAME,
        ADDRESS_STREET_ADDRESS,
        ADDRESS_CITY,
        ADDRESS_STATE,
        ADDRESS_ZIP,
        REQUEST_DESCRIPTION,
        EVENT_TYPE_NAME,
        EVENT_TYPE_REIMBURSE_PER,
        GRADING_FORMAT_NAME,
        GRADING_FORMAT_PASS_GRADE,
        REQUEST_DAYS_MISSED,
        REQUEST_JUSTIFICATION
        FROM REQUEST
        INNER JOIN EMPLOYEE E ON REQUEST_EMPLOYEE_ID = EMPLOYEE_ID
        INNER JOIN DEPARTMENT ON EMPLOYEE_DEPARTMENT = DEPARTMENT_ID
        INNER JOIN ADDRESS ON REQUEST_ADDRESS = ADDRESS_ID
        INNER JOIN EVENT_TYPE ON REQUEST_EVENT_TYPE = EVENT_TYPE_ID
        INNER JOIN GRADING_FORMAT ON REQUEST_GRADING_FORMAT = GRADING_FORMAT_ID
        INNER JOIN REQUEST_STATUS ON REQUEST_STATUS = REQUEST_STATUS_ID
        WHERE E.EMPLOYEE_SUPER_ID = ID;
    END;
/

CREATE OR REPLACE PROCEDURE READ_ATTACHMENTS_BY_REQUEST(
    ID IN NUMBER,
    OUT_CURS OUT SYS_REFCURSOR)
    AS
    BEGIN
        OPEN OUT_CURS FOR
        SELECT
        A.ATTACHMENT_ID,
        A.ATTACHMENT_FILENAME,
        A.ATTACHMENT_DIRECTORY,
        A.ATTACHMENT_REQUEST_ID,
        R.REQUEST_STATUS_NAME
        FROM ATTACHMENT A JOIN REQUEST_STATUS R
            ON REQUEST_STATUS_ID = ATTACHMENT_APPROVAL_TYPE
        WHERE ATTACHMENT_REQUEST_ID = ID;
    END;
/

CREATE OR REPLACE PROCEDURE READ_ALL_DEPARTMENTS(OUT_CURS OUT SYS_REFCURSOR) AS
    BEGIN
        OPEN OUT_CURS FOR
        SELECT DEPARTMENT_ID, DEPARTMENT_NAME FROM DEPARTMENT;
    END;
/

CREATE OR REPLACE PROCEDURE READ_ALL_EVENT_TYPES(OUT_CURS OUT SYS_REFCURSOR) AS
    BEGIN
        OPEN OUT_CURS FOR
        SELECT EVENT_TYPE_ID, EVENT_TYPE_NAME, EVENT_TYPE_REIMBURSE_PER
        FROM EVENT_TYPE;
    END;
/

CREATE OR REPLACE PROCEDURE READ_ALL_GRADING_FORMATS(OUT_CURS OUT SYS_REFCURSOR)
    AS
    BEGIN
        OPEN OUT_CURS FOR
        SELECT GRADING_FORMAT_ID, GRADING_FORMAT_NAME, GRADING_FORMAT_PASS_GRADE
        FROM GRADING_FORMAT;
    END;
/

CREATE OR REPLACE PROCEDURE READ_ALL_REQUEST_STATUSES
    (OUT_CURS OUT SYS_REFCURSOR) AS
    BEGIN
        OPEN OUT_CURS FOR
        SELECT REQUEST_STATUS_ID, REQUEST_STATUS_NAME FROM REQUEST_STATUS;
    END;
/

--------------------------------------------------------------------------------
-- INSERT
--------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE INSERT_EMPLOYEE(
    FIRSTNAME IN VARCHAR2, --1
    LASTNAME IN VARCHAR2, --2
    STREET_ADDRESS IN VARCHAR2, --3
    CITY IN VARCHAR2, --4
    STATE IN VARCHAR2, --5
    ZIP IN VARCHAR2, --6
    SUPERVISOR_ID IN NUMBER, --7
    DEPARTMENT_ID IN NUMBER, --8
    EMAIL IN VARCHAR2, --9
    PASSWORD IN RAW, --10
    TITLE IN VARCHAR2, --11
    ADDRESS_ID_NUM OUT NUMBER, --12
    TITLE_ID OUT NUMBER) --13
    AS      
    BEGIN
        BEGIN
            INSERT INTO ADDRESS (
            ADDRESS_STREET_ADDRESS,
            ADDRESS_CITY,
            ADDRESS_STATE,
            ADDRESS_ZIP)
            VALUES (
            STREET_ADDRESS,
            CITY,
            STATE,
            ZIP);
        EXCEPTION
            WHEN OTHERS THEN
            NULL;
        END;
        
        SELECT ADDRESS_ID INTO ADDRESS_ID_NUM FROM ADDRESS WHERE 
        ADDRESS_STREET_ADDRESS = STREET_ADDRESS AND
        ADDRESS_CITY = CITY AND
        ADDRESS_STATE = STATE AND
        ADDRESS_ZIP = ZIP;

        BEGIN
            INSERT INTO EMPLOYEE_TITLE (EMPLOYEE_TITLE_ID, EMPLOYEE_TITLE_NAME)
            VALUES (ID_SEQ.NEXTVAL, TITLE);
        EXCEPTION
            WHEN OTHERS THEN
            NULL;
        END;

        SELECT EMPLOYEE_TITLE_ID INTO TITLE_ID FROM EMPLOYEE_TITLE WHERE
        EMPLOYEE_TITLE_NAME = TITLE;

        INSERT INTO EMPLOYEE (
        EMPLOYEE_FIRSTNAME,
        EMPLOYEE_LASTNAME,
        EMPLOYEE_ADDRESS,
        EMPLOYEE_SUPER_ID,
        EMPLOYEE_DEPARTMENT,
        EMPLOYEE_EMAIL,
        EMPLOYEE_PASSWORD,
        EMPLOYEE_TITLE)
        VALUES (
        FIRSTNAME,
        LASTNAME,
        ADDRESS_ID_NUM,
        SUPERVISOR_ID,
        DEPARTMENT_ID,
        EMAIL,
        PASSWORD,
        TITLE_ID);
    END;
/

CREATE OR REPLACE PROCEDURE INSERT_ATTACHMENT (
    FILENAME IN VARCHAR2,
    DIRECTORY IN VARCHAR2,
    REQUEST_ID IN NUMBER,
    APPROVAL_TYPE IN VARCHAR2)
    AS
    BEGIN
        INSERT INTO ATTACHMENT (
        ATTACHMENT_ID,
        ATTACHMENT_FILENAME,
        ATTACHMENT_DIRECTORY,
        ATTACHMENT_REQUEST_ID,
        ATTACHMENT_APPROVAL_TYPE)
        VALUES (
        1,
        FILENAME,
        DIRECTORY,
        REQUEST_ID,
        (SELECT REQUEST_STATUS_ID 
            FROM REQUEST_STATUS 
            WHERE REQUEST_STATUS_NAME = APPROVAL_TYPE));
    END;
/

CREATE OR REPLACE PROCEDURE INSERT_REQUEST (
    EMPLOYEE_ID IN NUMBER,
    COST IN NUMBER,
    COURSE_DATE IN DATE,
    STREET_ADDRESS IN VARCHAR2,
    CITY IN VARCHAR2,
    STATE IN VARCHAR2,
    ZIP IN VARCHAR2,
    DESCRIPTION IN VARCHAR2,
    EVENT_TYPE IN NUMBER,
    GRADE_FORMAT IN NUMBER,
    DAYS_MISSED IN NUMBER,
    JUSTIFICATION IN VARCHAR2,
    ADDRESS_ID_NUM OUT NUMBER)
    AS
    BEGIN
        BEGIN
            INSERT INTO ADDRESS (ADDRESS_STREET_ADDRESS, ADDRESS_CITY,
            ADDRESS_STATE, ADDRESS_ZIP) 
            VALUES (
            STREET_ADDRESS,
            CITY,
            STATE,
            ZIP);
        EXCEPTION
            WHEN OTHERS THEN
            NULL;
        END;
        
        SELECT ADDRESS_ID INTO ADDRESS_ID_NUM FROM ADDRESS WHERE
        ADDRESS_STREET_ADDRESS = STREET_ADDRESS AND
        ADDRESS_CITY = CITY AND
        ADDRESS_STATE = STATE AND
        ADDRESS_ZIP = ZIP;
        
        INSERT INTO REQUEST (
        REQUEST_EMPLOYEE_ID,
        REQUEST_COST,
        REQUEST_COURSE_DATE,
        REQUEST_STATUS,
        REQUEST_ADDRESS,
        REQUEST_DESCRIPTION,
        REQUEST_EVENT_TYPE,
        REQUEST_GRADING_FORMAT,
        REQUEST_DAYS_MISSED,
        REQUEST_JUSTIFICATION)
        VALUES (
        EMPLOYEE_ID,
        COST,
        COURSE_DATE,
        0,
        ADDRESS_ID_NUM,
        DESCRIPTION,
        EVENT_TYPE,
        GRADE_FORMAT,
        DAYS_MISSED,
        JUSTIFICATION);
    END;
/

--------------------------------------------------------------------------------
-- UPDATE
--------------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE UPDATE_EMPLOYEE_WITH_ID(
    ID IN NUMBER,
    FIRSTNAME IN VARCHAR2, 
    LASTNAME IN VARCHAR2,
    STREET_ADDRESS IN VARCHAR2,
    CITY IN VARCHAR2,
    STATE IN VARCHAR2,
    ZIP IN VARCHAR2,
    SUPERVISOR_ID IN NUMBER,
    DEPARTMENT_ID IN NUMBER,
    EMAIL IN VARCHAR2,
    PASSWORD IN RAW,
    TITLE IN VARCHAR2,
    ADDRESS_ID_NUM OUT NUMBER,
    TITLE_ID OUT NUMBER)
    AS      
    BEGIN
        BEGIN
            INSERT INTO ADDRESS (
            ADDRESS_STREET_ADDRESS,
            ADDRESS_CITY,
            ADDRESS_STATE,
            ADDRESS_ZIP)
            VALUES (
            STREET_ADDRESS,
            CITY,
            STATE,
            ZIP);
        
        EXCEPTION
            WHEN OTHERS THEN
            NULL;
        END;
        
        SELECT ADDRESS_ID INTO ADDRESS_ID_NUM FROM ADDRESS WHERE 
        ADDRESS_STREET_ADDRESS = STREET_ADDRESS AND
        ADDRESS_CITY = CITY AND
        ADDRESS_STATE = STATE AND
        ADDRESS_ZIP = ZIP;

        BEGIN
            INSERT INTO EMPLOYEE_TITLE (EMPLOYEE_TITLE_ID, EMPLOYEE_TITLE_NAME)
            VALUES (ID_SEQ.NEXTVAL, TITLE);
        EXCEPTION
            WHEN OTHERS THEN
            NULL;
        END;

        SELECT EMPLOYEE_TITLE_ID INTO TITLE_ID FROM EMPLOYEE_TITLE WHERE
        EMPLOYEE_TITLE_NAME = TITLE;

        UPDATE EMPLOYEE SET
        EMPLOYEE_FIRSTNAME = FIRSTNAME,
        EMPLOYEE_LASTNAME = LASTNAME,
        EMPLOYEE_ADDRESS = ADDRESS_ID_NUM,
        EMPLOYEE_SUPER_ID = SUPERVISOR_ID,
        EMPLOYEE_DEPARTMENT = DEPARTMENT_ID,
        EMPLOYEE_EMAIL = EMAIL,
        EMPLOYEE_PASSWORD = PASSWORD,
        EMPLOYEE_TITLE = TITLE_ID
        WHERE EMPLOYEE_ID = ID;
    END;
/

CREATE OR REPLACE PROCEDURE UPDATE_EMPLOYEE_WITH_EMAIL(
    OLD_EMAIL IN VARCHAR2,
    FIRSTNAME IN VARCHAR2, 
    LASTNAME IN VARCHAR2,
    STREET_ADDRESS IN VARCHAR2,
    CITY IN VARCHAR2,
    STATE IN VARCHAR2,
    ZIP IN VARCHAR2,
    SUPERVISOR_ID IN NUMBER,
    DEPARTMENT_ID IN NUMBER,
    EMAIL IN VARCHAR2,
    PASSWORD IN RAW,
    TITLE IN VARCHAR2,
    ADDRESS_ID_NUM OUT NUMBER,
    TITLE_ID OUT NUMBER)
    AS      
    BEGIN
        BEGIN
            INSERT INTO ADDRESS (
            ADDRESS_STREET_ADDRESS,
            ADDRESS_CITY,
            ADDRESS_STATE,
            ADDRESS_ZIP)
            VALUES (
            STREET_ADDRESS,
            CITY,
            STATE,
            ZIP);
        EXCEPTION
            WHEN OTHERS THEN
            NULL;
        END;
        
        SELECT ADDRESS_ID INTO ADDRESS_ID_NUM FROM ADDRESS WHERE 
        ADDRESS_STREET_ADDRESS = STREET_ADDRESS AND
        ADDRESS_CITY = CITY AND
        ADDRESS_STATE = STATE AND
        ADDRESS_ZIP = ZIP;

        BEGIN
            INSERT INTO EMPLOYEE_TITLE (EMPLOYEE_TITLE_ID, EMPLOYEE_TITLE_NAME)
            VALUES (ID_SEQ.NEXTVAL, TITLE);
        EXCEPTION
            WHEN OTHERS THEN
            NULL;
        END;

        SELECT EMPLOYEE_TITLE_ID INTO TITLE_ID FROM EMPLOYEE_TITLE WHERE
        EMPLOYEE_TITLE_NAME = TITLE;

        UPDATE EMPLOYEE SET
        EMPLOYEE_FIRSTNAME = FIRSTNAME,
        EMPLOYEE_LASTNAME = LASTNAME,
        EMPLOYEE_ADDRESS = ADDRESS_ID_NUM,
        EMPLOYEE_SUPER_ID = SUPERVISOR_ID,
        EMPLOYEE_DEPARTMENT = DEPARTMENT_ID,
        EMPLOYEE_EMAIL = EMAIL,
        EMPLOYEE_PASSWORD = PASSWORD,
        EMPLOYEE_TITLE = TITLE_ID
        WHERE EMPLOYEE_EMAIL = OLD_EMAIL;
    END;
/

CREATE OR REPLACE PROCEDURE UPDATE_ATTACHMENT(
    OLD_ID IN NUMBER,
    FILENAME IN VARCHAR2,
    DIRECTORY IN VARCHAR2,
    REQUEST_ID IN NUMBER,
    APPROVAL_TYPE IN VARCHAR2)
    AS
    BEGIN
        UPDATE ATTACHMENT SET
        ATTACHMENT_FILENAME = FILENAME,
        ATTACHMENT_DIRECTORY = DIRECTORY,
        ATTACHMENT_REQUEST_ID = REQUEST_ID,
        ATTACHMENT_APPROVAL_TYPE = 
            (SELECT REQUEST_STATUS_ID FROM REQUEST_STATUS 
            WHERE REQUEST_STATUS_NAME = APPROVAL_TYPE)
        WHERE ATTACHMENT_ID = OLD_ID;
    END;
/

CREATE OR REPLACE PROCEDURE UPDATE_REQUEST(
    OLD_ID IN NUMBER,
    FILENAME IN VARCHAR2,
    DIRECTORY IN VARCHAR2,
    REQUEST_ID IN NUMBER,
    APPROVAL_TYPE IN VARCHAR2)
    AS
    BEGIN
        UPDATE ATTACHMENT SET
        ATTACHMENT_FILENAME = FILENAME,
        ATTACHMENT_DIRECTORY = DIRECTORY,
        ATTACHMENT_REQUEST_ID = REQUEST_ID,
        ATTACHMENT_APPROVAL_TYPE = 
            (SELECT REQUEST_STATUS_ID FROM REQUEST_STATUS 
            WHERE REQUEST_STATUS_NAME = APPROVAL_TYPE)
        WHERE ATTACHMENT_ID = OLD_ID;
    END;
/

CREATE OR REPLACE PROCEDURE UPDATE_REQUEST(

	END;
/
--------------------------------------------------------------------------------
-- DELETE
--------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE DELETE_EMPLOYEE_WITH_ID(ID IN NUMBER) AS
    BEGIN
        UPDATE EMPLOYEE SET
        EMPLOYEE_SUPER_ID = 
        (SELECT EMPLOYEE_SUPER_ID FROM EMPLOYEE
        WHERE EMPLOYEE_ID = ID)
        WHERE EMPLOYEE_SUPER_ID = ID;
        
        DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID = ID;
    END;
/

CREATE OR REPLACE PROCEDURE DELETE_REQUEST_WITH_ID(ID IN NUMBER) AS
    BEGIN
        
        DELETE FROM REQUEST WHERE REQUEST_ID = ID;
    END;
/

CREATE OR REPLACE PROCEDURE DELETE_EMPLOYEE_WITH_EMAIL(EMAIL IN VARCHAR2) AS
    BEGIN
        UPDATE EMPLOYEE SET
        EMPLOYEE_SUPER_ID = 
        (SELECT EMPLOYEE_SUPER_ID FROM EMPLOYEE
        WHERE EMPLOYEE_EMAIL = EMAIL)
        WHERE EMPLOYEE_SUPER_ID = 
        (SELECT EMPLOYEE_ID FROM EMPLOYEE
        WHERE EMPLOYEE_EMAIL = EMAIL);
        
        DELETE FROM EMPLOYEE WHERE EMPLOYEE_EMAIL = EMAIL;
    END;
/