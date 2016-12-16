package org.carbon.sample.web.sample.index;

import lombok.Data;

import java.util.List;

/**
 * @author ubuntu 2016/12/02.
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
