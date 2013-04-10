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

package org.obsidian.test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.obsidian.methodTest.ConstructorTest;
import org.obsidian.methodTest.GenericTest;
import org.obsidian.methodTest.GetterTest;
import org.obsidian.methodTest.SetterTest;
import org.obsidian.obsidianConstants.Content;
import org.obsidian.obsidianConstants.FieldNames;
import org.obsidian.obsidianConstants.Headers;
import org.obsidian.obsidianConstants.JUnitConstants;
import org.obsidian.obsidianConstants.Methods;
import org.obsidian.util.Helpers;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public class ConcreteClassTest extends TestAbstract {

    public ConcreteClassTest(Class classToTest) {
        super(classToTest);
    }

    @Override
    public void build() {

        //package declaration
        body.append(classTested.getPackage().toString());
        body.append(";\n\n");

        getDynamicImports().addAll(Arrays.asList(Content.IMPORTS));

        //time stamp
        body.append("\n\n//\n//");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'Generated on 'EEE, d MMM yyyy 'at' hh:mmaa");
        body.append(dateFormat.format(date));
        body.append("\n//");

        //class Declaration
        body.append("\n\npublic class");
        body.append(" ");
        body.append(getTestClassName());
        body.append("{");

        //Default Value Fields
        body.append(Headers.DEFAULT_VALUE_FIELD_HEADER);
        body.append(buildDefaultValueFields());


        //Obsidian Fields
        body.append(Headers.OBSIDIAN_FIELDS_HEADER);
        for (String s : Content.OBSIDIAN_FIELDS) {
            body.append("\n\n\t");
            body.append(s);
        }

        //default instance declaration
        body.append(Headers.DEFAULT_INSTANCE_HEADER);
        body.append("\n\t");
        body.append("private static ");
        body.append(getClassTestedSimpleName());
        body.append(" ");
        body.append(FieldNames.DEFAULT_INSTANCE_NAME);
        body.append(";");



        //test class constructor
        body.append("\n\t");
        body.append("\n\n\tpublic");
        body.append(" ");
        body.append(getTestClassName());
        body.append("(){");
        body.append("\n\t");
        body.append("}");

        //setUpClass
        body.append(JUnitConstants.buildSetUpClass(this));

        //tearDownClass
        body.append(JUnitConstants.buildTearDownClass());

        //setUp
        body.append(JUnitConstants.buildSetUp(this));

        //tearDown
        body.append(JUnitConstants.buildTearDown());

        //constructors
        if (constructors.length != 0) {
            body.append(Headers.CONSTRUCTOR_TESTS_HEADER);
        }

        //for each constructor in the array of constructors:
        for (int i = 0; i < constructors.length; i++) {

            if (!(Modifier.isPrivate(constructors[i].getModifiers()))) {
                ConstructorTest ct = new ConstructorTest(this, constructors[i]);
                ct.build();
                body.append(ct.toString());
            }

        }

        //field reflection method
        body.append(Headers.FIELD_REFLECTION_HEADER);
        body.append(Helpers.buildFieldReflectionMethod(classTestedSimpleName));

        //getters
        if (!getters.isEmpty()) {
            body.append(Headers.GETTER_TESTS_HEADER);
        }

        //for each getter dataInputStream the ArrayList<Method> getters:
        for (Method m : getters) {
            
            GetterTest gt = new GetterTest(this, m);
            gt.build();
            body.append(gt.toString());
        }

        //setters
        if (!setters.isEmpty()) {
            body.append(Headers.SETTER_TESTS_HEADER);
        }

        //for each getter dataInputStream the ArrayList<Method> setters:
        for (Method m : setters) {
            
            SetterTest st = new SetterTest(this, m);
            st.build();
            body.append(st.toString());
        }

        //remaining tests
        if (!remainingMethods.isEmpty()) {

            body.append(Headers.REMAINING_TESTS_HEADER);

        }

        //for each method dataInputStream the ArrayList<method> remaining tests:
        for (Method m : remainingMethods) {

            GenericTest gt = new GenericTest(this, m);

            gt.build();

            body.append(gt.toString());
        }


        //Method Accountability
        body.append(Headers.METHOD_ACCOUNTABILITY_HEADER);
        body.append(Methods.METHOD_ACCOUNTABILITY);

//        //class implementation
//        body.append(classImpl);

        //dynamic Imports
        //sort dynamic imports
        setDynamicImports(Helpers.sortImports(getDynamicImports()));
        String dynamicImportsString = "";

        for (String s : getDynamicImports()) {
            dynamicImportsString = dynamicImportsString + "import " + s + ";\n";
        }
        //insert after package declaration
        body.insert(body.indexOf("\n") + 3, dynamicImportsString);

        //Equality Methods
        body.append(Headers.EQUALITY_METHODS_HEADER);

        StringBuilder equalityMethods = new StringBuilder();

        for (Class c : getEqualityMethodTypes()) {
            equalityMethods.append(
                    buildTestClassEqualityMethodFromType(c));
        }

        //add getterTest class equalityMethod for Strings and Exceptions
        equalityMethods.append(
                buildTestClassEqualityMethodFromType(String.class));
        equalityMethods.append(
                buildTestClassEqualityMethodFromType(Exception.class));
        equalityMethods.append(
                buildTestClassEqualityMethodFromType(Throwable.class));

        body.append(equalityMethods.toString());

        //closing
        body.append("\n}");

    }

    public String toString() {
        return body.toString();
    }
}
