package com.myjava.core.pojo.response;

import com.myjava.core.pojo.specification.Specification;
import com.myjava.core.pojo.specification.SpecificationOption;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SpecificationResponse implements Serializable {
    private Specification spec;

    private List<SpecificationOption> specOpts;
}
