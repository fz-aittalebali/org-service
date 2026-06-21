-- Countries
CREATE TABLE countries (
    id              UUID         NOT NULL,
    name            VARCHAR(150) NOT NULL,
    domain_created_at TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    CONSTRAINT pk_countries PRIMARY KEY (id),
    CONSTRAINT uq_countries_name UNIQUE (name)
);

-- Cities
CREATE TABLE cities (
    id              UUID         NOT NULL,
    name            VARCHAR(150) NOT NULL,
    zip_code        VARCHAR(20)  NOT NULL,
    country_id      UUID         NOT NULL,
    domain_created_at TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    CONSTRAINT pk_cities PRIMARY KEY (id),
    CONSTRAINT uq_cities_name_country UNIQUE (name, country_id),
    CONSTRAINT fk_cities_country FOREIGN KEY (country_id) REFERENCES countries (id)
);

-- Companies
CREATE TABLE companies (
    id              UUID         NOT NULL,
    name            VARCHAR(150) NOT NULL,
    domain_created_at TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    CONSTRAINT pk_companies PRIMARY KEY (id),
    CONSTRAINT uq_companies_name UNIQUE (name)
);

-- Campuses
CREATE TABLE campuses (
    id              UUID         NOT NULL,
    name            VARCHAR(150) NOT NULL,
    company_id      UUID         NOT NULL,
    city_id         UUID         NOT NULL,
    domain_created_at TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    CONSTRAINT pk_campuses PRIMARY KEY (id),
    CONSTRAINT uq_campuses_name_company UNIQUE (name, company_id),
    CONSTRAINT fk_campuses_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_campuses_city FOREIGN KEY (city_id) REFERENCES cities (id)
);

-- Departments
CREATE TABLE departments (
    id              UUID         NOT NULL,
    name            VARCHAR(150) NOT NULL,
    campus_id       UUID         NOT NULL,
    domain_created_at TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    CONSTRAINT pk_departments PRIMARY KEY (id),
    CONSTRAINT uq_departments_name_campus UNIQUE (name, campus_id),
    CONSTRAINT fk_departments_campus FOREIGN KEY (campus_id) REFERENCES campuses (id)
);

-- Teams
CREATE TABLE teams (
    id              UUID         NOT NULL,
    name            VARCHAR(150) NOT NULL,
    department_id   UUID         NOT NULL,
    domain_created_at TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ,
    created_by      VARCHAR(255),
    updated_by      VARCHAR(255),
    CONSTRAINT pk_teams PRIMARY KEY (id),
    CONSTRAINT uq_teams_name_department UNIQUE (name, department_id),
    CONSTRAINT fk_teams_department FOREIGN KEY (department_id) REFERENCES departments (id)
);

-- Outbox events
CREATE TABLE outbox_events (
    id              UUID         NOT NULL,
    event_type      VARCHAR(255) NOT NULL,
    topic           VARCHAR(255) NOT NULL,
    aggregate_id    VARCHAR(255) NOT NULL,
    payload         TEXT         NOT NULL,
    occurred_on     TIMESTAMPTZ  NOT NULL,
    published       BOOLEAN      NOT NULL DEFAULT FALSE,
    published_at    TIMESTAMPTZ,
    CONSTRAINT pk_outbox_events PRIMARY KEY (id)
);

CREATE INDEX idx_outbox_events_published_occurred ON outbox_events (published, occurred_on)
    WHERE published = FALSE;

-- ShedLock
CREATE TABLE shedlock (
    name        VARCHAR(64)  NOT NULL,
    lock_until  TIMESTAMPTZ  NOT NULL,
    locked_at   TIMESTAMPTZ  NOT NULL,
    locked_by   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_shedlock PRIMARY KEY (name)
);
