package com.myjava.core.pojo.request;

import com.myjava.core.pojo.specification.Specification;
import com.myjava.core.pojo.specification.SpecificationOption;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SpecificationRequest implements Serializable {
    private Specification spec;

    private List<SpecificationOption> specOpts;
}
