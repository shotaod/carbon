package org.carbon.sample.web.sample.index;

import java.util.List;

import lombok.Data;

/**
 * @author Shota Oda 2016/12/02.
 */
@Data
class JobForm {
    class Department {
        private String name;
        private String position;
    }

    private String name;
    private Integer workYears;

    private List<Department> departments;
}
