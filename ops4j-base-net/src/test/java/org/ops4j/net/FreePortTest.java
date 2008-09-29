/*
 * Copyright 2008 Toni Menzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.net;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Toni Menzel (tonit)
 * @since Sep 29, 2008
 */
public class FreePortTest
{

    @Test
    public void testFreePortFirstTimeWithOneRange()
    {
        FreePort p = new FreePort( 1, 1 )
        {
            public boolean alreadyCalled = false;

            boolean isFree( int port )
            {
                assertEquals( 1, port );
                if( alreadyCalled )
                {
                    fail( "Just on call necessary" );
                }
                else
                {
                    alreadyCalled = true;
                }
                return true;
            }

        };
        assertEquals( 1, p.getPort() );
        assertEquals( 1, p.getPort() );
    }

    @Test
    public void testFreePortFirstTimeWithOneRangeNonFree()
    {
        FreePort p = new FreePort( 1, 1 )
        {
            public boolean alreadyCalled = false;

            boolean isFree( int port )
            {
                assertEquals( 1, port );
                if( alreadyCalled )
                {
                    // then free
                    return true;
                }
                else
                {
                    // first time non free
                    alreadyCalled = true;
                    return false;
                }

            }

        };
        try
        {
            assertEquals( 1, p.getPort() );
            fail( "Exception expected." );
        } catch( RuntimeException e )
        {

        }
        // second check should be made
        assertEquals( 1, p.getPort() );
    }

    @Test
    public void testRangeTraverse()
    {
        FreePort p = new FreePort( 1, 3 )
        {
            public int alreadyCalled = 0;

            boolean isFree( int port )
            {
                // this is the amount of required checks

                if( alreadyCalled > 3 )
                {
                    fail( "should not be called again." );
                }
                else
                {
                    alreadyCalled++;
                    if( port == 3 )
                    {
                        // must be the third call:
                        if( alreadyCalled == 3 )
                        {
                            return true;
                        }
                        else
                        {
                            fail( "Order of ports checked in given range is wrong. Must check in linear order." );
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                throw new RuntimeException( "Should not reach this." );
            }

        };

        assertEquals( 3, p.getPort() );
        // subsequent call:
        assertEquals( 3, p.getPort() );

    }
}
