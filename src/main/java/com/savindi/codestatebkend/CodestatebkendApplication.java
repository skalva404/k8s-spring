package com.savindi.codestatebkend;

//import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//@Slf4j
@SpringBootApplication
@RestController
public class CodestatebkendApplication {

    private static Logger log = LoggerFactory.getLogger(CodestatebkendApplication.class);

    final static String serverUrl1 = "https://gist.githubusercontent.com/skalva404/482f7f3c8ee5dfea0fb646b8d566afc5/raw/27fbc94c952c2116e4a5d0c8dc90f845f2e4d8ed/states_hash.json";
    final static String serverUrl2 = "https://gist.githubusercontent.com/skalva404/ed93e3aafd35b0564525c3546c58d507/raw/f335dfcdce2d55f550b90371126a05bb25de4092/states_title_case.json";

    public static void main(String[] args) {
        SpringApplication.run(CodestatebkendApplication.class, args);
    }

    public static String requestProcessedData(int urlid) {
        String serverUrl = null;
        if (urlid == 1) {
            serverUrl = serverUrl1;
        } else if (urlid == 2) {
            serverUrl = serverUrl2;
        } else {
            return "ERROR";
        }

        RestTemplate request = new RestTemplate();
        String result = request.getForObject(serverUrl, String.class);
        System.out.print(serverUrl);
        return (result);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public static String Hello() {
        return "HELLO IM DATA READER";
    }

    @GetMapping(value = "/readDataForCode", produces = MediaType.APPLICATION_JSON_VALUE)
    public static String requestCodeData() {
        return requestProcessedData(1);
    }

    @GetMapping(value = "/readDataForState", produces = MediaType.APPLICATION_JSON_VALUE)
    public static String requestStateData() {
        return requestProcessedData(2);
    }


    public static String requestProcessedData(String url) {
        RestTemplate request = new RestTemplate();
        String result = request.getForObject(url, String.class);
        System.out.print(url);
        return (result);
    }

    @GetMapping("/codeToState")
    public static String CodeToState(@RequestParam("code") String code) {
        String state = null;
        try {
            String response = requestProcessedData(serverUrl1);
            JSONObject jsonObject = new JSONObject(response);
            state = jsonObject.getString(code.toUpperCase());
        } catch (Exception e) {
            log.info("[ERROR] : [CUSTOM_LOG] : " + e);
        }

        if (state == null) {
            state = "No Match Found";
        }
        return state;
    }

    @GetMapping("/stateToCode")
    public static String StateToCode(@RequestParam("state") String state) {
        String value = "";
        try {
            String response = requestProcessedData(serverUrl2);
            JSONArray jsonArray = new JSONArray(response);

            for (int n = 0; n < jsonArray.length(); n++) {
                JSONObject object = jsonArray.getJSONObject(n);
                String name = object.getString("name");

                if (state.equalsIgnoreCase(name)) {
                    value = object.getString("abbreviation");
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("[ERROR] : [CUSTOM_LOG] : " + e);
        }

        if (value == null) {
            value = "No Match Found";
        }
        return value;
    }
}
