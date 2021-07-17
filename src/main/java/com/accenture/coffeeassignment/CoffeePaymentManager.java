package com.accenture.coffeeassignment;

import com.accenture.coffeeassignment.exception.CoffeePaymentManagerException;
import com.accenture.coffeeassignment.models.Order;
import com.accenture.coffeeassignment.models.Payments;
import com.accenture.coffeeassignment.models.Product;
import com.accenture.coffeeassignment.models.Settlement;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Processor class with the logic of processing the Json file
 * and identifying the amount paid and owed by each user
 */
public class CoffeePaymentManager {

  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Method which calls different helper method to process the JSON files and evaluate amount paid and owed  by each user in a settlement object mapped to user as key
   *
   * @param paymentsJsonFilePath - Absolute Path of Payments Json File
   * @param productsJsonFilePath - Absolute Path of Products Json File
   * @param orderJsonFilePath    - Absolute Path of Order Json File
   * @return settlementOfUsers - Map with key as user and value as settlement object representing amount paid and owed byt each user
   * @throws IOException - IOException in case issue reading file
   */
  public Map<String, Settlement> evaluateAmountPaidAndOwedPerUser(String paymentsJsonFilePath,
      String productsJsonFilePath, String orderJsonFilePath) throws IOException {

    List<Payments> amountPaidByUsers = paymentsJsonToObjectMapper(paymentsJsonFilePath);
    List<Product> products = productsJsonToObjectMapper(productsJsonFilePath);
    List<Order> orders = ordersJsonToObjectMapper(orderJsonFilePath);

    Map<String, Double> totalAmountPaidPerUser = calculateTotalAmountPaidPerUser(amountPaidByUsers);
    Map<String, Double> totalAmountBilledPerUser = calculateTotalAmountBilledPerUser(orders,
        products);

    return calculateOverallSettlementPerUser(
        totalAmountPaidPerUser, totalAmountBilledPerUser);

  }

  /**
   * This helper method calculates Amount paid and Amount owed per user in settlement object and map it in collection with key as user
   *
   * @param totalAmountPaidPerUser   - Total amount paid per user.
   * @param totalAmountBilledPerUser - Total amount billed or supposed to pay per user.
   * @return overallSettlementPerUsers - Overall Settlement per user describing how much he paid and how much he owe
   */
  private Map<String, Settlement> calculateOverallSettlementPerUser(
      Map<String, Double> totalAmountPaidPerUser, Map<String, Double> totalAmountBilledPerUser) {
    Set<String> keys = new HashSet<>(totalAmountPaidPerUser.keySet());
    keys.addAll(totalAmountBilledPerUser.keySet());

    Map<String, Settlement> overallSettlementPerUsers = new HashMap<>();
    for (String k : keys) {
      overallSettlementPerUsers.put(k, new Settlement(totalAmountPaidPerUser.getOrDefault(k, 0.0),
          totalAmountBilledPerUser.getOrDefault(k, 0.0) - totalAmountPaidPerUser
              .getOrDefault(k, 0.0)));
    }
    return overallSettlementPerUsers;
  }

  /**
   * Helper method to process the file data
   * and initialize it in Collection
   *
   * @param paymentsJsonFilePath - absolute path of the json file with the data that need to be processed
   * @return amountPaidByUsers - List of Payment object representing amount paid by each users
   */
  private List<Payments> paymentsJsonToObjectMapper(String paymentsJsonFilePath) throws IOException {
    return Arrays
        .asList(objectMapper.readValue(Paths.get(paymentsJsonFilePath).toFile(), Payments[].class));

  }

  /**
   * Helper method to process the file data
   * and initialize it in Collection
   *
   * @param productsJsonFilePath - absolute path of the json file with the data that need to be processed
   * @return products - List of Product object representing different coffee products size and its respective price
   */
  private List<Product> productsJsonToObjectMapper(String productsJsonFilePath) throws IOException {
    return Arrays
        .asList(objectMapper.readValue(Paths.get(productsJsonFilePath).toFile(), Product[].class));

  }

  /**
   * Helper method to process the file data
   * and initialize it in Collection
   *
   * @param orderJsonFilePath - absolute path of the json file with the data that need to be processed
   * @return orderPlacedByUser - List of Order placed by all user.
   */
  private List<Order> ordersJsonToObjectMapper(String orderJsonFilePath) throws IOException {
    return Arrays
        .asList(objectMapper.readValue(Paths.get(orderJsonFilePath).toFile(), Order[].class));

  }

  /**
   * helper method to calculate total amount paid by each user
   * and store it in a collection with key as user and
   * value representing total amount paid by him
   *
   * @param amountPaidByUsers - List of User and the amount they paid.Same user might have paid multiple time
   * @return totalAmountPaidByUsers - Total Amount Paid per user where key is Unique user and value is total amount paid by him
   */
  private Map<String, Double> calculateTotalAmountPaidPerUser(List<Payments> amountPaidByUsers) {
    return amountPaidByUsers.stream()
        .collect(Collectors
            .groupingBy(Payments::getUser, Collectors.summingDouble(Payments::getAmount)));

  }

  /**
   * helper method to calculate total amount billed to each user
   * and store it in a collection with key as user and
   * value representing total amount billed to him
   *
   * @param orders   - List of Order placed by all user.
   * @param products - List of Product object representing different coffee products size and its respective price
   * @return totalAmountBilledPerUser - Map of total amount billed to every user.
   */
  private Map<String, Double> calculateTotalAmountBilledPerUser(List<Order> orders,
      List<Product> products) {
    Map<String, Double> totalAmountBilledPerUser = new HashMap<>();

    //Group users with values as the list of orders they placed
    Map<String, List<Order>> orderPlacedByUser = orders.stream()
        .collect(Collectors.groupingBy(Order::getUser));

    orderPlacedByUser.forEach((users, listOfOrders) ->
        listOfOrders.forEach(eachOrder -> {

          //here we are updating the existing user by evaluating the price based on the size and type of coffee and adding it with earlier price
          totalAmountBilledPerUser.computeIfPresent(eachOrder.getUser(),
              (key, val) -> val + products.stream()
                  .filter(product -> eachOrder.getDrink().equalsIgnoreCase(product.getDrinkName()))
                  .findAny()
                  .orElseThrow(() -> new CoffeePaymentManagerException(
                      "Unexpected Product found in Order by user.")).getPrices()
                  .get(eachOrder.getSize()));

          //here we are adding the user for first time in map and with the price based on the size and type of coffee
          totalAmountBilledPerUser.computeIfAbsent(eachOrder.getUser(),
              key -> products.stream()
                  .filter(product -> eachOrder.getDrink().equalsIgnoreCase(product.getDrinkName()))
                  .findAny()
                  .orElseThrow(() -> new CoffeePaymentManagerException(
                      "Unexpected Product found in Order by user.")).getPrices()
                  .get(eachOrder.getSize()));

        })
    );
    return totalAmountBilledPerUser;
  }

}