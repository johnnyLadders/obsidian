/**
 * Copyright 2013 Hunter Hegler
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
 */

package org.obsidian.methodTest;

import java.lang.reflect.Method;
import org.obsidian.test.TestAbstract;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public class GenericTest extends MethodTest {

    public GenericTest(TestAbstract test, Method m) {
        super(test, m);
    }

    @Override
    public String toString() {
        return body.toString();
    }

    @Override
    void addAssertions() {
        body.append("\n\n\t\t//Assertions");
    }

}
