INSERT INTO DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NAME)
    VALUES (0, 'sales');

INSERT INTO DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NAME)
    VALUES (1, 'human resources');

INSERT INTO DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NAME)
    VALUES (2, 'engineering');

INSERT INTO DEPARTMENT (DEPARTMENT_ID, DEPARTMENT_NAME)
    VALUES (3, 'benco');

INSERT INTO REQUEST_STATUS VALUES (0, 'pending');
INSERT INTO REQUEST_STATUS VALUES (1, 'direct supervisor approval');
INSERT INTO REQUEST_STATUS VALUES (2, 'department head approval');
INSERT INTO REQUEST_STATUS VALUES (3, 'approved');
INSERT INTO REQUEST_STATUS VALUES (4, 'denied');

INSERT INTO EVENT_TYPE VALUES (0, 'university course', 80);
INSERT INTO EVENT_TYPE VALUES (1, 'seminar', 60);
INSERT INTO EVENT_TYPE VALUES (2, 'certification preparation', 75);
INSERT INTO EVENT_TYPE VALUES (3, 'certification', 100);
INSERT INTO EVENT_TYPE VALUES (4, 'technical training', 90);
INSERT INTO EVENT_TYPE VALUES (5, 'other', 30);

INSERT INTO GRADING_FORMAT VALUES (0, 'percentage', 70);
INSERT INTO GRADING_FORMAT VALUES (1, 'pass/fail', 100);

INSERT INTO EMPLOYEE_TITLE VALUES (0, 'department head');
COMMIT;

UPDATE EMPLOYEE SET EMPLOYEE_SUPER_ID=4;

SELECT * FROM EMPLOYEE;
SELECT REQUEST_STATUS_ID 
            FROM REQUEST_STATUS 
            WHERE REQUEST_STATUS_NAME = 'pending';
SELECT * FROM ADDRESS;
SELECT * FROM ATTACHMENT;
SELECT * FROM EMPLOYEE_TITLE;
SELECT * FROM REQUEST;
SELECT * FROM EVENT_TYPE;
SELECT * FROM DEPARTMENT;

DELETE FROM EMPLOYEE WHERE EMPLOYEE_ADDRESS = 314;
-------------
--For testing

INSERT INTO REQUEST (REQUEST_ID, REQUEST_EMPLOYEE_ID, REQUEST_COST, REQUEST_COURSE_DATE,
REQUEST_STATUS, REQUEST_DESCRIPTION, REQUEST_EVENT_TYPE, REQUEST_GRADING_FORMAT,
REQUEST_DAYS_MISSED, REQUEST_JUSTIFICATION, REQUEST_ADDRESS) VALUES
(0, 4, 200, TO_DATE('01-12-2018', 'mm-dd-yyyy'),
0, 'this is a test request', 0, 0,
3, 'this is a test request...', 7); 