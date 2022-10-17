package com.fernandorobles.school.audit;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SchoolInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder){
        Map<String, String> schoolMap = new HashMap<>();
        schoolMap.put("App Name", "School");
        schoolMap.put("App Description", "School Web Application for students and Admin");
        schoolMap.put("App Version", "1.0.0");
        schoolMap.put("Contact Email", "info@gmail.com");
        schoolMap.put("Contact Mobile", "6861588888");
        builder.withDetail("school-info", schoolMap);
    }
}
