

USE FindYourDisneyRestaurant;

CREATE TABLE IF NOT EXISTS Parks (
    parkName VARCHAR(200) PRIMARY KEY,
    parkDescription text
);

create table if not exists Restaurants(
restaurantName  VARCHAR(200) PRIMARY KEY,
restaurantDescription text, 
isCharacterDining boolean,
openingHours time,
closingHours time,
isAllYouCanEat boolean,
park Varchar(200), 
typeOfFood varchar(200),
priceRange varchar(5),
foreign key (park) references Parks(parkName) on update cascade on delete cascade
);

create table if not exists MenuItem(
menuItem  VARCHAR(200) PRIMARY KEY, 
restaurantName Varchar(200), 
foreign key (restaurantName) references restaurants(restaurantName) on update cascade on delete cascade
);

create table if not exists needs_reservations(
restaurantName  VARCHAR(200) PRIMARY KEY,
 HowHardisItToGet1to10 INTEGER,
foreign key (restaurantName) references restaurants(restaurantName) on update cascade on delete cascade );

create table if not exists QuickService(
restaurantName  VARCHAR(200) PRIMARY KEY,
hasSeating boolean, 
startingprice integer,
foreign key (restaurantName) references restaurants(restaurantName) on update cascade on delete cascade );
