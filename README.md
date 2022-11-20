# Meat Database

## Inspiration

This database project was inspired by the transactions between Chick-Fil-A and the companies that supply their chicken. As a Chick-Fil-A employee I know that Tyson and other brands sell chicken to Chick-Fil-A that are pre-marinated. These meats are then cooked and served to customers. I felt that this process could be a great opportunity to create an API that does the work when it comes to tracking the costs, earnings, and waste. This is made more complicated by the fact that chicken is purchased in different types of marinade: mainly **regular**, **spicy**, and **grilled**. All of this needs to be tracked so Chick-Fil-A can adjust the prices of their food and be sure to not over-buy.


## The Approach

#### A Spring API with an H2 CRUD repository using Java as the backend and Thymeleaf for the UI

This is a deployed **Spring API** project that returns filtered database entries to the user and offers several ways to upload and edit entries.

This project uses the **Three-Tier Architecture** model to appropriately organize code and keep it easy to adjust and build upon for additional user interfaces. The code has been split into 3 packages: **data**, **biz**, and **web**.

+ **Data** sends CRUD requests to the repositiry to retrieve and input entries.
+ **Biz** is for the logic of the application, such as the model classes of the data and service classes for controllers.
+ **Web** has the controllers that communicate with the front-end and implements formatters.

I used **JUnit** for **Test Driven Development** to test and create formatters. These formatters ensure that entries are translated correctly between the front-end and back-end.


## Deployed Links

The [deployed project](https://salty-mesa-20902.herokuapp.com/supply) can be viewed at:<br />
https://salty-mesa-20902.herokuapp.com/supply

Incase the database appears empty, please follow [this link](https://salty-mesa-20902.herokuapp.com/update) to generate testing entries:<br />
https://salty-mesa-20902.herokuapp.com/update
