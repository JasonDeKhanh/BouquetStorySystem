# BouquetStorySystem

BouquetStory invites you to show your own creativity by creating your own bouquets to give to your loved ones! Where most flower shops only allow you to choose from pre-made bouquets, BouquetStory lets you choose from a wide variety of fresh flowers, containers, and decorations to create your very own custom flower bundles. The unparalleled flexibility and convenience makes it possible for you to find the ideal gift for any occasion!

[Project Showcase](https://uvents.nus.edu.sg/event/20th-steps/module/IS3106/project/18)

[Project Demo - Admin Portal and Customer Portal Walkthrough](https://studio.youtube.com/video/EJkYN0Hd5ZQ/edit)

## Description

Bouquet Story is a multi-tier enterprise system that consists of an admin management functionalities as well as a customer e-commerce website for a flower shop.

## Tech Stack

The system comprises of 2 frontend applications and 1 common backend:

### Frontend

BouquetStory Customer E-Commerce website: Web application developed using Angular 13 and PrimeNG. [[Link]](https://github.com/JasonDeKhanh/BouquetStoryAngular)

BouquetStory Admin Management Portal: Web application developed using JavaServer Faces and PrimeFaces. Reports are generated using JasperReport.
[[Link]](https://github.com/JasonDeKhanh/BouquetStorySystem)

### Backend

BouquetStory Backend: Common backend developed using Java Enterprise Edition 8, JPQL and MySQL database. [[Link]](https://github.com/JasonDeKhanh/BouquetStorySystem/tree/main/BouquetStorySystem-ejb)

The common backend has a component-based architecture and a service-oriented architecture. Data storage utilises JPQL that is used in conjuction with MySQL for object relational mapping to store and read data from the database. RESTful Web Services are implemented to allow the angular app to call the backend.

The common backend incorporates both a component-based architecture and a service-oriented architecture. For data storage, MySQL is used and JPQL is the main query language used to insert and retrieve data from the database. To facilitate communication between the Angular application and the backend, RESTful Web Services have been implemented.

## Screenshots

 <img src="https://i.imgur.com/ae6Gkks.png" height="400" alt="E-commerce website home page"/>
 
 <img src="https://i.imgur.com/ZCcreS1.png" height="400" alt="E-commerce website shopping cart"/>
  
 <img src="https://i.imgur.com/9r09tOM.png" height="400" alt="Admin Home Page"/>
  
 <img src="https://i.imgur.com/7vxUlBx.png" height="800" alt="Poster"/>
