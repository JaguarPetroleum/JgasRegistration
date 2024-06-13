package com.jaguarpetroleum.JgasRegistration.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@RestController
public class NSIntegration {
	private static final Logger logger = LoggerFactory.getLogger(NSIntegration.class);
	
	@PostMapping("/cashSalePost")
	public JSONObject cashSale() {		
		logger.info("Received a Cash Sale request ");
		
		JSONObject result = new JSONObject();
		String CONSUMER_KEY = "cd665f4b95d80e26c9b1f12907548c7d1c3a9aee93dc53a4c5d68696628b7fd9";
		String CONSUMER_SECRET = "fa0f975744095552fc668fa72387bc4fcfe5a08d0e470e3d3debfe355920c2a8";
		String ACCESS_TOKEN = "14240b24f5c7c9804a8c46348f34e4db707a7a73e6b0ee9793f50df2e596f267";
		String TOKEN_SECRET = "95892b39c709aeefba7e9b2bd5259aef41185ca6a38d381253af4edc45e005fb";
		String REALM = "5262141_SB1";
		String ACCOUNT_ID = "5262141-sb1";
		String BASE_URL = "https://"+ACCOUNT_ID+".suitetalk.api.netsuite.com/services/rest/record/v1/cashSale";
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	long TIMESTAMP = timestamp.getTime();
		
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(BASE_URL);

            // Add authentication headers
            httpPost.addHeader("Authorization", "OAuth realm=\""+REALM+"\", "
            		+ "oauth_consumer_key=\""+CONSUMER_KEY+"\", "
            				+ "oauth_token=\""+CONSUMER_SECRET+"\", "
            						+ "oauth_signature_method=\"HMAC-SHA256\", "
            						+ "oauth_timestamp=\""+TIMESTAMP+"\", "
            								+ "oauth_nonce=\""+TIMESTAMP+"\", "
            										+ "oauth_version=\"1.0\", "
            										+ "oauth_signature=\""+generateSignature("POST", BASE_URL, String.valueOf(TIMESTAMP) , String.valueOf(TIMESTAMP), CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, TOKEN_SECRET)+"\"");

            // Add request body - JSON representing the cash sale
            StringEntity requestBody = new StringEntity("{\r\n"
            		+ "    \"recordType\":\"cashSale\",\r\n"
            		+ "    \"entity\": {\r\n"
            		+ "        \"id\": 2533\r\n"
            		+ "    },\r\n"
            		+ "    \"memo\": \"Cash Sale Demo, Order No RYEEEWBUDW, MPESA Reference TEST_REF4\",\r\n"
            		+ "    \"tranDate\": \""+LocalDate.now()+"\",\r\n"
            		+ "    \"subsidiary\": {\r\n"
            		+ "        \"id\": 1\r\n"
            		+ "    },\r\n"
            		+ "    \"location\": {\r\n"
            		+ "        \"id\": 43\r\n"
            		+ "    },\r\n"
            		+ "    \"class\": {\r\n"
            		+ "        \"id\": 2\r\n"
            		+ "    },\r\n"
            		+ "    \"item\": {\r\n"
            		+ "        \"items\": [\r\n"
            		+ "            {\r\n"
            		+ "                \"item\": {\r\n"
            		+ "                    \"type\": \"inventoryItem\",\r\n"
            		+ "                    \"id\": 304\r\n"
            		+ "                },\r\n"
            		+ "                \"amount\": 2400\r\n"
            		+ "            }\r\n"
            		+ "        ]\r\n"
            		+ "    },\r\n"
            		+ "    \"shipMethod\": 486,\r\n"
            		+ "    \"shippingCost\": 380,\r\n"
            		+ "    \"shippingAddress\": {\r\n"
            		+ "        \"addr1\": \"TEST ADDRESS\"\r\n"
            		+ "    },\r\n"
            		+ "    \"account\":{\r\n"
            		+ "        \"id\": 509\r\n"
            		+ "    },\r\n"
            		+ "    \"paymentMethod\": {\r\n"
            		+ "        \"id\": 8\r\n"
            		+ "    },\r\n"
            		+ "    \"tranId\": \"MPESAREF\",\r\n"
            		+ "    \"ccNumber\":\"MPESAREF\"\r\n"
            		+ "}");
            httpPost.setEntity(requestBody);

            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);

            result.put("resultCode", 0);
        	result.put("resultMessage", response);
        	
            httpClient.close();
        } catch (Exception e) {
        	result.put("resultCode", 10010);
        	result.put("resultMessage", e.getMessage());
        }
        
        logger.info(result.toJSONString());
        return result;
    }
	
	public static String generateSignature(String method, String baseUrl, String timestamp, String nonce, String consumerKey, String consumerSecret, String token, String tokenSecret) throws NoSuchAlgorithmException, InvalidKeyException {
        String signatureMethod = "HMAC-SHA256";
        String version = "1.0";

        // Construct signature base string
        String baseString = method.toUpperCase() + "&" +
                            URLEncoder.encode(baseUrl, StandardCharsets.UTF_8) + "&" +
                            URLEncoder.encode("oauth_consumer_key=" + consumerKey + "&" +
                                             "oauth_nonce=" + nonce + "&" +
                                             "oauth_signature_method=" + signatureMethod + "&" +
                                             "oauth_timestamp=" + timestamp + "&" +
                                             "oauth_token=" + token + "&" +
                                             "oauth_version=" + version, StandardCharsets.UTF_8);

        // Generate signing key
        String signingKey = URLEncoder.encode(consumerSecret, StandardCharsets.UTF_8) + "&" +
                            URLEncoder.encode(tokenSecret, StandardCharsets.UTF_8);

        // Sign the signature base string using HMAC-SHA256
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(signingKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(keySpec);
        byte[] signatureBytes = hmacSha256.doFinal(baseString.getBytes(StandardCharsets.UTF_8));

        // Encode the signature and return
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

}
