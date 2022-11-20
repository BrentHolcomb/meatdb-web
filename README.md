# Meat Database

## Inspiration

This database project was inspired by the transactions between Chick-Fil-A and the companies that supply their chicken. As a Chick-Fil-A employee, I know that Tyson and other brands sell pre-marinated chicken to Chick-Fil-A. These meats are then cooked and served to customers. I felt that this process could be an excellent opportunity to create an API that does the work regarding tracking the costs, earnings, and waste. The transaction process is made more complicated because chicken is purchased in different types of marinade, such as: **regular**, **spicy**, and **grilled**. All of this needs to be tracked so Chick-Fil-A can adjust their food prices and be sure not to over-buy.


## The Approach

#### A Spring API with an H2 CRUD repository using Java as the backend and Thymeleaf for the UI

This is a deployed **Spring API** project that returns filtered database entries to the user and offers several ways to upload and edit entries.

This project uses the **Three-Tier Architecture** model to appropriately organize code and keep it easy to adjust and build upon for additional user interfaces. The code has been split into 3 packages: **data**, **biz**, and **web**.

+ **Data** sends CRUD requests to the repository to retrieve and input entries.
+ **Biz** is for the application's logic, such as the model classes of the data and service classes for controllers.
+ **Web** has controllers that communicate with the frontend and implement formatters.

I used **JUnit** for **Test Driven Development** to test and create formatters. These formatters ensure that entries are translated correctly between the frontend and backend.


## Deployed Links

The [deployed project](https://salty-mesa-20902.herokuapp.com/supply) can be viewed at:<br />
https://salty-mesa-20902.herokuapp.com/supply

In case the database appears empty, please follow [this link](https://salty-mesa-20902.herokuapp.com/update) to generate testing entries:<br />
https://salty-mesa-20902.herokuapp.com/update
