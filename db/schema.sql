create extension if not exists citext;

create type application_status as enum ('pending','accepted','rejected','withdrawn');

create table users (
    id serial primary key,
    email citext unique not null,
    password text not null,
    full_name text not null,
    is_active boolean not null default true,
    created_at timestamptz not null default now(),
	role_id int,

    constraint fk_role foreign key (role_id) references roles(id) on delete set null
);

create table roles (
    id serial primary key,
    role text unique not null -- admin, organizer, volunteer
);

create table events (
    id serial primary key,
    organization_id int not null references organizations(id) on delete cascade,
    title text not null,
    slug text unique not null,
    starts_at timestamptz not null,
    ends_at timestamptz not null,
    max_volunteers int,
    is_published boolean not null default false,
    created_at timestamptz not null default now(),
    check (ends_at > starts_at)
);

create table event_media (
	id serial primary key,
	event_id int not null references events(id) on delete cascade,
	media_type text not null check(media_type in ('VIDEO', 'IMAGE')),
	url text not null,
	created_at timestamptz not null default now()
);

create table event_shifts (
    id serial primary key,
    event_id int not null references events(id) on delete cascade,
    name text not null,
    starts_at timestamptz not null,
    ends_at timestamptz not null,
    capacity int not null,
    check (ends_at > starts_at),
    check (capacity >= 0)
);

create table applications (
    id serial primary key,
    event_id int not null references events(id) on delete cascade,
    shift_id int references event_shifts(id) on delete set null,
    volunteer_id int not null references users(id) on delete cascade,
    status application_status not null default 'pending',
    cover_message text,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    unique(event_id, volunteer_id)
);

create table conversations (
    id serial primary key,
    is_group boolean not null default false,
    created_by int references users(id) on delete set null,
    created_at timestamptz not null default now()
);

create table conversation_participants (
    conversation_id int references conversations(id) on delete cascade,
    user_id int references users(id) on delete cascade,
    joined_at timestamptz not null default now(),
    primary key(conversation_id, user_id)
);

create table messages (
    id serial primary key,
    conversation_id int not null references conversations(id) on delete cascade,
    sender_id int not null references users(id) on delete cascade,
    body text,
    sent_at timestamptz not null default now()
);