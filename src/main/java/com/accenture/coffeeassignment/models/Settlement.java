package com.accenture.coffeeassignment.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model object representing amount paid and owed by each user
 */
@Data
@AllArgsConstructor
public class Settlement {

  private Double amountPaid;
  private Double amountOwed;
}
