DROP DATABASE IF EXISTS FindYourDisneyRestaurant;
CREATE DATABASE FindYourDisneyRestaurant;
USE FindYourDisneyRestaurant;

CREATE TABLE IF NOT EXISTS Parks (
    Park_Name VARCHAR(200) PRIMARY KEY,
    description VARCHAR(200)
);
create table if not exists restaurants(
restaurant_name  VARCHAR(200) PRIMARY KEY,
description varchar(200), 
ischaracterdining boolean,
hours datetime,
isallyoucaneat boolean,
Park Varchar(200), 
typeoffood varcharacter(200),
foreign key (park) references Parks.Park_Name on update cascade on delete cascade
);
create table if not exists menuitem(
menuitem  VARCHAR(200) PRIMARY KEY, 
restaurant_name Varchar(200), 
foreign key (restaurant_name) references restaurnats.restaurant_name on update cascade on delete cascade
);
create table if not exists needs_reservations(
Restaurant_name  VARCHAR(200) PRIMARY KEY,
HowHardisItToGet1to10 integer,
foreign key (restaurant_name) references restaurnats.restaurant_name on update cascade on delete cascade 
);

create table if not exists quickservice(
Restaurant_name  VARCHAR(200) PRIMARY KEY,
has_seating boolean, 
startingprice integer,
foreign key (restaurant_name) references restaurnats.restaurant_name on update cascade on delete cascade );




