# Disney Restaurant Database

## Initialization
To initialize the Disney Restaurant Database, follow these steps:
1. Run the `tablecreation_disney.sql` script to create the necessary tables.
2. Run the `StoredProceduresDisney.sql` script to create the stored procedures.
3. Utilize the import wizard to import each CSV file into the appropriate connections.
4. Finally, run the `main` method of `introtopresentation.java` to start using the application.



## Welcome to the Disney Restaurant’s Database! 
Having trouble finding your top Disney restaurant for your next trip? With this database you can search by the four main parks( Magic Kingdom, Animal Kingdom, Hollywood Studios, Epcot) or the service type. After which you have the option of adding any of these results into your exportable favorite list! You also can add that new restaurant you’ve had your eye on!- Even if its not in the database yet through our add restaurant option! Not sure if we have the restaurant you want? Feel free to try adding it, and if it's already there the information of the restaurant will come up. You know you want a classic restaurant but forget some of the details? We've got you covered, too just search restaurant details, and we've got you covered. Just be sure after you’re done to go back into your favorite dashboard and all the restaurants you want to be exported in our easy-to-use favorite export method exporting the name of your go-to restaurants into a neat text file. Once you are done please close out the database connection and remember if you need a reminder on any of the specifics of those restaurants we are here to help! 

## Note For Adding Restaurants

It should be noted that all Disney restaurant names are unique and thus every attribute is assigned to that specific restaurant name. Thus if you are trying to reference generic restaurants like Starbucks or Rainforest Cafe, please be sure to follow the official Disney naming and add “at animal kingdom” or “at disney springs” to the name. 




## Technical Explanation

This application is designed to help a user view their options for different Disney World
restaurants based on specific requirements that they choose. When the application is first
opened, a user is given 8 options: Add restaurant to database, search by park, search by
service type, export favorites, view favorites, close database connections, search by restaurant
details, and exit. After they have selected their choice, they are prompted to either give input or
they close out of the program. If they choose to add a new restaurant, they provide the
restaurant name and if that one doesn’t already exist, they can add in the additional details of
that restaurant and then it is added to the database. If they choose to search by park, the user
types in the name of the park they want to find a restaurant within (Magic Kingdom, Epcot,
Hollywood Studios, Animal Kingdom, null), and then a list of restaurants within that specified
park. If they choose to search by service type, they can either choose to search for quick
services or restaurants that require reservations, and then, based on their input, the restaurants
that meet the requirements are listed. After these lists of restaurants are generated, the user
has the option to save the restaurant to their favorites list. If they choose to save any, they can
export their favorites to a file and save them. The fifth option allows the user to view their saved
favorites. Next, they have the option to close the database connections. If they search
for restaurant details, they put in the name of the restaurant they want the details for, and then a list with all of the information is returned to the user. Finally, they can exit the
application, which closes everything. This is designed to be user-friendly and also give the user
multiple different options that would actually be useful when planning a trip to Disney.

## Technical Functional Dependencies

### Parks Table:
- The park's name uniquely determines its description.

### Restaurants Table:
- The restaurant name uniquely determines all other attributes about that restaurant.

### MenuItem Table:
- Each menu item is unique and belongs to a specific restaurant.

### needs_reservations Table:
- The restaurant name uniquely determines the difficulty of getting reservations at that restaurant.

### QuickService Table:
- The restaurant name uniquely determines whether it has seating and the starting price for meals.

