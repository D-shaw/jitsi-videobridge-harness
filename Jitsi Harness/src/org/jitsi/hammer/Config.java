package org.jitsi.hammer;

import java.io.File;

/**
 *
 * @author Jitsi Test Harness Team
 *
 *         This class contains the configuration variables to launch Jitsi Testharness.
 *         Some code was copies from Jitsi Hammer, since TestHarness will be build on top of
 *         Jitsi Hammer.
 */
public class Config {
	/**
	 * The name of the property that stores the home dir for cache data, such as
	 * avatars and spelling dictionaries.
	 */
	public static final String PNAME_SC_CACHE_DIR_LOCATION = "net.java.sip.communicator.SC_CACHE_DIR_LOCATION";

	/**
	 * The name of the property that stores the home dir for application log
	 * files (not history).
	 */
	public static final String PNAME_SC_LOG_DIR_LOCATION = "net.java.sip.communicator.SC_LOG_DIR_LOCATION";

	/**
	 * The name of the property that stores our home dir location.
	 */
	public static final String PNAME_SC_HOME_DIR_LOCATION = "net.java.sip.communicator.SC_HOME_DIR_LOCATION";

	/**
	 * The name of the property that stores our home dir name.
	 */
	public static final String PNAME_SC_HOME_DIR_NAME = "net.java.sip.communicator.SC_HOME_DIR_NAME";

	/**
	 * Sets the system properties net.java.sip.communicator.SC_HOME_DIR_LOCATION
	 * and net.java.sip.communicator.SC_HOME_DIR_NAME (if they aren't already
	 * set) in accord with the OS conventions specified by the name of the OS.
	 *
	 * Please leave the access modifier as package (default) to allow launch-
	 * wrappers to call it.
	 *
	 * @param osName
	 *            the name of the OS according to which the SC_HOME_DIR_*
	 *            properties are to be set
	 */
	static void setScHomeDir(String osName) {
		/*
		 * Though we'll be setting the SC_HOME_DIR_* property values depending
		 * on the OS running the application, we have to make sure we are
		 * compatible with earlier releases i.e. use
		 * ${user.home}/.sip-communicator if it exists (and the new path isn't
		 * already in use).
		 */
		String profileLocation = System.getProperty(PNAME_SC_HOME_DIR_LOCATION);
		String name = System.getProperty(PNAME_SC_HOME_DIR_NAME);

		if (profileLocation == null || name == null) {
			String defaultLocation = System.getProperty("user.home");
			String defaultName = ".Jitsi-Hammer";

			if (osName.startsWith("Mac")) {
				if (profileLocation == null)
					profileLocation = System.getProperty("user.home") + File.separator + "Library" + File.separator
							+ "Application Support";

				if (name == null)
					name = "Jitsi-Hammer";
			} else if (osName.startsWith("Windows")) {
				/*
				 * Primarily important on Vista because Windows Explorer opens
				 * in %USERPROFILE% so .sip-communicator is always visible. But
				 * it may be a good idea to follow the OS recommendations and
				 * use APPDATA on pre-Vista systems as well.
				 */
				if (profileLocation == null)
					profileLocation = System.getenv("APPDATA");
				if (name == null)
					name = "Jitsi-Hammer";
			}

			/* If there're no OS specifics, use the defaults. */
			if (profileLocation == null)
				profileLocation = defaultLocation;
			if (name == null)
				name = defaultName;

			System.setProperty(PNAME_SC_HOME_DIR_LOCATION, profileLocation);
			System.setProperty(PNAME_SC_HOME_DIR_NAME, name);
		}

		// when we end up with the home dirs, make sure we have log dir
		new File(new File(profileLocation, name), "log").mkdirs();
		System.out.println("Profile: " + profileLocation);
		System.out.println("name: " + name);

		// Make libjitsi treat the configuration file as read-only
		System.setProperty("net.java.sip.communicator.CONFIGURATION_FILE_IS_READ_ONLY", "true");
	}

	/**
	 * Sets some system properties specific to the OS that needs to be set at
	 * the very beginning of a program (typically for UI related properties,
	 * before AWT is launched).
	 *
	 * @param osName
	 *            OS name
	 */
	public static void setSystemProperties(String osName) {
		// setup here all system properties that need to be initialized at
		// the very beginning of an application
		if (osName.startsWith("Windows")) {
			// disable Direct 3D pipeline (used for fullscreen) before
			// displaying anything (frame, ...)
			System.setProperty("sun.java2d.d3d", "false");
		} else if (osName.startsWith("Mac")) {
			// On Mac OS X when switch in fullscreen, all the monitors goes
			// fullscreen (turns black) and only one monitors has images
			// displayed. So disable this behavior because somebody may want
			// to use one monitor to do other stuff while having other ones with
			// fullscreen stuff.
			System.setProperty("apple.awt.fullscreencapturealldisplays", "false");
		}
	}

}
