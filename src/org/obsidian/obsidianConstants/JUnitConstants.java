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

package org.obsidian.obsidianConstants;

import java.lang.reflect.Method;
import java.util.ArrayList;
import org.obsidian.test.TestAbstract;
import org.obsidian.util.MethodSorter;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 * 
 * Contributors:
 *
 */
public class JUnitConstants {
    //test annotation

    public static final String TEST_ANNOTATION = "@Test";
    //setUp Class Annotation
    public static final String SETUP_CLASS_ANNOTATION = "@BeforeClass";
    //tearDown Class Annotation
    public static final String TEARDOWN_CLASS_ANNOTATION = "@AfterClass";
    //setup annotation
    public static final String SETUP_ANNOTATION = "@Before";
    //tearDown annotation
    public static final String TEARDOWN_ANNOTATION = "@After";
    //assertions
    public static final String PRIMITIVE_ASSERT_EQUALS = "assertEquals";
    public static final String ASSERT_TRUE = "assertTrue";
    
    private static int headerLength = 0;
    
    //
    //
    //
    //************************
    //************************
    //**** JUnit Builders ****
    //************************ 
    //************************
    //
    //
    //
    public static String buildSetUpClass(TestAbstract test) {
        StringBuilder setUpClass = new StringBuilder();
        
        Class c = test.getClassTested();

        ArrayList<Method> privateMethods = MethodSorter.getPrivateMethods(
                test.getClassTested().getDeclaredMethods());

        setUpClass.append("\n\n\t");
        setUpClass.append(JUnitConstants.SETUP_CLASS_ANNOTATION);
        setUpClass.append("\n\t");
        setUpClass.append("public static void SetUpClass() throws Exception {");
        setUpClass.append("\n");
        setUpClass.append("\n\t\t");

        //Header
        int inSet = 20;//number of * before and after class name dataInputStream header

        headerLength = c.getSimpleName().length() + (inSet * 2) + 2;

        setUpClass.append("System.out.println(\"");
        for (int i = 0; i < headerLength; i++) {
            setUpClass.append("#");
        }
        setUpClass.append("\");");
        setUpClass.append("\n\t\t");
        setUpClass.append("System.out.println(\"");
        for (int i = 0; i < inSet; i++) {
            setUpClass.append("#");
        }
        setUpClass.append(" ");
        setUpClass.append(c.getSimpleName());
        setUpClass.append(" ");
        for (int i = 0; i < inSet; i++) {
            setUpClass.append("#");
        }
        setUpClass.append("\");");
        setUpClass.append("\n\t\t");
        setUpClass.append("System.out.println(\"");
        for (int i = 0; i < c.getSimpleName().length() + (inSet * 2) + 2; i++) {
            setUpClass.append("#");
        }
        setUpClass.append("\");");


//        //Method Map
//        setUpClass.append("\n\n\t\t");
//        setUpClass.append(FieldNames.METHOD_MAP);
//        setUpClass.append(" = new MethodMap(\"");
//        setUpClass.append(passedClass.getCanonicalName());
//        setUpClass.append("\");");

        //Method Map
        setUpClass.append("\n\n\t\t");
        setUpClass.append(FieldNames.METHOD_MAP);
        setUpClass.append(" = new MethodMap(");
        setUpClass.append(c.getSimpleName());
        setUpClass.append(".class);");

        //Declare Ignored Methods
        setUpClass.append("\n\n\t\t");
        setUpClass.append("//Declare Ignored Methods");
        setUpClass.append("\n\t\t");
        setUpClass.append(FieldNames.METHOD_MAP);
        setUpClass.append(".setIgnored(\"\");");

        //privateMethods set ignored commented out
        for (Method m : privateMethods) {
            setUpClass.append("\n\t\t//");
            setUpClass.append(FieldNames.METHOD_MAP);
            setUpClass.append(".setIgnored(\"");
            setUpClass.append(m.getName());
            setUpClass.append("\");");
        }

//        getterTest.append("\n");
//        getterTest.append("\n\t\t");
//        getterTest.append("myHtmlFormatter = new MyHtmlFormatter();");
//        getterTest.append("\n\t\t");
//        getterTest.append("myHtmlFormatter.setClassTested(\"");
//        getterTest.append(passedClass.getName());
//        getterTest.append("\");");
//        getterTest.append("\n");
//        getterTest.append("\n\t\t");
//
//        getterTest.append("filehandler = new FileHandler(\"Log.html\");");
//        getterTest.append("\n\t\t");
//        getterTest.append("filehandler.setFormatter(myHtmlFormatter);");
//        getterTest.append("\n");
//        getterTest.append("\n\t\t");

//        getterTest.append("logger.setLevel(Level.FINEST);");
//        getterTest.append("\n\t\t");
//        getterTest.append("logger.addHandler(filehandler);");
//        getterTest.append("\n");
        setUpClass.append("\n\t\t");

        setUpClass.append("}");

        return setUpClass.toString();
    }

    public static String buildTearDownClass() {
        StringBuilder test = new StringBuilder();

        test.append("\n\n\t");
        test.append(JUnitConstants.TEARDOWN_CLASS_ANNOTATION);
        test.append("\n\t");
        test.append("public static void tearDownClass() throws Exception{");
        test.append("\n\t\t");
        test.append("System.out.println(\"");
        for (int i = 0; i < headerLength; i++) {
            test.append("#");
        }
        test.append("\");\n\t\t");
        test.append("System.out.println(\"");
        for (int i = 0; i < headerLength; i++) {
            test.append("#");
        }
        test.append("\");\n\t\t");
        test.append("System.out.println(\"\\n");
        for (int i = 0; i < headerLength; i++) {
            test.append("-");
        }
        test.append("\");\n\t");
        test.append("}");
        return test.toString();
    }

    public static String buildSetUp(TestAbstract ta) {
        StringBuilder setUP = new StringBuilder();

        setUP.append("\n\n\t");
        setUP.append(JUnitConstants.SETUP_ANNOTATION);
        setUP.append("\n\t");
        setUP.append("public void setUp(){");
        setUP.append("\n\n\t\t");
        setUP.append("//prepare instance under test");
        setUP.append("\n\t\t");
        setUP.append(ta.buildDefaultInstance());
        setUP.append("\n\t");
        setUP.append("}");

        return setUP.toString();
    }

    public static String buildTearDown() {
        StringBuilder test = new StringBuilder();

        test.append("\n\n\t");
        test.append(JUnitConstants.TEARDOWN_ANNOTATION);
        test.append("\n\t");
        test.append("public void tearDown(){");
        test.append("\n\t");
        test.append("}");

        return test.toString();
    }

    
}
