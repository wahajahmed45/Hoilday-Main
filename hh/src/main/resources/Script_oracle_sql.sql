-- Drop existing tables
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE helmo_documents CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE helmo_event CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE helmo_activity_participant CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE helmo_roles CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE helmo_participant CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE helmo_activity CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE helmo_vacation_participant CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE helmo_vacation CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE helmo_schedule CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE helmo_place CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE helmo_users CASCADE CONSTRAINTS';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -942 THEN
            RAISE;
        END IF;
END;
/

-- Create sequences
CREATE SEQUENCE helmo_users_seq START WITH 101 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE helmo_roles_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE helmo_place_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE helmo_vacation_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE helmo_activity_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE helmo_documents_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE helmo_participant_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE helmo_schedule_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE helmo_event_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- Create tables
CREATE TABLE helmo_users (
                             ID NUMBER PRIMARY KEY,
                             nom VARCHAR2(30 CHAR) NOT NULL,
                             prenom VARCHAR2(30 CHAR) NOT NULL,
                             email VARCHAR2(100 CHAR) NOT NULL UNIQUE,
                             mot_de_passe VARCHAR2(100 CHAR) NOT NULL,
                             pictureUrl VARCHAR2(255 CHAR)
);

CREATE TABLE helmo_roles (
                             id NUMBER PRIMARY KEY,
                             name VARCHAR2(20 CHAR),
                             user_id NUMBER,
                             CONSTRAINT fk_role_user FOREIGN KEY (user_id) REFERENCES helmo_users (id)
);

CREATE TABLE helmo_place (
                             id NUMBER PRIMARY KEY,
                             longitude NUMBER(9, 6) NOT NULL CHECK (longitude BETWEEN -180 AND 180),
                             latitude NUMBER(8, 6) NOT NULL CHECK (latitude BETWEEN -90 AND 90),
                             street VARCHAR2(70 CHAR),
                             "number" VARCHAR2(15 CHAR),
                             postalCode VARCHAR2(15 CHAR),
                             city VARCHAR2(50 CHAR),
                             country VARCHAR2(50 CHAR) NOT NULL
);

CREATE TABLE helmo_vacation (
                                id NUMBER PRIMARY KEY,
                                name VARCHAR2(50 CHAR) NOT NULL,
                                description VARCHAR2(250 CHAR) NOT NULL,
                                startDate TIMESTAMP WITH TIME ZONE NOT NULL,
                                endDate TIMESTAMP WITH TIME ZONE NOT NULL,
                                user_id NUMBER,
                                status VARCHAR2(255 CHAR),
                                place_id NUMBER NOT NULL,
                                CONSTRAINT fk_vacation_user FOREIGN KEY (user_id) REFERENCES helmo_users (id),
                                CONSTRAINT fk_vacation_place FOREIGN KEY (place_id) REFERENCES helmo_place (id)
);

CREATE TABLE helmo_activity (
                                id NUMBER PRIMARY KEY,
                                name VARCHAR2(50 CHAR) NOT NULL,
                                description VARCHAR2(50 CHAR) NOT NULL,
                                startDate TIMESTAMP WITH TIME ZONE NOT NULL,
                                endDate TIMESTAMP WITH TIME ZONE NOT NULL,
                                status VARCHAR2(255 CHAR),
                                user_id NUMBER NOT NULL,
                                place_id NUMBER NOT NULL,
                                vacation_id NUMBER,
                                CONSTRAINT fk_activity_user FOREIGN KEY (user_id) REFERENCES helmo_users (id),
                                CONSTRAINT fk_activity_place FOREIGN KEY (place_id) REFERENCES helmo_place (id),
                                CONSTRAINT fk_activity_vacation FOREIGN KEY (vacation_id) REFERENCES helmo_vacation (id)
);

CREATE TABLE helmo_documents (
                                 id NUMBER PRIMARY KEY,
                                 fileName VARCHAR2(255 CHAR) NOT NULL,
                                 fileUri VARCHAR2(255 CHAR) NOT NULL,
                                 units VARCHAR2(255 CHAR),
                                 user_id NUMBER NOT NULL,
                                 vacation_id NUMBER NOT NULL,
                                 CONSTRAINT fk_document_user FOREIGN KEY (user_id) REFERENCES helmo_users (id),
                                 CONSTRAINT fk_document_vacation FOREIGN KEY (vacation_id) REFERENCES helmo_vacation (id)
);

CREATE TABLE helmo_participant (
                                   id NUMBER PRIMARY KEY,
                                   email VARCHAR2(100 CHAR) NOT NULL UNIQUE,
                                   nom VARCHAR2(30 CHAR) NOT NULL,
                                   prenom VARCHAR2(30 CHAR) NOT NULL
);

CREATE TABLE helmo_activity_participant (
                                            activity_id NUMBER,
                                            participant_id NUMBER,
                                            PRIMARY KEY (activity_id, participant_id),
                                            CONSTRAINT fk_activity_participant_act FOREIGN KEY (activity_id) REFERENCES helmo_activity (id),
                                            CONSTRAINT fk_activity_participant_part FOREIGN KEY (participant_id) REFERENCES helmo_participant (id)
);

CREATE TABLE helmo_vacation_participant (
                                            vacation_id NUMBER,
                                            participant_id NUMBER,
                                            PRIMARY KEY (vacation_id, participant_id),
                                            CONSTRAINT fk_vacation_participant_vac FOREIGN KEY (vacation_id) REFERENCES helmo_vacation (id),
                                            CONSTRAINT fk_vacation_participant_part FOREIGN KEY (participant_id) REFERENCES helmo_participant (id)
);

CREATE TABLE helmo_schedule (
                                id NUMBER PRIMARY KEY,
                                title VARCHAR2(255 CHAR) NOT NULL,
                                user_id NUMBER NOT NULL,
                                CONSTRAINT fk_schedule_user FOREIGN KEY (user_id) REFERENCES helmo_users (id)
);

CREATE TABLE helmo_event (
                             id NUMBER PRIMARY KEY,
                             title VARCHAR2(255 CHAR) NOT NULL,
                             description VARCHAR2(255 CHAR),
                             startDate TIMESTAMP WITH TIME ZONE NOT NULL,
                             endDate TIMESTAMP WITH TIME ZONE NOT NULL,
                             schedule_id NUMBER NOT NULL,
                             activity_id NUMBER NOT NULL UNIQUE,
                             CONSTRAINT fk_event_schedule FOREIGN KEY (schedule_id) REFERENCES helmo_schedule (id),
                             CONSTRAINT fk_event_activity FOREIGN KEY (activity_id) REFERENCES helmo_activity (id)
);

-- Create triggers to auto-increment IDs
CREATE OR REPLACE TRIGGER helmo_users_before_insert
    BEFORE INSERT ON helmo_users
    FOR EACH ROW
BEGIN
    SELECT helmo_users_seq.NEXTVAL INTO :NEW.ID FROM dual;
END;
/

CREATE OR REPLACE TRIGGER helmo_roles_before_insert
    BEFORE INSERT ON helmo_roles
    FOR EACH ROW
BEGIN
    SELECT helmo_roles_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER helmo_place_before_insert
    BEFORE INSERT ON helmo_place
    FOR EACH ROW
BEGIN
    SELECT helmo_place_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER helmo_vacation_before_insert
    BEFORE INSERT ON helmo_vacation
    FOR EACH ROW
BEGIN
    SELECT helmo_vacation_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER helmo_activity_before_insert
    BEFORE INSERT ON helmo_activity
    FOR EACH ROW
BEGIN
    SELECT helmo_activity_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER helmo_documents_before_insert
    BEFORE INSERT ON helmo_documents
    FOR EACH ROW
BEGIN
    SELECT helmo_documents_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER helmo_part_before_insert
    BEFORE INSERT ON helmo_participant
    FOR EACH ROW
BEGIN
    SELECT helmo_participant_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER helmo_schedule_before_insert
    BEFORE INSERT ON helmo_schedule
    FOR EACH ROW
BEGIN
    SELECT helmo_schedule_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER helmo_event_before_insert
    BEFORE INSERT ON helmo_event
    FOR EACH ROW
BEGIN
    SELECT helmo_event_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/
