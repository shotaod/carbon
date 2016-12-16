package org.carbon.sample;

import org.carbon.web.WebStarter;

/**
 * @author Shota Oda 2016/10/02
 */
public class CarbonStarter {
	public static void main(String[] args) throws Exception {
		new WebStarter().start(ScanBase.class);
	}
}
