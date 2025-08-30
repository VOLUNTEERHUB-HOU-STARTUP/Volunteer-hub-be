CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE profiles (
    user_id BIGINT PRIMARY KEY,
    avatar_url VARCHAR(500),
    bio TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE user_auth_provider (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    provider VARCHAR(50) NOT NULL,
    provider_id VARCHAR(255),

    CONSTRAINT fk_auth_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE invalidated_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(500) NOT NULL UNIQUE,
    expiry_time TIMESTAMP NOT NULL
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    event_date TIMESTAMP,
    published BOOLEAN DEFAULT FALSE,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_events_user FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE event_medias (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    media_url VARCHAR(500) NOT NULL,
    media_type VARCHAR(50),

    CONSTRAINT fk_event_medias_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

-- shifts
create table event_shifts (
    id uuid primary key default gen_random_uuid(),
    event_id uuid not null references events(id) on delete cascade,
    name text not null,
    starts_at timestamptz not null,
    ends_at timestamptz not null,
    capacity int not null,
    check (ends_at > starts_at),
    check (capacity >= 0)
);

-- applications
create table applications (
    id uuid primary key default gen_random_uuid(),
    event_id uuid not null references events(id) on delete cascade,
    shift_id uuid references event_shifts(id) on delete set null,
    volunteer_id uuid not null references users(id) on delete cascade,
    status application_status not null default 'pending',
    cover_message text,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    unique(event_id, volunteer_id)
);

-- conversations
create table conversations (
    id uuid primary key default gen_random_uuid(),
    is_group boolean not null default false,
    created_by uuid references users(id) on delete set null,
    created_at timestamptz not null default now()
);

create table conversation_participants (
    conversation_id uuid references conversations(id) on delete cascade,
    user_id uuid references users(id) on delete cascade,
    joined_at timestamptz not null default now(),
    primary key(conversation_id, user_id)
);

create table messages (
    id uuid primary key default gen_random_uuid(),
    conversation_id uuid not null references conversations(id) on delete cascade,
    sender_id uuid not null references users(id) on delete cascade,
    body text,
    sent_at timestamptz not null default now()
);