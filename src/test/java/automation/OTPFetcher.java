package automation;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPFetcher {

    // Method to fetch the OTP from SMS
    public static String getOTP() {
        String otp = null;
        try {
            // Use the full path to adb executable
            String adbPath = "C:\\Users\\LOGESH\\Downloads\\platform-tools-latest-windows\\platform-tools\\adb";  // Update with your path
            ProcessBuilder processBuilder = new ProcessBuilder(adbPath, "shell", "content", "query", "--uri", "content://sms/inbox", "--projection", "body");
            Process process = processBuilder.start();
            
            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Check for a keyword like "Amazon" in the SMS content
                if (line.contains("Flipkart")) {
                    // Use regex to extract the OTP (assuming a 6-digit OTP)
                    Pattern pattern = Pattern.compile("(\\d{6})");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        otp = matcher.group(1); // Get the OTP
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return otp;
    }

    public static void main(String[] args) {
        String otp = getOTP();
        if (otp != null) {
            System.out.println("OTP: " + otp);
        } else {
            System.out.println("OTP not found.");
        }
    }
}
