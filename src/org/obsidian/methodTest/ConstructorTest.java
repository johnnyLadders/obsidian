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
import java.lang.reflect.Field;
import org.obsidian.obsidianConstants.FieldNames;
import org.obsidian.obsidianConstants.MethodNames;
import org.obsidian.test.TestAbstract;
import org.obsidian.util.Helpers;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public class ConstructorTest extends MethodTest {

    Constructor constructor;

    public ConstructorTest(TestAbstract test, Constructor c) {
        super(test, c);
        constructor = c;
    }

    @Override
    void addAssertions() {
        body.append("\n\n\t\t//Assertions");

        //If constructor Has Parameters
        String forLoopTab = "";//initialized to "\t" if for loop is needed

        if (parameters.length != 0) {
            //initialize forLoopTab
            forLoopTab = "\t";

        }

        //open Try block
        if (declaringClass.getDeclaredFields().length != 0) {
            body.append("\n\n\t\t");
            body.append(forLoopTab);
            body.append("try {");
        }


        for (Field f : declaringClass.getDeclaredFields()) {
            String fieldName = f.getName();
            Class fieldClass = f.getType();
            String castType = fieldClass.getSimpleName();
            String expectResultName = f.getName() + "ExpResult";
            String resultName = f.getName() + "Result";




            //Field comment
            body.append("\n\n\n\t\t\t");
            body.append(forLoopTab);
            body.append("//");
            body.append("\n\t\t\t");
            body.append(forLoopTab);
            body.append("//");
            body.append("Name: ");
            body.append(f.getName());
            body.append("\n\t\t\t");
            body.append(forLoopTab);
            body.append("//");
            body.append("Type: ");
            body.append(f.getType().getSimpleName());
            body.append("\n\t\t\t");
            body.append(forLoopTab);
            body.append("//");

            //field block open
            body.append("\n\t\t\t");
            body.append(forLoopTab);
            body.append("{");

            //expected result
            body.append("\n\t\t\t\t");
            body.append(forLoopTab);
            body.append("// Expected Result");
            body.append("\n\t\t\t\t");
            body.append(forLoopTab);
            body.append(f.getType().getSimpleName());
            body.append(" ");
            body.append(expectResultName);
            body.append(" = ");
            body.append(Helpers.getDefalutValueFieldName(f));
            body.append(";");

            //note
            body.append("\n\n\t\t\t\t");
            body.append(forLoopTab);
            body.append("//Reflection");

            //if primitive: must cast differently
            if (fieldClass.isPrimitive()) {
                castType = Helpers.PRIMITIVE_OBJECT_NAMES.get(castType);
            }

            //get
            body.append("\n\t\t\t\t");
            body.append(forLoopTab);
            body.append(fieldClass.getSimpleName());
            body.append(" ");
            body.append(resultName);
            body.append(" = (");
            body.append(castType);
            body.append(") ");
            body.append(testClass.getTestClassName());
            body.append(".//\n\t\t\t\t");
            body.append(forLoopTab);
            body.append(MethodNames.FIELD_REFLECTION_METHOD_NAME);
            body.append("(//\n\t\t\t\t");
            body.append(forLoopTab);
            body.append(FieldNames.DEFAULT_INSTANCE_NAME);
            body.append(", //");
            body.append("\n\t\t\t\t");
            body.append(forLoopTab);
            body.append("\"");
            body.append(f.getName());
            body.append("\"");
            body.append(");");

            //assertion
            body.append("\n\n\t\t\t\t");
            body.append(forLoopTab);
            body.append("//Assertion");
            body.append("\n\t\t\t\t");
            body.append(forLoopTab);
            body.append(Helpers.buildEqualityAssertion(fieldClass,
                    expectResultName, resultName, 4));

            //field close open
            body.append("\n\t\t\t");
            body.append(forLoopTab);
            body.append("}");



        }

        if (declaringClass.getDeclaredFields().length != 0) {
            //close Try block
            body.append("\n\n\t\t");
            body.append(forLoopTab);
            body.append("}");

            //constructor catch blocks
            body.append(buildConstructorCatchBlocks(constructor));
        }
    }

    @Override
    public String toString() {
        return body.toString();
    }

    //Build Helper Methods
    /**
     *
     * Builds the current constructor "print out methodName." meaning
     * constructorOne instead of constructorOneTest. Only works up to twenty,
     * when the integer representing the current constructor number is appended
     * to the word constructor. For example, constructor21
     *
     * @return returns "constructor" + current Constructor Number as a word
     */
    private String buildConstructorPrintStatementName() {

        int currentConstructorTestNumber = testClass.getCurrentConstructorTestNumber();

        //constructor getterTest methodName == "constructorOneTest"
        //constructor print statement methodName == "Constructor One
        String constructorName = "";

        if (currentConstructorTestNumber <= 20) {
            constructorName = "Constructor "
                    + Helpers.NUMBER_WORDS[currentConstructorTestNumber - 2];
        } else {
            constructorName = "Constructor " + currentConstructorTestNumber;
        }

        return constructorName;
    }

    /**
     * Builds catch blocks to go with the try dataInputStream constructor tests.
     * Uses the current constructor methodName and the class methodName to
     * create meaningful fail statements.
     *
     * @return catch blocks to go with constructor getterTest try
     */
    private String buildConstructorCatchBlocks(Constructor constructor) {
        StringBuilder constructorCatchBlocks = new StringBuilder();

        //If constructor Has Parameters
        String forLoopTab = "";//initialized to "\t" if for loop is needed

        if (parameters.length != 0) {
            //initialize forLoopTab
            forLoopTab = "\t";

        }

        int currentConstructorTestNumber = testClass.getCurrentConstructorTestNumber();
        String classTestedSimpleName = testClass.getClassTestedSimpleName();

        //Illegal Argument Exception
        constructorCatchBlocks.append("catch (IllegalArgumentException ex){");
        constructorCatchBlocks.append("\n\t\t\t");
        constructorCatchBlocks.append(forLoopTab);
        constructorCatchBlocks.append("fail(\"Illegal Argument Exception while testing the ");
        constructorCatchBlocks.append(Helpers.//
                ORDINAL_NUMBERS[currentConstructorTestNumber - 2]);
        constructorCatchBlocks.append(" Constructor in ");
        constructorCatchBlocks.append(classTestedSimpleName);


        constructorCatchBlocks.append("\");");
        constructorCatchBlocks.append("\n\t\t");
        constructorCatchBlocks.append(forLoopTab);
        constructorCatchBlocks.append("}");

        //Illegal Access Exception
        constructorCatchBlocks.append("catch (IllegalAccessException ex){");
        constructorCatchBlocks.append("\n\t\t\t");
        constructorCatchBlocks.append(forLoopTab);
        constructorCatchBlocks.append("fail(\"Illegal Access Exception while testing the ");
        constructorCatchBlocks.append(Helpers.//
                ORDINAL_NUMBERS[currentConstructorTestNumber - 2]);
        constructorCatchBlocks.append(" Constructor in ");
        constructorCatchBlocks.append(classTestedSimpleName);
        constructorCatchBlocks.append("\");");
        constructorCatchBlocks.append("\n\t\t");
        constructorCatchBlocks.append(forLoopTab);
        constructorCatchBlocks.append("}");

        //No Such Field Exception
        constructorCatchBlocks.append("catch (NoSuchFieldException ex){");
        constructorCatchBlocks.append("\n\t\t\t");
        constructorCatchBlocks.append(forLoopTab);
        constructorCatchBlocks.append("fail(\"No Such Field Exception while testing the ");
        constructorCatchBlocks.append(Helpers.//
                ORDINAL_NUMBERS[currentConstructorTestNumber - 2]);
        constructorCatchBlocks.append(" Constructor in ");
        constructorCatchBlocks.append(classTestedSimpleName);
        constructorCatchBlocks.append("\");");
        constructorCatchBlocks.append("\n\t\t");
        constructorCatchBlocks.append(forLoopTab);
        constructorCatchBlocks.append("}");

        //Security Exception
        constructorCatchBlocks.append("catch (SecurityException ex){");
        constructorCatchBlocks.append("\n\t\t\t");
        constructorCatchBlocks.append(forLoopTab);
        constructorCatchBlocks.append("fail(\"Security Exception while testing the ");
        constructorCatchBlocks.append(Helpers.//
                ORDINAL_NUMBERS[currentConstructorTestNumber - 2]);
        constructorCatchBlocks.append(" Constructor in ");
        constructorCatchBlocks.append(classTestedSimpleName);
        constructorCatchBlocks.append("\");");
        constructorCatchBlocks.append("\n\t\t");
        constructorCatchBlocks.append(forLoopTab);
        constructorCatchBlocks.append("}");


        return constructorCatchBlocks.toString();
    }
}
