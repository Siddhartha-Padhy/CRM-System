# CRM System

A Spring Boot-based Customer Relationship Management (CRM) System that efficiently stores sales data, tracks customer interactions, and provides insightful analytics. This project is built around two main entities, **Sale** and **CustomerLog**, to streamline sales tracking and customer interaction management for a better understanding of sales performance and customer engagement.

## Features

- **Sales Data Management**: Easily create, read, update, and delete sales data.
- **Customer Interaction Logs**: Record customer interactions related to each sale for effective tracking and follow-ups.
- **Analytics**: Get insights into sales trends, customer behavior and revenue aspects to make data-driven decisions.

## Entities and their relation
- The **Sale** entity has attributes namely *Id, Product name, Sale stage, Deal size, Closing date*. The Sale stage can consist of any stage from *Lead, Contact Mande, Qualification, Demo Scheduled, Closed*.
- The **Customer Log** entity has attributes namely *Id, Customer name, Interaction type, Interaction notes, Interaction date*.
- **Customer Log** and **Sale** have a many-to-one relation where more than one customer logs are associated with a single sale.

## Testing and Logging
- Junit and Mockito frameworks have been used to cover all test cases including the edge cases.
- Logging have been performed using slf4j.

## Getting Started

### Prerequisites

- Java 11 or later
- Maven 3.6+
- MySQL (or a compatible database)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/CRM-System.git
2. Navigate to the project directory:
   ```bash
   cd CRM-System
3. Configure database credentials in **src/main/resources/application.properties**
4. Build the project with Maven:
   ```bash
   mvn clean install
5. Run the application:
   ```bash
   mvn spring-boot:run

# API Endpoints
### Sale Management
- `POST /api/sales/addSale` - Create a new sale
- `GET /api/sales/sales/{id}` - Retrieve sale by id
- `GET /api/sales/allSales` - Retrive all sales
- `PUT /api/sales/update/{id}` - Update sale with id
- `GET /api/sales/salesStageCount` - Count sales in each stage
- `DELETE /api/sales/delete/{id}` - Delete sale by id
- `GET /api/sales/totalDealSizeForClosedSales` - Find total deal size for closed sales
- `GET /api/sales/totalDealSizeForUnclosedSales` - Find total deal size for unclosed sales

### Customer Logs
- `POST /api/customerLogs/addLog` - Add customer log
- `GET /api/customerLogs/sale/{saleId}` - Retrieve customer logs for a sale
- `GET /api/customerLogs/countByCustomer` - Count customer logs for each customer
- `GET /api/customerLogs/{id}` - Retrieve customer log using the log id


> Enjoy tracking and managing your sales and customer relationships with the CRM System!
