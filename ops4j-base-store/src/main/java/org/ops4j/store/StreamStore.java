package org.ops4j.store;

import java.io.InputStream;

/**
 * Convenience Interface since most usage is actually with InputStreams.
 *
 * @since 1.6.0
 * @author Toni Menzel (toni.menzel@rebaze.com)
 */
public interface StreamStore extends Store<InputStream>
{
}
