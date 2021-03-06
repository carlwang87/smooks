/*-
 * ========================LICENSE_START=================================
 * Smooks Core
 * %%
 * Copyright (C) 2020 Smooks
 * %%
 * Licensed under the terms of the Apache License Version 2.0, or
 * the GNU Lesser General Public License version 3.0 or later.
 * 
 * SPDX-License-Identifier: Apache-2.0 OR LGPL-3.0-or-later
 * 
 * ======================================================================
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * ======================================================================
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * =========================LICENSE_END==================================
 */
package org.smooks.delivery.dom;

import org.junit.Test;
import static org.junit.Assert.*;

import org.smooks.Smooks;
import org.smooks.FilterSettings;
import org.smooks.StreamFilterType;
import org.smooks.lang.LangUtil;
import org.smooks.payload.StringSource;
import org.smooks.payload.StringResult;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class MILYN_247_Test {

	@Test
    public void test_MILYN_247_01() {
        if (LangUtil.getJavaVersion() != 1.5) {
            return;
        }

        Smooks smooks = new Smooks();

        smooks.setFilterSettings(new FilterSettings().setFilterType(StreamFilterType.DOM).setRewriteEntities(true));

        StringResult stringResult = new StringResult();
        smooks.filterSource(new StringSource("<a attrib=\"an &amp; 'attribute\">Bigger &gt; is better!</a>"), stringResult);
        assertEquals("<a attrib=\"an &amp; &apos;attribute\">Bigger &gt; is better!</a>", stringResult.getResult());
    }

	@Test
    public void test_MILYN_247_02() {
        if (LangUtil.getJavaVersion() != 1.5) {
            return;
        }

        Smooks smooks = new Smooks();

        smooks.setFilterSettings(new FilterSettings().setFilterType(StreamFilterType.DOM).setRewriteEntities(false));

        StringResult stringResult = new StringResult();
        smooks.filterSource(new StringSource("<a attrib=\"an &amp; 'attribute\">Bigger &gt; is better!</a>"), stringResult);
        assertEquals("<a attrib=\"an & 'attribute\">Bigger &#62; is better!</a>", stringResult.getResult());
    }

	@Test
    public void test_MILYN_247_03() {
        if (LangUtil.getJavaVersion() != 1.5) {
            return;
        }

        Smooks smooks = new Smooks();

        smooks.setFilterSettings(new FilterSettings().setFilterType(StreamFilterType.SAX).setRewriteEntities(true));

        StringResult stringResult = new StringResult();
        smooks.filterSource(new StringSource("<a attrib=\"an &amp; 'attribute\">Bigger &gt; is better!</a>"), stringResult);
        assertEquals("<a attrib=\"an &amp; &apos;attribute\">Bigger &gt; is better!</a>", stringResult.getResult());
    }

	@Test
    public void test_MILYN_247_04() {
        if (LangUtil.getJavaVersion() != 1.5) {
            return;
        }

        Smooks smooks = new Smooks();

        smooks.setFilterSettings(new FilterSettings().setFilterType(StreamFilterType.SAX).setRewriteEntities(false));

        StringResult stringResult = new StringResult();
        smooks.filterSource(new StringSource("<a attrib=\"an &amp; 'attribute\">Bigger &gt; is better!</a>"), stringResult);
        assertEquals("<a attrib=\"an & 'attribute\">Bigger &#62; is better!</a>", stringResult.getResult());
    }
}
