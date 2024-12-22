# ECommerceAutomation

This project automates functionality testing of the Amazon India website using Selenium WebDriver with Java. It includes tests for login, product search, adding items to the cart, and checking for broken links on the homepage. Additionally, the project provides functionality to fetch OTP from a mobile device connected via USB using **ADB** (Android Debug Bridge) for scenarios that require OTP validation.

## Project Structure

- **ECommerceAutomation.java**: Contains automated test scripts for Amazon India website functionalities (login, product search, add to cart, broken links).
- **OTPFetcher.java**: Fetches OTP from a connected mobile device using ADB. This class is used to fetch OTP and can be extended for testing login through OTP.
- **TestNG**: Used for running the tests and generating reports.

## Features

- **Login Test**: Validates login functionality using either regular credentials or OTP fetched from a mobile device.
- **Product Search and Add to Cart**: Automates the search for a product (e.g., iPhone 14) and adds it to the cart.
- **Broken Link Detection**: Identifies broken links on the homepage and logs the response status.
- **OTP Fetcher**: Fetches OTP from a mobile device connected via USB using ADB.

## Prerequisites

1. **Java** (latest version recommended) installed.
2. **Maven** for project dependency management.
3. **Selenium WebDriver** and **WebDriverManager** for handling browser drivers.
4. **ADB** (Android Debug Bridge) for fetching OTP from the mobile device.
5. **IDE** (like Eclipse or IntelliJ IDEA) for editing and running the code.
6. **TestNG** for running the test cases and generating reports.

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone <repository_url>
   ```

2. Navigate to the project directory:
   ```bash
   cd ECommerceAutomation
   ```

3. Ensure all dependencies are downloaded by running:
   ```bash
   mvn clean install
   ```

4. For OTP fetching, ensure that your mobile device is connected via USB and **ADB** is set up and running.

5. Set up the required environment variables for **Amazon credentials**:
   - `AMAZON_USERNAME`
   - `AMAZON_PASSWORD`

## Test Cases

### 1. Login Test
- **Description**: Tests the login functionality by navigating to the login page, entering valid credentials (or OTP if extended), and verifying the login success.

### 2. Product Search and Add to Cart
- **Description**: Searches for a specific product (e.g., iPhone 14) and adds it to the cart, taking a screenshot after successful addition.

### 3. Broken Link Finder
- **Description**: Detects broken links on the homepage by checking the response codes for all links.

### 4. OTP Fetcher
- **Description**: Uses ADB to fetch OTP from a mobile device connected via USB. This can be used for testing login scenarios that require OTP validation.

## Using OTP Fetcher in Login Test

If you want to use OTP fetching as part of your login test, follow these steps:

1. **Extend the OTPFetcher class** in your `ECommerceAutomation` test class:
   ```java
   public class ECommerceAutomation extends OTPFetcher {
       // Your existing test methods
   }
   ```

2. **Use the OTP** in your login flow. For example:
   ```java
   @Test(priority = 1)
   @Description("Test Amazon login functionality with OTP.")
   public void loginTestWithOTP() {
       String otp = fetchOTP(); // Fetch OTP using the OTPFetcher class
       loginWithOTP(otp); // Implement login method that takes OTP
   }

   @Step("Login using OTP: {otp}.")
   public void loginWithOTP(String otp) {
       // Implement logic to enter OTP and complete login process
   }
   ```

## Running the Tests

- To run the tests with Maven:
   ```bash
   mvn clean test
   ```

- To generate the Allure report after test execution:
   ```bash
   mvn allure:serve
   ```

## Future Enhancements

- **ITestResult Analyzer**: We plan to integrate an ITestResult analyzer to provide detailed insights on test failures.
- **Extend OTP Features**: We can extend the OTP feature to handle OTP validation across different platforms or scenarios.

## Conclusion

This project automates essential functionalities of the Amazon India website and also provides a way to handle OTP-based logins by fetching OTP from a mobile device using ADB. It offers the flexibility to test multiple login mechanisms (password-based and OTP-based) for a more comprehensive testing suite.
