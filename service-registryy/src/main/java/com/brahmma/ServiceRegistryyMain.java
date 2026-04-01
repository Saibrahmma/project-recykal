package com.brahmma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//service-registry Port : 8761

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryyMain 
{
  public static void main( String[] args )
  {
  	SpringApplication.run(ServiceRegistryyMain.class, args);
  }
}
