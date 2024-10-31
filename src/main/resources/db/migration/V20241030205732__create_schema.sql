
CREATE TABLE IF NOT EXISTS dmn_member (
    member_id BIGINT PRIMARY KEY,
    nationalId VARCHAR(11) NOT NULL,
    voting_session_id BIGINT UNIQUE,
    version BIGINT,
    removed BOOLEAN DEFAULT FALSE
);

CREATE TYPE dmn_meeting_agenda_status  AS ENUM ('OPEN', 'CLOSED');

CREATE TABLE IF NOT EXISTS dmn_meeting_agenda (
    meeting_agenda_id BIGINT PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status dmn_meeting_agenda_status DEFAULT 'CLOSED'
);

CREATE TABLE IF NOT EXISTS dmn_voting_session (
    voting_session_id BIGINT PRIMARY KEY,
    opened_at TIMESTAMP,
    closed_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dmn_meeting_agenda_voting_session (
    voting_session_id BIGINT REFERENCES dmn_voting_session(voting_session_id) ON DELETE CASCADE,
    meeting_agenda_id BIGINT REFERENCES dmn_meeting_agenda(meeting_agenda_id) ON DELETE CASCADE,
    PRIMARY KEY (meeting_agenda_id, voting_session_id)
);

CREATE TABLE IF NOT EXISTS dmn_vote (
    vote_id BIGINT PRIMARY KEY,
    value BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    voting_session_id BIGINT REFERENCES dmn_voting_session(voting_session_id) NOT NULL
);
