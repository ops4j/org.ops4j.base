/*
 * Copyright 2009 Alin Dreghiciu.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.io;

import java.io.IOException;

/**
 * An {@link java.io.IOException}
 * 
 * @author Alin Dreghiciu
 * @since 1.1.0, August 31, 2009
 */
public class HierarchicalIOException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 3608772268419554801L;

    /**
     * Constructor.
     * 
     * @param message
     *            exception message
     */
    public HierarchicalIOException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param message
     *            exception message
     * @param cause
     *            exception cause
     */
    public HierarchicalIOException(final String message, final Throwable cause) {
        super(message);
        initCause(cause);
    }

    /**
     * Constructor.
     * 
     * @param cause
     *            exception cause
     */
    public HierarchicalIOException(final Throwable cause) {
        super();
        initCause(cause);
    }
}
