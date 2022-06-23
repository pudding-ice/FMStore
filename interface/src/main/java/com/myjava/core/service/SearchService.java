package com.myjava.core.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface SearchService {
    Map<String, Object> search(Map paramMap);
}
