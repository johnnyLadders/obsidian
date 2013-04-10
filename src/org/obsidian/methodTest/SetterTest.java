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
import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.obsidian.obsidianConstants.FieldNames;
import org.obsidian.test.TestAbstract;
import org.obsidian.util.Helpers;
import org.obsidian.util.MethodSorter;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public class SetterTest extends MethodTest {

    Method setter;

    public SetterTest(TestAbstract test, Method m) {
        super(test, m);
        this.setter = m;
    }

    @Override
    void addAssertions() {
        body.append("\n\n\t\t//Assertions");
        
        String exceptionTryTab = "";
        
        if(throwsExceptions){
            exceptionTryTab = "\t";
        }

        //set value from getter body
        body.append("\n\n\t\t\t");
        body.append(exceptionTryTab);
        body.append("//Get Set Field With Getter");
        body.append("\n\t\t\t");
        body.append(exceptionTryTab);
        body.append(parameters[0].getSimpleName());
        body.append(" ");
        body.append(FieldNames.RESULT);
        body.append(" = ");
        body.append(getGetterCallFromSetter(setter));
        body.append(";");

        //Assertions
        body.append("\n\n\t\t\t");
        body.append(exceptionTryTab);
        body.append("//Assertion");
        body.append("\n\t\t\t");
        body.append(exceptionTryTab);
        body.append(Helpers.buildEqualityAssertion(parameters[0],
                "testCases[" + FieldNames.TCI_VARIABLE + "][1]",
                FieldNames.RESULT, 3));

    }

    @Override
    public String toString() {
        return body.toString();
    }

    //Build Helper Methods
    private String getGetterCallFromSetter(Method setter) {
        String retVal = "";

        String setterName = setter.getName();
        String possibleFieldName = setterName;
        if (setterName.length() > 3) {
            possibleFieldName = setterName.substring(3);
        }

        for (Method m : MethodSorter.getGetters(declaringClass.getDeclaredMethods())) {
            Pattern pattern = Pattern.compile(".*" + possibleFieldName,
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(m.getName());
            if (matcher.matches() && m.getReturnType().getSimpleName().compareToIgnoreCase(setter.getReturnType().getSimpleName()) == 0) {
                if (Modifier.isStatic(m.getModifiers())) {
                    retVal = testClass.getClassTestedSimpleName() + "." + m.getName()
                            + "()";
                } else {
                    retVal = "instance." + m.getName() + "()";
                }

            }
        }

        if (retVal.compareToIgnoreCase("") == 0) {
            retVal = testClass.buildConstructionStringFromType(setter.//
                    getParameterTypes()[0], new Constructor[0]) + ";// TODO: replace with"
                    + " with a getter or Reflection call";

        }

        return retVal;
    }
}
