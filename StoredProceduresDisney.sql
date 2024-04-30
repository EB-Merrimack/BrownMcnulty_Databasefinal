USE FindYourDisneyRestaurant;

DELIMITER //

CREATE PROCEDURE InsertNewRestaurants(
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

CREATE PROCEDURE InsertNewRestaurantsfull(
    IN restaurantName VARCHAR(200),
    IN restaurantDescription VARCHAR(200), 
    IN isCharacterDining BOOLEAN,
    IN openingHours time,
    IN closingHours time,
    IN is_all_you_can_eat BOOLEAN,
    IN park VARCHAR(200), 
    IN typeOfFood VARCHAR(200)
    IN priceRange(5)
)
BEGIN
    -- Inserting data into the restaurants table
    INSERT INTO restaurants (restaurantName, restaurantDescription, isCharacterDining, openingHours, closingHours, isAllYouCanEat, park, typeOfFood, priceRange)
    VALUES (restaurantName, restaurantDescription, isCharacterDining, openingHours, closingHours, isAllYouCanEat, park, typeOfFood, priceRange);
END //

DELIMITER ;
