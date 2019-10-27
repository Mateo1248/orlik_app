/* script to log in into db
   mysql -h dborlik.cjmn080aizuq.us-east-2.rds.amazonaws.com -u orlikAdmin -p
   password is known in group structure
 */

create database OrlikDB;
use OrlikDB;


create table users (
	user_login varchar(50) not null primary key,
	user_password varchar(255) not null);

create table pitches (
	pitch_id int auto_increment not null primary key,
	pitch_name varchar(70) not null,
	latitude decimal not null,
	longitude decimal not null);


create table reservations (
	reservation_id int auto_increment not null primary key,
	which_user varchar(50) not null,
    which_pitch int not null,
    reservation_date date not null,
    start_hour time not null,
    end_hour time not null,
   	foreign key(which_user) references users(user_login),
   	foreign key(which_pitch) references pitches(pitch_id));



/* validation triggers was created, but two-level validation in mobile app and java server is enough to care about data consistency */


/*delimiter $$
drop trigger if exists playground_validation$$
create trigger playground_validation
	before insert on playgrounds
    for each row
    begin
		if(new.latitude_1st<0 or new.latitude_1st>180) then
			signal sqlstate '40000'
			set message_text = 'Latitude first coordinate should be value from [0, 180]';
		end if;
        if(new.longitude_1st<0 or new.longitude_1st>180) then
			signal sqlstate '40000'
			set message_text = 'Longitude first coordinate should be value from [0, 180]';
		end if;
        if(new.latitude_2st<0) then
			signal sqlstate '40000'
			set message_text = 'Latitude second coordinate should be value greater than 0';
		end if;
        if(new.longitude_2st<0) then
			signal sqlstate '40000'
			set message_text = 'Longitude second coordinate should be value greater than 0';
		end if;
	end;$$
delimiter ;


delimiter $$
drop trigger if exists reservation_validation $$
create trigger reservation_validation
	before insert on reservations
    for each row
    begin
		if(new.reservation_date<curdate()) then
			signal sqlstate '40000'
			set message_text = 'Reservation may be done only in future, not in the past';
		end if;
	end;$$
delimiter ;*/








/* validation triggers was created, but two-level validation in mobile app and java server is enough to care about data consistency */


/*delimiter $$
drop trigger if exists playground_validation$$
create trigger playground_validation
	before insert on playgrounds
    for each row
    begin
		if(new.latitude_1st<0 or new.latitude_1st>180) then
			signal sqlstate '40000'
			set message_text = 'Latitude first coordinate should be value from [0, 180]';
		end if;
        if(new.longitude_1st<0 or new.longitude_1st>180) then
			signal sqlstate '40000'
			set message_text = 'Longitude first coordinate should be value from [0, 180]';
		end if;
        if(new.latitude_2st<0) then
			signal sqlstate '40000'
			set message_text = 'Latitude second coordinate should be value greater than 0';
		end if;
        if(new.longitude_2st<0) then
			signal sqlstate '40000'
			set message_text = 'Longitude second coordinate should be value greater than 0';
		end if;
	end;$$
delimiter ;


delimiter $$
drop trigger if exists reservation_validation $$
create trigger reservation_validation
	before insert on reservations
    for each row
    begin
		if(new.reservation_date<curdate()) then
			signal sqlstate '40000'
			set message_text = 'Reservation may be done only in future, not in the past';
		end if;
	end;$$
delimiter ;*/





