package org.jitsi.hammer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.jitsi.hammer.stats.HammerStats;

public class FunctionTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println(HammerStats.class);
		System.out.println(System.getProperty(Main.PNAME_SC_HOME_DIR_LOCATION) + File.separator
				+ System.getProperty(Main.PNAME_SC_HOME_DIR_NAME) + File.separator + "stats");
		System.out.println(File.separator);

		String XMPPhost = "52.88.34.121";
		String roomName = "/Wrapper";
		
		URL url = new URL("http",XMPPhost, roomName);

		if (Desktop.isDesktopSupported()) {
			
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(url.toURI());
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("open " + url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
