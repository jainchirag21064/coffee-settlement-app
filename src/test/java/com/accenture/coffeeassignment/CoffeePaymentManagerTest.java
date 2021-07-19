package com.accenture.coffeeassignment;

import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.accenture.coffeeassignment.exception.CoffeePaymentManagerException;
import com.accenture.coffeeassignment.models.Settlement;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test class for CoffeePaymentManager.java where we process to find the amount paid and owed by each user
 */
@RunWith(MockitoJUnitRunner.class)
public class CoffeePaymentManagerTest {

  @InjectMocks
  CoffeePaymentManager underTest;
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void test_EvaluateAmountPaidAndOwedPerUser_Happy() throws IOException {
    Map<String, Settlement> totalAmountPaidPerUserActual = underTest
        .evaluateAmountPaidAndOwedPerUser(getFilePathFromResources("payments.json"),
            getFilePathFromResources("products.json"), getFilePathFromResources("orders.json"));

    assertNotNull("The result if not null ", totalAmountPaidPerUserActual);
    assertFalse("The result of total amount paid is not Empty ",
        totalAmountPaidPerUserActual.isEmpty());
    assertEquals("The result of total amount paid is of expected size ",8,
        totalAmountPaidPerUserActual.size());
  }


  @Test
  public void test_EvaluateAmountPaidAndOwedPerUser_UnExpectedProductPlacedByExistingUserError() throws IOException {
    exceptionRule.expect(CoffeePaymentManagerException.class);
    exceptionRule.expectMessage("Unexpected Product found in Order by user.");
    underTest
        .evaluateAmountPaidAndOwedPerUser(getFilePathFromResources("payments.json"),
            getFilePathFromResources("products.json"), getFilePathFromResources(
                "orders_UnExpectedProductExistingUser.json"));

  }

  @Test
  public void test_EvaluateAmountPaidAndOwedPerUser_UnExpectedProductPlacedByOneUserError() throws IOException {
    exceptionRule.expect(CoffeePaymentManagerException.class);
    exceptionRule.expectMessage("Unexpected Product found in Order by user.");

    underTest
        .evaluateAmountPaidAndOwedPerUser(getFilePathFromResources("payments.json"),
            getFilePathFromResources("products.json"), getFilePathFromResources(
                "orders_UnExpectedProductNewUser.json"));
  }

  /**
   * Helper method to read the file path from resources for unit test purpose
   *
   * @return filePath - absolute path of the file
   */
  String getFilePathFromResources(String fileName) {
    ClassLoader classloader = currentThread().getContextClassLoader();
    return new File(requireNonNull(classloader.getResource(fileName)).getFile())
        .getAbsolutePath();
  }
}
