package org.example.smartcampus.config;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends ResourceConfig {
    public SmartCampusApplication() {
        // Scan packages for resources, exception mappers, and filters
        packages("org.example.smartcampus.resource",
                "org.example.smartcampus.exception",
                "org.example.smartcampus.filter");
    }
}
