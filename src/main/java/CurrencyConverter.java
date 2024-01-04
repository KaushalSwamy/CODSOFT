import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

    public class CurrencyConverter {
        private static final String API_KEY = "https://openexchangerates.org/api/latest.json?app_id=ed3b4a27c8124dec996dd989b8871fc5"; // Replace with your actual API key
        private static final String BASE_URL = "https://open.er-api.com/v6/latest/";

        public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the base currency code (e.g., USD): ");
            String baseCurrency = scanner.next().toUpperCase();

            System.out.print("Enter the target currency code (e.g., EUR): ");
            String targetCurrency = scanner.next().toUpperCase();


            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);
            if (exchangeRate == -1) {
                System.out.println("Error fetching exchange rates. Exiting.");
                return;
            }

            System.out.print("Enter the amount to convert: ");
            double amount = scanner.nextDouble();
            double convertedAmount = amount * exchangeRate;
            System.out.printf("%.2f %s is equal to %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
            scanner.close();
        }

        private static double getExchangeRate(String baseCurrency, String targetCurrency) {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + baseCurrency + "?apikey=" + API_KEY))
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JSONObject jsonResponse = new JSONObject(response.body());
                    return jsonResponse.getJSONObject("rates").getDouble(targetCurrency);
                } else {
                    System.out.println("Error: " + response.statusCode());
                    System.out.println(response.body());
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            return -1;
        }
    }


