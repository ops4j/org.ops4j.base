/*
 * Copyright 2011 Harald Wellmann.
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
package org.ops4j.spi;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.ops4j.spi.ServiceProviderFinder.findAnyServiceProvider;
import static org.ops4j.spi.ServiceProviderFinder.findServiceProviders;
import static org.ops4j.spi.ServiceProviderFinder.loadAnyServiceProvider;
import static org.ops4j.spi.ServiceProviderFinder.loadUniqueServiceProvider;

import java.util.List;

import javax.swing.text.StyleContext.SmallAttributeSet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests {@link ServiceProviderFinder}.
 * 
 * @author Harald Wellmann
 */
public class ServiceProviderFinderTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void throwWhenNoProviderFoundOnLoad() {
        thrown.expect( NoServiceProviderException.class );
        loadAnyServiceProvider( UnsatisfiedService.class );
    }

    @Test
    public void loadUniqueThrowsForZeroProviders() {
        thrown.expect( NoServiceProviderException.class );
        loadUniqueServiceProvider( UnsatisfiedService.class );
    }

    @Test
    public void findAnyReturnsNull() {
        UnsatisfiedService service = findAnyServiceProvider( UnsatisfiedService.class );
        assertThat(service, is(nullValue()));
    }
    
    @Test
    public void findAnyReturnsEmpty() {
        List<UnsatisfiedService> providers = findServiceProviders( UnsatisfiedService.class );
        assertThat(providers, is(notNullValue()));
        assertThat(providers.isEmpty(), is(true));
    }
    
    @Test
    public void findReturnsOne() {
        List<IceCreamService> providers = findServiceProviders( IceCreamService.class );
        assertThat(providers, is(notNullValue()));
        assertThat(providers.size(), is(1));
        assertThat(providers.get(0), is(instanceOf( VanillaService.class )));
    }
    
    @Test 
    public void loadAnySuccess() {
        IceCreamService service = loadAnyServiceProvider( IceCreamService.class );
        assertThat(service, is(notNullValue()));
        assertThat(service, is(instanceOf( VanillaService.class )));
    }

    @Test 
    public void loadUniqueSuccess() {
        IceCreamService service = loadUniqueServiceProvider( IceCreamService.class );
        assertThat(service, is(notNullValue()));
        assertThat(service, is(instanceOf( VanillaService.class )));
    }
    
    @Test 
    public void loadUniqueIsNotRepeatable() {
        IceCreamService service1 = loadUniqueServiceProvider( IceCreamService.class );
        assertThat(service1, is(notNullValue()));

        IceCreamService service2 = loadUniqueServiceProvider( IceCreamService.class );
        assertThat(service2, is(notNullValue()));
        assertThat(service2, is(not(sameInstance(service1))));
    }
    
    @Test
    public void findReturnsTwo() {
        List<MusicService> providers = findServiceProviders( MusicService.class );
        assertThat(providers, is(notNullValue()));
        assertThat(providers.size(), is(2));
    }
    
    @Test
    public void loadUniqueThrowsWhenNonUnique() {
        thrown.expect( NonUniqueServiceProviderException.class );
        loadUniqueServiceProvider( MusicService.class );
    }
}
