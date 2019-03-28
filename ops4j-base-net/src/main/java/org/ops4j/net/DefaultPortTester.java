package org.ops4j.net;

import java.net.ServerSocket;

public final class DefaultPortTester implements PortTester {

	@Override
	public boolean isFree(int port) {
		try
		{
			ServerSocket sock = new ServerSocket( port );
			sock.close();
			// is free:
			return true;
			// We rely on an exception thrown to determine availability or not availability.
		} catch( Exception e )
		{
			// not free.
			return false;
		}
	}
}
