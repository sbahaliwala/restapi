package com.example.restapi.models;

import lombok.Data;
import java.util.Map;

@Data
public class ObjectResponse {
    private String id;
    private String name;
    private String createdAt;
    private Map<String, Object> data;
}
