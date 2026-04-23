package org.example.smartcampus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getDiscovery() {
        Map<String, Object> discovery = new HashMap<>();
        discovery.put("version", "v1.0");
        discovery.put("description", "Smart Campus Sensor & Room Management API");
        discovery.put("admin_contact", "admin@westminster.ac.uk");
        
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "rooms");
        links.put("sensors", "sensors");
        discovery.put("links", links);
        
        return discovery;
    }
}
