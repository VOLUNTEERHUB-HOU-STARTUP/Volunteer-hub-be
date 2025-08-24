create extension if not exists citext;
create extension if not exists "pgcrypto"; -- hỗ trợ UUID

create type application_status as enum ('pending','accepted','rejected','withdrawn');

-- roles
create table roles (
    id uuid primary key default gen_random_uuid(),
    role text unique not null -- admin, organizer, volunteer
);

-- users: có thể là cá nhân hoặc tổ chức
create table users (
    id uuid primary key default gen_random_uuid(),
    email citext unique not null,
    password text not null,
    full_name text not null,
    is_active boolean not null default true,
    created_at timestamptz not null default now(),
    role_id uuid,
    constraint fk_role foreign key (role_id) references roles(id) on delete set null
);

-- events do user (tổ chức) tạo
create table events (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references users(id) on delete cascade,
    title text not null,
    slug text unique not null,
    start_at timestamptz not null,
    end_at timestamptz not null,
    max_volunteers int,
    is_published boolean not null default false,
    created_at timestamptz not null default now(),
    check (end_at > start_at)
);

-- event_media
create table event_media (
    id uuid primary key default gen_random_uuid(),
    event_id uuid not null references events(id) on delete cascade,
    media_type text not null check(media_type in ('VIDEO', 'IMAGE')),
    url text not null,
    created_at timestamptz not null default now()
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