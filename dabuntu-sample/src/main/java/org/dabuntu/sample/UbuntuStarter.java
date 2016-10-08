package org.dabuntu.sample;

import org.dabuntu.web.WebStarter;

/**
 * @author ubuntu 2016/10/02
 */
public class UbuntuStarter {
	public static void main(String[] args) throws Exception {
		new WebStarter().start(ScanBase.class);
	}
}
