package org.ops4j.net;

public interface PortTester {

	/**
	 * Checks a given port for availability
	 *
	 * @param port Port to check
	 *
	 * @return true if its free, otherwise false.
	 */
	boolean isFree( int port );

}
