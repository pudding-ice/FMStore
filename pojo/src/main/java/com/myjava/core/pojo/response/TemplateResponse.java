package com.myjava.core.pojo.response;

import com.myjava.core.pojo.specification.Specification;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TemplateResponse implements Serializable {
    private Long id;

    private String name;

    private List<SpecificationResponse> specificationList;
}
