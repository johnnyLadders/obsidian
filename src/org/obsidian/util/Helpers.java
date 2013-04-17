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

package org.obsidian.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import org.obsidian.Main;
import org.obsidian.obsidianConstants.JUnitConstants;
import org.obsidian.obsidianConstants.MethodNames;
import org.obsidian.test.TestAbstract;
import org.obsidian.obsidianConstants.ClassNames;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public class Helpers {

    public static final String[] NUMBER_WORDS = {"One",
        "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
        "Seventeen", "Eighteen", "Nineteen", "Twenty"
    };
    public static final String[] ORDINAL_NUMBERS = {"First",
        "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth", "Tenth",
        "Eleventh", "Twelfth", "Thirteenth", "Fourteenth", "Fifteenth", "Sixteenth",
        "Seventeenth", "Eighteenth", "Nineteenth", "Twentieth"
    };
    public static final HashMap<String, String> PRIMITIVE_CONSTRUCTIONS =
            new HashMap<String, String>() {
            };

    static {
        PRIMITIVE_CONSTRUCTIONS.put("String", "\"\"");
        PRIMITIVE_CONSTRUCTIONS.put("int", "0");
        PRIMITIVE_CONSTRUCTIONS.put("byte", "0");
        PRIMITIVE_CONSTRUCTIONS.put("short", "0");
        PRIMITIVE_CONSTRUCTIONS.put("long", "0L");
        PRIMITIVE_CONSTRUCTIONS.put("float", "0.0f");
        PRIMITIVE_CONSTRUCTIONS.put("double", "0.0d");
        PRIMITIVE_CONSTRUCTIONS.put("boolean", "false");
        PRIMITIVE_CONSTRUCTIONS.put("char", "\'\u0000\'");
        PRIMITIVE_CONSTRUCTIONS.put("BigDecimal", "new BigDecimal(\"0\")");
    }
    public static final HashMap<String, String> PRIMITIVE_OBJECT_NAMES =
            new HashMap<String, String>();

    static {
        PRIMITIVE_OBJECT_NAMES.put("String", "String");
        PRIMITIVE_OBJECT_NAMES.put("int", "Integer");
        PRIMITIVE_OBJECT_NAMES.put("byte", "Byte");
        PRIMITIVE_OBJECT_NAMES.put("short", "Short");
        PRIMITIVE_OBJECT_NAMES.put("long", "Long");
        PRIMITIVE_OBJECT_NAMES.put("float", "Float");
        PRIMITIVE_OBJECT_NAMES.put("double", "Double");
        PRIMITIVE_OBJECT_NAMES.put("boolean", "Boolean");
        PRIMITIVE_OBJECT_NAMES.put("char", "Character");
    }

    public static <T> Class getConcreteSubstitution(Class<T> type) {
        for (Class c : Main.classes) {

            if (!Modifier.isAbstract(c.getModifiers()) && !c.isInterface()) {
                //immediate super class
                Class superClass = c.getSuperclass();

                if (superClass != null) {
                    while (!superClass.equals(Object.class)) {//while still has superclasses

                        if (superClass.equals(type)) {
                            type = c;
                        }
                        superClass = superClass.getSuperclass();
                    }
                }
            }

        }

        return type;
    }

    public static ArrayList<Constructor> notVisitedConstructors(Constructor[] constructors,
            Constructor[] visitedConstructors) {



        ArrayList<Constructor> notVisited = new ArrayList<Constructor>();
        for (int i = 0; i < constructors.length; i++) {
            boolean found = false;
            for (int j = 0; j < visitedConstructors.length; j++) {
                if (!(constructors[i].equals(visitedConstructors[j]))) {
                    found = true;
                }
            }
            if (!found) {
                notVisited.add(constructors[i]);
            }

        }

        return notVisited;
    }

    public static <T> Constructor getConstructorWithLeastParametersFromList(
            ArrayList<Constructor> constructorsFromType) {

        Constructor planBConstructor = Object.class.getDeclaredConstructors()[0];
        Constructor retVal;

        if (!constructorsFromType.isEmpty()) {

            //initially assume that first constructor is best
            int index = 0;
            retVal = constructorsFromType.get(index);
            index++;

            //look for constructor that does not throw exceptions
            while (retVal.getExceptionTypes().length > 0
                    && index < constructorsFromType.size()) {
                retVal = constructorsFromType.get(index);
                index++;
            }

            if (!(retVal.getExceptionTypes().length > 0)) {
                //Try to find constructor with less number of parameters
                for (int i = 1; i < constructorsFromType.size(); i++) {
                    Constructor next = constructorsFromType.get(i);
                    if (next.getParameterTypes().length < retVal.getParameterTypes().length) {
                        retVal = next;

                    }
                }
            }



        } else {

            retVal = planBConstructor;
        }

//        if (retVal.getParameterTypes().length == 1 && retVal.getParameterTypes()[0] == type) {
//            retVal = ArrayList.class.getDeclaredConstructors()[0];
//
//        }




        return retVal;
    }

    public static Constructor[] addConstructor(Constructor[] constructors, Constructor constructor) {
        Constructor[] retVal = new Constructor[constructors.length + 1];
        System.arraycopy(constructors, 0, retVal, 0, constructors.length);
        retVal[retVal.length - 1] = constructor;

        return retVal;
    }

    public static Boolean typeHasDefaultConstructor(Class type) {
        Boolean retVal = false;

        for (Constructor c : type.getConstructors()) {
            if (c.getParameterTypes().length == 0) {
                retVal = true;
            }
        }

        return retVal;
    }

    public static String getDefalutValueFieldName(Field f) {
        String name = f.getName();

        return name + "DefaultValue";
    }

    public static <T> String buildEqualityAssertion(Class<T> type,
            String valueOne, String valueTwo, int tabDepth) {
        StringBuilder assertion = new StringBuilder();
        

        if (type.isPrimitive()) {
            assertion.append(ClassNames.GLOBAL_EQUALITY_METHODS);
            assertion.append(".");
            assertion.append(MethodNames.EQUALITY_METHOD_NAME);
            assertion.append("(// ");
            assertion.append("\n");
            for (int i = 0; i <= tabDepth; i++) {
                assertion.append("\t");
            }
            assertion.append(valueOne);
            assertion.append(" ,//\n");
            for (int i = 0; i <= tabDepth; i++) {
                assertion.append("\t");
            }
            assertion.append(valueTwo);
        } else {
            assertion.append(MethodNames.EQUALITY_METHOD_NAME);
            assertion.append("(//\n");
            for (int i = 0; i <= tabDepth; i++) {
                assertion.append("\t");
            }
            assertion.append(valueOne);
            assertion.append(" ,//\n");
            for (int i = 0; i <= tabDepth; i++) {
                assertion.append("\t");
            }
            assertion.append(valueTwo);


        }

        assertion.append(");");


        return assertion.toString();
    }

    public static String buildFieldReflectionMethod(String classTestedSimpleName) {
        StringBuilder fieldReflectionMethod = new StringBuilder();
        fieldReflectionMethod.append("\n\tprivate static <T> Object fieldReflection(");
        fieldReflectionMethod.append(classTestedSimpleName);
        fieldReflectionMethod.append(" instance,//\n\t\t\tString fieldName) throws//");
        fieldReflectionMethod.append("\n\t\t\tNoSuchFieldException,"
                + "\n\t\t\tIllegalArgumentException,\n\t\t\tIllegalAccessException {");
        fieldReflectionMethod.append("\n\n\t\tField field = instance.getClass()."
                + "getDeclaredField(fieldName);");
        fieldReflectionMethod.append("\n\t\tfield.setAccessible(true);");
        fieldReflectionMethod.append("\n\t\treturn field.get(instance);");
        fieldReflectionMethod.append("\n\n\t}");

        return fieldReflectionMethod.toString();
    }

    public static <T> String buildTwoDimensionalTestCaseArrayFromType(TestAbstract test,
            Class<T> type, int tabDepth) {
        StringBuilder testCases = new StringBuilder();

        //type construction
        String construction = test.buildConstructionStringFromType(type, new Constructor[0]);

        //open
        testCases.append("{\n");

        //First testCase
        for (int i = 0; i <= tabDepth; i++) {
            testCases.append("\t");
        }
        testCases.append("{");
        testCases.append(construction);
        testCases.append(", ");
        testCases.append(construction);
        testCases.append("}\n");

        //commented out getterTest case
        for (int i = 0; i <= tabDepth; i++) {
            testCases.append("\t");
        }
        testCases.append("//  {");
        testCases.append(construction);
        testCases.append(", ");
        testCases.append(construction);
        testCases.append("}\n");

        //close
        for (int i = 0; i < tabDepth; i++) {
            testCases.append("\t");
        }
        testCases.append("};");

        return testCases.toString();
    }

    public static ArrayList<String> sortImports(ArrayList<String> imports) {
        ArrayList<String> javaImports = new ArrayList<String>();
        ArrayList<String> javaxImports = new ArrayList<String>();
        ArrayList<String> orgImports = new ArrayList<String>();
        ArrayList<String> comImports = new ArrayList<String>();
        ArrayList<String> miscImports = new ArrayList<String>();

        for (String s : imports) {
            if (s.startsWith("java")) {
                javaImports.add(s);
            } else if (s.startsWith("javax")) {
                javaxImports.add(s);
            } else if (s.startsWith("org")) {
                orgImports.add(s);
            } else if (s.startsWith("com")) {
                comImports.add(s);
            } else {
                miscImports.add(s);
            }
        }

        ArrayList<String> sortedImports = new ArrayList<String>();
        //add sorted lists to sorted imports in correct order
        //com
        sortedImports.addAll(comImports);
        //java
        sortedImports.addAll(javaImports);
        //javax
        sortedImports.addAll(javaxImports);
        //org
        sortedImports.addAll(orgImports);
        //everything else
        sortedImports.addAll(miscImports);



        return sortedImports;
    }
    
        //
    //Field Reflection
    //
    public static <T> Object fieldReflection(Object instance,//
            String fieldName) {

        Field field = null;
        Object retVal = null;

        try {

            field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            retVal = field.get(instance);

        } catch (IllegalArgumentException ex) {
            
        } catch (IllegalAccessException ex) {
            
        } catch (NoSuchFieldException ex) {
            
        } catch (SecurityException ex) {
            
        }

        return retVal;

    }
    public static boolean isExceptionSuperclass(Class c1, Class c2){
        if (c1.equals(c2.getSuperclass())){
            return true;
}
        else {
            return false;
        }
    }
    
    public static Class[] sortExceptions(Class[] exceptions){
        Class[] returnExceptions = new Class[exceptions.length];
        ArrayList<Class> sortedExcArrayList = new ArrayList<Class>();
        
        if (sortedExcArrayList.isEmpty()){
                    sortedExcArrayList.add(exceptions[0]);
        }
        for (int i = 1; i < exceptions.length; i++){
            boolean added = false;
            for (int j = 0; j < sortedExcArrayList.size(); j++){
                if (isExceptionSuperclass(sortedExcArrayList.get(j), exceptions[i])){
                    sortedExcArrayList.add(j, exceptions[i]);
                    added = true;
                    break;
                }
            }
            if (!added) {
                sortedExcArrayList.add(exceptions[i]);
            }
        }
        for(int k = 0; k < sortedExcArrayList.size(); k++){
            returnExceptions[k] = sortedExcArrayList.get(k);
        }
        return returnExceptions;
    }
}
