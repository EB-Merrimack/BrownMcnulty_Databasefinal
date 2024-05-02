USE FindYourDisneyRestaurant;

DELIMITER //
drop procedure if exists InsertNewRestaurants;
CREATE PROCEDURE  InsertNewRestaurants(
   IN restaurantName VARCHAR(200),
   OUT need_additional_details BOOLEAN
)
BEGIN 
    DECLARE restaurant_count INT;
    
    -- Check if the restaurant already exists
    SELECT COUNT(*) INTO restaurant_count FROM restaurants WHERE restaurantName = restaurantName;
    
    -- If restaurant does not exist, set flag to true
    IF restaurant_count = 0 THEN
        SET need_additional_details := TRUE;
    ELSE
        -- If restaurant exists, set flag to false
        SET need_additional_details := FALSE;
    END IF;
END //
drop procedure if exists InsertNewRestaurantsfull;
CREATE PROCEDURE InsertNewRestaurantsfull(
    IN restaurantName VARCHAR(200),
    IN restaurantDescription VARCHAR(200), 
    IN isCharacterDining BOOLEAN,
    IN openingHours time,
    IN closingHours time,
    IN isAllYouCanEat BOOLEAN,
    IN park VARCHAR(200), 
    IN typeOfFood VARCHAR(200),
    IN priceRange varchar(5)
)
BEGIN
    -- Inserting data into the restaurants table
    INSERT INTO restaurants (restaurantName, restaurantDescription, isCharacterDining, openingHours, closingHours, isAllYouCanEat, park, typeOfFood, priceRange)
    VALUES (restaurantName, restaurantDescription, isCharacterDining, openingHours, closingHours, isAllYouCanEat, park, typeOfFood, priceRange);
END //

DELIMITER ;


DELIMITER //

DROP PROCEDURE IF EXISTS FindRestaurantsByParkName;

CREATE PROCEDURE FindRestaurantsByParkName(IN parkName VARCHAR(200))
BEGIN
    SELECT restaurantName 
    FROM Restaurants 
    WHERE park = parkName;
END//

DELIMITER ;


DELIMITER //
drop procedure if exists FindRestaurantsByServiceType;
CREATE PROCEDURE FindRestaurantsByServiceType(IN serviceType VARCHAR(50))
BEGIN
    IF serviceType = 'Quick Service' THEN
        SELECT r.restaurantName 
        FROM findyourdisneyrestaurant.restaurants r
        INNER JOIN QuickService qs ON r.restaurantName = qs.restaurantName;
    ELSE
        SELECT r.restaurantName 
        FROM Restaurants r
        INNER JOIN NeedsReservations nr ON r.restaurantName = nr.restaurantName;
    END IF;
END//

DELIMITER ;
