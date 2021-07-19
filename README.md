# Coffee-Settlement App

## Description
The application accepts 3 JSON files. The files contain data for products, orders and payments in products.json,orders.json and payments.json files respectively.

The application then retrieves the following information given the provided files:
- Amount paid per user.
- Amount that each users still owes.

## Requirements
- The program should be written in Java.
- Write production-ready code.
- Document how to run the application.
- Publish the source code into GitHub (using your own personal account) and share it with us.

## Approach
1. Program first reads respective JSON file and map it to respective collection.
```
E.g. 
payments.json is mapped to List<Payment> representing each element of payment json
products.json is mapped to List<Product> representing each element of product json
orders.json is mapped to List<Order> representing each element of product json
```
2. In order to calculate total amount paid by each user we process the collection of List<Payment. 
   <br>Since same user can pay multiple times thus we the Collectors Grouping clause which groups the list based on unique user as key and value as sum of the amount paid by them.
    <br>The result in a MAP<String,Double> where key is Unique User and Value is the sum of amount paind by them.

3. In order to calculate total amount owed by each user we first need to calculate the total amount billed/charged to each user which can be obtained by processing the collection of List<Order> and List<Product>.
   <br>Once we know how much each used is billed we can use the result from step 2 and subtract the amount paid from amount billed, and the result will be the amount owed by each user 
  
4. We have mapped all the data in MAP<String,Settlement> where key is each user and Settlement object represent the amount paid and amount owed by that user.

## HOW-TO Run the Application
> Windows
```bash
git clone https://github.com/jainchirag21064/coffee-assesment.git
cd coffee-assesment
mvnw.cmd clean install
java -jar target\coffee-assesment-1.0-SNAPSHOT-jar-with-dependencies.jar
```
> Linux/Mac/Git Bash
```shell
git clone https://github.com/jainchirag21064/coffee-assesment.git
cd coffee-assesment
./mvnw clean install
java -jar target/coffee-assesment-1.0-SNAPSHOT-jar-with-dependencies.jar