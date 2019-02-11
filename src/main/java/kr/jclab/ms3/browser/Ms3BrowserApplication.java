package kr.jclab.ms3.browser;

import kr.jclab.cloud.ms3.client.MS3ClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ms3BrowserApplication {

    public static void main(String[] args) {
        MS3ClientBuilder.init("http://127.0.0.1:9005/ms3/");
        SpringApplication.run(Ms3BrowserApplication.class, args);
    }

}

