use OrlikDB;

create table rating (
    rating_id integer not null auto_increment primary key,
    pitch_id integer not null,
    user_id varchar(255) not null,
    value smallint check ( value >=1 AND value <=5 ),
    foreign key (pitch_id) references pitches(pitch_id),
    foreign key (user_id) references users(user_login)
);