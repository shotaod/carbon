package org.dabuntu.sample.web.request.index;

/**
 * @author ubuntu 2016/10/15.
 */
public class TestResponseModel {
	private String form1;
	private String form2;
	private String cookie1;
	private String cookie2;

	public void setForm1(String form1) {
		this.form1 = form1;
	}
	public void setForm2(String form2) {
		this.form2 = form2;
	}
	public void setCookie1(String cookie1) {
		this.cookie1 = cookie1;
	}
	public void setCookie2(String cookie2) {
		this.cookie2 = cookie2;
	}

	public String getForm1() {
		return form1;
	}
	public String getForm2() {
		return form2;
	}
	public String getCookie1() {
		return cookie1;
	}
	public String getCookie2() {
		return cookie2;
	}
}
