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
import java.util.ArrayList;
import org.obsidian.obsidianConstants.Content;
import org.obsidian.obsidianConstants.FieldNames;
import org.obsidian.obsidianConstants.JUnitConstants;
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
public abstract class MethodTest {

    //Useful method info
    String methodName = "";
    Class returnType;
    Class declaringClass;
    StringBuilder body;
    boolean isConstructor = false;
    //
    //Exceptions
    Boolean throwsExceptions = false;
    Class[] exceptionTypes = new Class[0];
    //
    //Parameters
    Boolean hasParameters = false;
    Class[] parameters = new Class[0];
    TestAbstract testClass;
    //
    //context that method will be called ex) instance.getName()
    String context = FieldNames.DEFAULT_INSTANCE_NAME;

    private MethodTest(TestAbstract test) {
        testClass = test;
        body = new StringBuilder();

    }

    public MethodTest(TestAbstract test, Method method) {
        this(test);


        //common fields
        methodName = method.getName();
        returnType = method.getReturnType();
        declaringClass = method.getDeclaringClass();
        parameters = method.getParameterTypes();

        //throws exceptions
        if (method.getExceptionTypes().length > 0) {
            throwsExceptions = true;
            exceptionTypes = removeDuplicates(method.getExceptionTypes());
            exceptionTypes = Helpers.sortExceptions(exceptionTypes);
        }

        //has parameters 
        if (method.getParameterTypes().length > 0) {
            hasParameters = true;
        }


        //initialize
        this.initBody(buildMethodTestName());

        if (Modifier.isStatic(method.getModifiers())) {
            context = method.getDeclaringClass().getSimpleName();
        }

    }

    public MethodTest(TestAbstract test, Constructor constructor) {
        this(test);
        methodName = constructor.getName();
        declaringClass = constructor.getDeclaringClass();
        parameters = constructor.getParameterTypes();
        returnType = null;
        isConstructor = true;

        //throws exceptions
        if (constructor.getExceptionTypes().length > 0) {
            throwsExceptions = true;
            exceptionTypes = removeDuplicates(constructor.getExceptionTypes());
            exceptionTypes = Helpers.sortExceptions(exceptionTypes);
        }

        //has parameters 
        if (constructor.getParameterTypes().length > 0) {
            hasParameters = true;
        }

        //initizialize
        this.initBody(this.buildConstructorTestName());

    }

    //Obsidian Method Test Generation Algorithm
    public void build() {

        if (throwsExceptions && hasParameters) {
            addExceptionTestCaseIterator();
        } else if (hasParameters) {
            addTestCaseIterator();
        } else if (throwsExceptions) {
            addTry();
        } else {
            body.append("\t\t");
            body.append(buildMethodCall(FieldNames.TEST_CASES));
        }

        this.addAssertions();

        //Close Test Case Iterator
        if (hasParameters) {
            body.append("\n\t\t}");
        }

        //close
        body.append("\n\t");
        body.append("}");
        body.append("\n\t");
    }

    //#### Patch submitted by Micah Jenkins (student0208@gmail.com) 2/13/13
    public final String buildMethodTestName() {

        String returnName;

        String firstLetter = methodName.substring(0, 1);
        String newMethodName = firstLetter.toUpperCase() + methodName.substring(1);
        returnName = "test_" + newMethodName;

        int testNumber = 0;
        String currentName = returnName;
        while (testClass.methodNames.containsKey(returnName)) {
            returnName = currentName + "_" + testNumber;
            currentName = returnName.substring(0, returnName.lastIndexOf("_"));
            testNumber++;
        }

        testClass.methodNames.put(returnName, returnName);
        return returnName;
    }

    //SetUp
    String addMethodTestSetUp() {
        //String builder to build headerSB in
        StringBuilder headerSB = new StringBuilder();

        //declaration
        headerSB.append("\n\t");
        headerSB.append(JUnitConstants.TEST_ANNOTATION);
        headerSB.append("\n\t");
        headerSB.append("public ");
        headerSB.append("void ");
        headerSB.append(buildMethodTestName());
        headerSB.append("(){");

        //print statement
        headerSB.append("\n\t\t");
        headerSB.append("System.out.println(\"Testing ");
        headerSB.append(declaringClass.getSimpleName());
        headerSB.append(".");
        headerSB.append(methodName);
        headerSB.append("()\");");

        //Method Accountability
        headerSB.append("\n\t\t");
        headerSB.append(FieldNames.METHOD_MAP);
        headerSB.append(".");
        headerSB.append(MethodNames.METHOD_MAP_SET_TESTED);
        headerSB.append("(\"");
        headerSB.append(methodName);
        headerSB.append("\");");

        //fail Statement
        headerSB.append("\n\n\t\t");
        headerSB.append("//Fail Statement. Remove this after updating");
        headerSB.append("\n\t\t");
        headerSB.append(Content.FAIL_STATEMENT);

        return headerSB.toString();
    }

    //Block adding methods
    final void initBody(String testName) {
        //declaration
        body.append("\n\t");
        body.append(JUnitConstants.TEST_ANNOTATION);
        body.append("\n\t");
        body.append("public ");
        body.append("void ");
        body.append(testName);
        body.append("(){");

        //print statement
        body.append("\n\t\t");
        body.append("System.out.println(\"Testing ");
        body.append(testClass.getClassTestedSimpleName());
        body.append(".");
        body.append(methodName);
        body.append("()\");");

        //Method Accountability
        body.append("\n\t\t");
        body.append(FieldNames.METHOD_MAP);
        body.append(".");
        body.append(MethodNames.METHOD_MAP_SET_TESTED);
        body.append("(\"");
        body.append(methodName);
        body.append("\");");

        //fail Statement
        body.append("\n\n\t\t");
        body.append("//Fail Statement. Remove this after updating");
        body.append("\n\t\t");
        body.append(Content.FAIL_STATEMENT);
    }

    //Test Case Iterator
    final void addTestCaseIterator() {
        body.append("\n\n\t\t");
        body.append("//");
        body.append("\n\t\t");
        body.append("//Test Cases");
        body.append("\n\t\t");
        body.append("//");

        //if constructor only has one parameter build array of the parameter's
        //type 
        if (parameters.length == 1) {
            body.append("\n\t\t");
            body.append(parameters[0].getSimpleName());
            body.append("[][] testCases = {");
            body.append("\n\t\t\t{");
            body.append(testClass.buildConstructionStringFromType(
                    parameters[0], new Constructor[0]));
            body.append(", ");
            body.append(testClass.buildConstructionStringFromType(
                    parameters[0], new Constructor[0]));
            body.append("}");
            body.append("\n\t\t\t//  {");
            body.append(testClass.buildConstructionStringFromType(
                    parameters[0], new Constructor[0]));
            body.append(", ");
            body.append(testClass.buildConstructionStringFromType(
                    parameters[0], new Constructor[0]));
            body.append("}");
            body.append("\n\t\t");

        } else {
            body.append("\n\t\t//{");
            for (Class c : parameters) {
                body.append("\n\t\t//\t{ ");
                body.append(c.getSimpleName());
                body.append(" , ");
                body.append("Object");
                body.append(" }");
            }
            body.append("\n\t\t//}");
            body.append("\n\t\t");
            body.append("Object[][][] testCases = {");
            body.append("\n\t\t\t{");
            for (Class c : parameters) {
                body.append("\n\t\t\t\t{");
                body.append(testClass.buildConstructionStringFromType(c, new Constructor[0]));
                body.append(",");
                body.append(testClass.buildConstructionStringFromType(c, new Constructor[0]));
                body.append("},");
            }
            body.deleteCharAt(body.lastIndexOf(","));
            body.append("\n\t\t\t}");
            body.append("\n\t\t");
        }
        //close array
        body.append("};");

        //open for-loop
        body.append("\n\n\t\t");
        body.append("//For Each Test Case");
        body.append("\n\t\t");
        body.append("for(int ");
        body.append(FieldNames.TCI_VARIABLE);
        body.append(" = 0; ");
        body.append(FieldNames.TCI_VARIABLE);
        body.append(" < testCases.length; ");
        body.append(FieldNames.TCI_VARIABLE);
        body.append("++){");

        //add method methodCall
        if (throwsExceptions) {
            addTry();
        } else {
            body.append(buildMethodCall(FieldNames.TEST_CASES));
        }





//        //setter method methodCall
//        methodCall.append("\n\n\t\t\t");
//
//        methodCall.append("//Call Setter");
//        methodCall.append("\n\t\t\t");
//        if (Modifier.isStatic(setter.getModifiers())) {
//            methodCall.append(testClass.getClassTestedSimpleName());
//        } else {
//            methodCall.append(FieldNames.DEFAULT_INSTANCE_NAME);
//        }
//        methodCall.append(".");
//        methodCall.append(methodName);
//        methodCall.append("(testCases[");
//        methodCall.append(FieldNames.TCI_VARIABLE);
//        methodCall.append("][0]);");
//
//        //set value from getter methodCall
//        methodCall.append("\n\n\t\t\t");
//        methodCall.append("//Get Set Field With Getter");
//        methodCall.append("\n\t\t\t");
//        methodCall.append(parameter.getSimpleName());
//        methodCall.append(" ");
//        methodCall.append(FieldNames.RESULT);
//        methodCall.append(" = ");
//        methodCall.append(getGetterCallFromSetter(setter));
//        methodCall.append(";");
    }

    //Exception Test Case Iterator
    final void addExceptionTestCaseIterator() {

        for (Class exception : exceptionTypes) {
            //name of testCases array for this exception type
            String tCArrayName = exception.getSimpleName() + "TestCases";

            //Test Cases
            //if constructor only has one parameter build array of the parameter's
            //type 
            if (parameters.length == 1) {
                body.append("\n\n\t\t");
                body.append(parameters[0].getSimpleName());
                body.append("[][] ");
                body.append(tCArrayName);
                body.append(" = {");
                body.append("\n\t\t\t{");
                body.append(testClass.buildConstructionStringFromType(
                        parameters[0], new Constructor[0]));
                body.append(", ");
                body.append(testClass.buildConstructionStringFromType(
                        parameters[0], new Constructor[0]));
                body.append("}");
                body.append("\n\t\t\t//  {");
                body.append(testClass.buildConstructionStringFromType(
                        parameters[0], new Constructor[0]));
                body.append(", ");
                body.append(testClass.buildConstructionStringFromType(
                        parameters[0], new Constructor[0]));
                body.append("}");
                body.append("\n\t\t");

            } else {
                body.append("\n\n\t\t//{");
                for (Class c : parameters) {
                    body.append("\n\t\t//\t{ ");
                    body.append(c.getSimpleName());
                    body.append(" , ");
                    body.append("Object");
                    body.append(" }");
                }
                body.append("\n\t\t//{");
                body.append("\n\t\t");
                body.append("Object[][][] ");
                body.append(tCArrayName);
                body.append(" = {");
                body.append("\n\t\t\t{");
                for (Class c : parameters) {
                    body.append("\n\t\t\t\t{");
                    body.append(testClass.buildConstructionStringFromType(c, new Constructor[0]));
                    body.append(",");
                    body.append(testClass.buildConstructionStringFromType(c, new Constructor[0]));
                    body.append("},");
                }
                body.deleteCharAt(body.lastIndexOf(","));
                body.append("\n\t\t\t}");
                body.append("\n\t\t");
            }
            //close array
            body.append("};");


            //Open For Loop around setter body, getter body, and assertion
            body.append("\n\n\t\t");
            body.append("//For Each ");
            body.append(exception.getSimpleName());
            body.append(" Test Case");
            body.append("\n\t\t");
            body.append("for(int ");
            body.append(FieldNames.TCI_VARIABLE);
            body.append(" = 0; ");
            body.append(FieldNames.TCI_VARIABLE);
            body.append(" < ");
            body.append(tCArrayName);
            body.append(".length; ");
            body.append(FieldNames.TCI_VARIABLE);
            body.append("++){");

            if (returnType != null && (returnType.getSimpleName().compareToIgnoreCase("void") != 0)) {
                //declare actual result
                body.append("\n\n\t\t\t");
                body.append(returnType.getSimpleName());
                body.append(" ");
                body.append(FieldNames.ACTUAL_RESULT);
                body.append(" = ");
                body.append(testClass.buildConstructionStringFromType(returnType, new Constructor[0]));
                body.append(";");
            }

            //open try for setters that throw exceptions
            body.append("\n\n\t\t\ttry{");

            //method Call
            body.append(buildMethodCall(tCArrayName));

            //be sure expected exception was thrown
            body.append("\n\n\t\t\t\t");
            body.append("//If Expected Exception wasn't thrown");
            body.append("\n\t\t\t\t");
            body.append("fail(\"Expected Exception not thrown.\"");
            body.append("\n\t\t\t\t\t");
            body.append("+ \" Test case #\" + (");
            body.append(FieldNames.TCI_VARIABLE);
            body.append(" + 1) + \" : \"");
            body.append("\n\t\t\t\t\t");
            body.append("+ ");
            body.append(tCArrayName);
            body.append("[");
            body.append(FieldNames.TCI_VARIABLE);
            body.append("][0]");

//            //only add .toString() if not primitive
//            if (Helpers.PRIMITIVE_CONSTRUCTIONS.get(parameter.toString()) == null) {
//                body.append(".toString()");
//            }
            body.append(");");

            //#### Patch Submitted by Joanna Illing (illing.joanna@gmail.com) 2/13/13
            //Close Try Block open catch
            body.append("\n\n\t\t\t");
            body.append("}catch(Throwable thr){");


            //catchblock

            //Expected Exceptions

            body.append("\n\t\t\t\t");
            body.append("if(!(");
            body.append(testClass.getTestClassName());
            body.append(".areEqual(thr,");
            body.append(testClass.buildConstructionStringFromType(exception, new Constructor[0]));
            body.append("))){");

            body.append("\n\n\t\t\t\t\t");
            body.append("fail(\"Expected: \" + \"").append(
                    exception.getSimpleName()).append(
                    "\"+ \n\t\t\t\t\t\" Got: \"+ thr.getClass().getSimpleName());");
            body.append("\n\t\t\t\t");
            body.append("}");

            //Close catch
            body.append("\n\t\t\t");
            body.append("}");

            //Close For-Loop
            body.append("\n\t\t}");
        }

        this.addTestCaseIterator();
    }

    final void addTry() {

        int tabDepth = 1;

        if (throwsExceptions) {
            tabDepth += 1;
        }
        if (hasParameters) {
            tabDepth += 1;
        }

        //build base tabs
        String baseTabs = "";
        for (int i = 0; i < tabDepth; i++) {
            baseTabs = baseTabs + "\t";
        }

        if (returnType != null && (returnType.getSimpleName().compareToIgnoreCase("void") != 0)) {
            //declare actual result
            body.append("\n\n").append(baseTabs);
            body.append(returnType.getSimpleName());
            body.append(" ");
            body.append(FieldNames.ACTUAL_RESULT);
            body.append(" = ");
            body.append(testClass.buildConstructionStringFromType(returnType, new Constructor[0]));
            body.append(";");
        }

        //build try
        body.append("\n\n").append(baseTabs);
        body.append("try{");
        //returnBlock.append("\n\t\t");
        body.append(buildMethodCall(FieldNames.TEST_CASES));
        body.append("\n\n").append(baseTabs).append("}");

        //build catch blocks
        for (Class e : exceptionTypes) {
            testClass.appendDynamicImports(e);
            body.append("catch(");
            body.append(e.getSimpleName());
            body.append(" ex){");
            body.append("\n").append(baseTabs).append("}");
        }


    }

    final String buildMethodCall(String testCaseArray) {

        String modularTabs = "";

        if (exceptionTypes.length > 0 && parameters.length > 0) {
            modularTabs = "\t\t";
        } else if (exceptionTypes.length > 0 || parameters.length > 0) {
            modularTabs = "\t";
        }

        StringBuilder methodCall = new StringBuilder();
        //Beginning Tabs
        methodCall.append("\n\n\t\t");
        methodCall.append(modularTabs);

        //comment
        methodCall.append("//Method Call");
        methodCall.append("\n\t\t");
        methodCall.append(modularTabs);

        if (!isConstructor) {
            if (!(returnType.toString().compareToIgnoreCase("void") == 0)) {

                testClass.appendDynamicImports(returnType);

                //import simple methodName
                if (!throwsExceptions) {
                    methodCall.append(returnType.getSimpleName());
                    methodCall.append(" ");
                }

                methodCall.append(FieldNames.ACTUAL_RESULT);
                methodCall.append(" = ");
            }

            //context
            methodCall.append(context);
            methodCall.append(".");

            //methodName and open paren
            methodCall.append(methodName);
            methodCall.append("(");
        } else {
            methodCall.append(context);
            methodCall.append(" = new ");
            methodCall.append(declaringClass.getSimpleName());
            methodCall.append("(");

        }



        if (parameters.length == 1) {
            methodCall.append(testCaseArray);
            methodCall.append("[");
            methodCall.append(FieldNames.TCI_VARIABLE);
            methodCall.append("][0]");
        } else {
            for (int i = 0; i < parameters.length; i++) {
                methodCall.append("//\n\t\t\t");
                methodCall.append(modularTabs);
                methodCall.append("(");
                if (parameters[i].isPrimitive()) {
                    methodCall.append(Helpers.PRIMITIVE_OBJECT_NAMES.get(parameters[i].getSimpleName()));
                } else {
                    methodCall.append(parameters[i].getSimpleName());
                }
                methodCall.append(")");
                methodCall.append(testCaseArray);
                methodCall.append("[");
                methodCall.append(FieldNames.TCI_VARIABLE);
                methodCall.append("][");
                methodCall.append(i);
                methodCall.append("][0],");
            }
            try {
                methodCall.deleteCharAt(methodCall.lastIndexOf(","));
            } catch (Exception x) {
            }
        }
        methodCall.append(");");




        return methodCall.toString();
    }

    final String buildTestCaseArray() {

        StringBuilder testCaseArray = new StringBuilder();
        //if constructor only has one parameter build array of the parameter's
        //type 
        if (parameters.length == 1) {
            testCaseArray.append("\n\t\t");
            testCaseArray.append(parameters[0].getSimpleName());
            testCaseArray.append("[][] testCases = {");
            testCaseArray.append("\n\t\t\t{");
            testCaseArray.append(testClass.buildConstructionStringFromType(
                    parameters[0], new Constructor[0]));
            testCaseArray.append(", ");
            testCaseArray.append(testClass.buildConstructionStringFromType(
                    parameters[0], new Constructor[0]));
            testCaseArray.append("}");
            testCaseArray.append("\n\t\t\t//  {");
            testCaseArray.append(testClass.buildConstructionStringFromType(
                    parameters[0], new Constructor[0]));
            testCaseArray.append(", ");
            testCaseArray.append(testClass.buildConstructionStringFromType(
                    parameters[0], new Constructor[0]));
            testCaseArray.append("}");
            testCaseArray.append("\n\t\t");

        } else {
            testCaseArray.append("\n\t\t//{");
            for (Class c : parameters) {
                testCaseArray.append("\n\t\t//\t{ ");
                testCaseArray.append(c.getSimpleName());
                testCaseArray.append(" , ");
                testCaseArray.append("Object");
                testCaseArray.append(" }");
            }
            testCaseArray.append("\n\t\t//{");
            testCaseArray.append("\n\t\t");
            testCaseArray.append("Object[][][] testCases = {");
            testCaseArray.append("\n\t\t\t{");
            for (Class c : parameters) {
                testCaseArray.append("\n\t\t\t\t{");
                testCaseArray.append(testClass.buildConstructionStringFromType(c, new Constructor[0]));
                testCaseArray.append(",");
                testCaseArray.append(testClass.buildConstructionStringFromType(c, new Constructor[0]));
                testCaseArray.append("},");
            }
            testCaseArray.deleteCharAt(testCaseArray.lastIndexOf(","));
            testCaseArray.append("\n\t\t\t}");
            testCaseArray.append("\n\t\t");
        }
        //close array
        testCaseArray.append("};");

        return testCaseArray.toString();
    }


    //#### Patch Submitted by Micah Jenkins (student0208@gmail.com) 2/13/13
    public final String buildConstructorTestName() {
        //string to be returned 
        String constructorName;

        int currentConstructorTestNumber = testClass.getCurrentConstructorTestNumber();


        constructorName = ("test_constructor_"
                + (currentConstructorTestNumber - 1));

        testClass.setCurrentConstructorTestNumber(currentConstructorTestNumber + 1);

        return constructorName;
    }

    public static Class[] removeDuplicates(Class[] array) {
        ArrayList<Class> temp = new ArrayList<Class>();

        for (Class c : array) {
            if (!temp.contains(c)) {
                temp.add(c);
            }
        }

        Class[] retVal = new Class[temp.size()];

        for (int i = 0; i < temp.size(); i++) {
            retVal[i] = temp.get(i);
        }

        return retVal;
    }

    abstract void addAssertions();
}
