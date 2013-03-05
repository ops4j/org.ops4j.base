/*
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class URLUtilsTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();   

    @Test
    public void testDecodingUsernameWithEncodings()
    {
        assertEquals("user@gmail.com:mypassword", URLUtils.decode("user%40gmail.com:mypassword"));
        assertEquals("user@gmail.com:mypassword", URLUtils.decode("user@gmail.com:mypassword"));
        assertEquals("user:mypassword", URLUtils.decode("user:mypassword"));
        assertEquals("user@gmail.com:mypass+word", URLUtils.decode("user%40gmail.com:mypass%2Bword"));
        assertNull(URLUtils.decode(null));
    }

    @Test
    public void testInvalidEncodedUsername()
    {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("must be followed by two hex digits");
        URLUtils.decode("foo%");
    }
}
