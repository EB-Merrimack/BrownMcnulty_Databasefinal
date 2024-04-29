USE FindYourDisneyRestaurant;

DELIMITER //

CREATE PROCEDURE InsertNewRestaurants(
   IN restaurant_name VARCHAR(200),
   OUT need_additional_details BOOLEAN
)
BEGIN 
    DECLARE restaurant_count INT;
    
    -- Check if the restaurant already exists
    SELECT COUNT(*) INTO restaurant_count FROM restaurants WHERE restaurant_name = restaurant_name;
    
    -- If restaurant does not exist, set flag to true
    IF restaurant_count = 0 THEN
        SET need_additional_details := TRUE;
    ELSE
        -- If restaurant exists, set flag to false
        SET need_additional_details := FALSE;
    END IF;
END //

CREATE PROCEDURE InsertNewRestaurantsfull(
    IN restaurant_name VARCHAR(200),
    IN description VARCHAR(200), 
    IN is_character_dining BOOLEAN,
    IN hours DATETIME,
    IN is_all_you_can_eat BOOLEAN,
    IN park VARCHAR(200), 
    IN type_of_food VARCHAR(200)
)
BEGIN
    -- Inserting data into the restaurants table
    INSERT INTO restaurants (restaurant_name, description, is_character_dining, hours, is_all_you_can_eat, park, type_of_food)
    VALUES (restaurant_name, description, is_character_dining, hours, is_all_you_can_eat, park, type_of_food);
END //

DELIMITER ;
