package com.myjava.core.pojo.template;

import com.myjava.core.pojo.good.Brand;
import com.myjava.core.pojo.response.SpecificationResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TypeTemplateSearch implements Serializable {
    private Long id;

    private List<Brand> brandList;

    private List<SpecificationResponse> specificationList;
}
