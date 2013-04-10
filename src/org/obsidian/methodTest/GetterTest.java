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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.obsidian.obsidianConstants.FieldNames;
import org.obsidian.test.TestAbstract;
import org.obsidian.util.Helpers;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public class GetterTest extends MethodTest {

    Method getter;

    public GetterTest(TestAbstract test, Method getter) {
        super(test, getter);
        this.getter = getter;
    }

    @Override
    void addAssertions() {
        //Assertions
        body.append("\n\n\t\t");
        body.append("//Assertions");
        
        //Expected Result
        body.append("\n\n\t\t");
        body.append("//Expected Result");
        body.append("\n\t\t");
        body.append(returnType.getSimpleName());
        body.append(" ");
        body.append(FieldNames.EXPECTED_RESULT);
        body.append(" = ");
        body.append(testClass.buildConstructionStringFromType(returnType, new Constructor[0]));
        body.append(";");


        body.append("\n\n\t\t");
        body.append(Helpers.buildEqualityAssertion(returnType,
                FieldNames.EXPECTED_RESULT, FieldNames.ACTUAL_RESULT, 2));

    }

    @Override
    public String toString() {
        return body.toString();
    }
}
