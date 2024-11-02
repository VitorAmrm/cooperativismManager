
CREATE TABLE IF NOT EXISTS dmn_member (
    member_id BIGINT PRIMARY KEY,
    nationalId VARCHAR(11) NOT NULL,
    voting_session_id BIGINT UNIQUE,
     removed BOOLEAN DEFAULT false,
     version NUMERIC DEFAULT 0
);

CREATE TYPE dmn_meeting_agenda_status  AS ENUM ('OPEN', 'CLOSED');

CREATE TABLE IF NOT EXISTS dmn_meeting_agenda (
    meeting_agenda_id BIGINT PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status dmn_meeting_agenda_status DEFAULT 'CLOSED',
    removed BOOLEAN DEFAULT false,
    version NUMERIC DEFAULT 0
);

CREATE TABLE IF NOT EXISTS dmn_voting_session (
    voting_session_id BIGINT PRIMARY KEY,
    meeting_agenda_id BIGINT NOT NULL,
    opened_at TIMESTAMP,
    closed_at TIMESTAMP,
    removed BOOLEAN DEFAULT false,
    version NUMERIC DEFAULT 0
);

CREATE TABLE IF NOT EXISTS dmn_vote (
    vote_id BIGINT PRIMARY KEY,
    value BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    voting_session_id BIGINT REFERENCES dmn_voting_session(voting_session_id) NOT NULL,
    removed BOOLEAN DEFAULT false,
    version NUMERIC DEFAULT 0
);

CREATE SEQUENCE sq_member
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE sq_meeting_agenda
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE sq_voting_session
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE sq_vote
START WITH 1
INCREMENT BY 1;
