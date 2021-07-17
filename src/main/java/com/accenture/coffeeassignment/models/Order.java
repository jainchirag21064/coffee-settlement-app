package com.accenture.coffeeassignment.models;

import lombok.Data;

/**
 * Model for Order representing user and drink of which they placed order and respective Size
 */
@Data
public class Order {

  private String user;
  private String drink;
  private String size;

}
