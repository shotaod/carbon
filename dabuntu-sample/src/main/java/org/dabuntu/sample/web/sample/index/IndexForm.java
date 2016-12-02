package org.dabuntu.sample.web.sample.index;

import lombok.Data;

/**
 * @author ubuntu 2016/10/12.
 */
@Data
public class IndexForm {
	public class InnerForm{
		private String data1;
		private String data2;

		public String getData1() {
			return data1;
		}

		public String getData2() {
			return data2;
		}
	}
	private String data1;
	private String data2;
	private InnerForm inner;
}
