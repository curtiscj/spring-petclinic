package org.springframework.samples.petclinic.supplement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@SessionAttributes("supplements")
public class SupplementController {

    @Value("localhost")
    private String host;
    static final Logger LOG = LoggerFactory.getLogger(SupplementController.class);

    @RequestMapping(value = "/supplements", method = RequestMethod.GET)
    public String processFindForm(Map<String, Object> model) {
        Collection<Supplement> results = getSupplements();
        model.put("supplements", results);
        LOG.info("It is working");
        LOG.trace("Trace feature is working");
        LOG.debug(results.toString());

//		if(true) {
//			throw new RuntimeException("KEYBOARD NOT FOUND!!!!!");
//		}
        return "supplement/supplementList";
    }

    private Collection<Supplement> getSupplements(){
        String json = getLocalSupplementsJson();

        try {
            ObjectMapper mapper = new ObjectMapper();
            Supplement[] supplements = mapper.readValue(json, Supplement[].class);
            return Arrays.asList(supplements);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    //displays supplements from locally stored location
    private String getLocalSupplementsJson() {
        return
            "[" +
                "{\"name\": \"Apples\", \"price\": \"123.6\"}," +
                "{\"name\": \"Bananas\", \"price\": \"234.5\"}," +
                "{\"name\": \"Carrots\", \"price\": \"85.3\"}," +
                "{\"name\": \"Pears\", \"price\": \"948.8\"}" +
                "]";
    }

    private String getRemoteSupplementsJson() {
        StringBuilder sb = new StringBuilder();
        try {
            String spec = "http://supplements-service:8080/supplements/";
            System.out.println("Calling to " +spec);

            URL url = new URL(spec);
            URLConnection urlConnection = url.openConnection();

            urlConnection.setConnectTimeout(4000);
            try {
                urlConnection.connect();
            }catch (UnknownHostException | SocketTimeoutException | ConnectException e) {
                System.out.println("Whoops Service isn't available!!! What you gonna do?");
                return getLocalSupplementsJson();
            }
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
