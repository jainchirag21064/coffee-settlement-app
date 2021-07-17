package com.accenture.coffeeassignment.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;

/**
 * Model for Products representing its name and various size and respective price
 */
@Data
public class Product {

  @JsonProperty("drink_name")
  private String drinkName;
  private Map<String, Double> prices;

}
