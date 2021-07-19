package com.accenture.coffeeassignment.models;

import lombok.Data;

/**
 * Model for Payments representing user and amount they paid
 */
@Data
public class Payment {

  private String user;
  private Double amount;

}
