package com.accenture.coffeeassignment;


import com.accenture.coffeeassignment.exception.CoffeePaymentManagerException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Entry point of the coffee-settlement program
 */
public class MainApp {

  public static void main(String[] args) throws CoffeePaymentManagerException {
    Scanner sc = new Scanner(System.in);
    System.out.println("################## Coffee Settlement App ##################");
    System.out.println(
        "Please provide absolute path to the payments.json file: ");
    String paymentsJsonFilePath = sc.nextLine();
    System.out.println(
        "Please provide absolute path to the products.json file: ");
    String productsJsonFilePath = sc.nextLine();
    System.out.println(
        "Please provide absolute path to the orders.json file: ");
    String orderJsonFilePath = sc.nextLine();

    CoffeePaymentManager cpm = new CoffeePaymentManager();
    System.out.println(cpm.evaluateAmountPaidAndOwedPerUser(paymentsJsonFilePath,productsJsonFilePath,orderJsonFilePath));
  }

}
