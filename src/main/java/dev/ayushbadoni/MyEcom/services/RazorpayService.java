package dev.ayushbadoni.MyEcom.services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class RazorpayService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    public Map<String, String> createRazorpayOrder(Double amount) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", (int)(amount * 100)); // Razorpay needs amount in paise
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());
        options.put("payment_capture", 1);

        Order order = client.orders.create(options);

        Map<String, String> response = new HashMap<>();
        response.put("razorpayOrderId", order.get("id"));
        response.put("currency", order.get("currency"));
        response.put("amount", order.get("amount").toString());
        return response;
    }

    public static boolean verifySignature(String orderId, String paymentId, String actualSignature, String secret) {
        try {
            String data = orderId + "|" + paymentId;

            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256HMAC.init(secretKey);

            byte[] hash = sha256HMAC.doFinal(data.getBytes());
            String expectedSignature = Base64.getEncoder().encodeToString(hash);

            return expectedSignature.equals(actualSignature);
        } catch (Exception e) {
            return false;
        }
    }

}
