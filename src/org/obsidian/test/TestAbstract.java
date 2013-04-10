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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.obsidian.obsidianConstants.ClassNames;
import org.obsidian.obsidianConstants.Content;
import org.obsidian.obsidianConstants.FieldNames;
import org.obsidian.obsidianConstants.MethodNames;
import org.obsidian.util.Helpers;
import org.obsidian.util.MethodSorter;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public abstract class TestAbstract {

    String classTestedSimpleName;
    String testClassName;
    //
    static Class classTested;
    //
    ArrayList<String> dynamicImports = new ArrayList<String>();
    ArrayList<Class> equalityMethodTypes = new ArrayList<Class>();
    ArrayList<String> packageEqualityMethodTypes = new ArrayList<String>();
    public HashMap<String, String> methodNames = new HashMap<String, String>();
    //
    Constructor[] constructors;
    ArrayList<Method> getters;
    ArrayList<Method> setters;
    ArrayList<Method> remainingMethods;    
    //
    private int currentConstructorTestNumber = 1;
    //
    StringBuilder body;

    public TestAbstract(Class classToTest) {
        //class object to test
        TestAbstract.classTested = classToTest;

        //class tested simple name
        classTestedSimpleName = classToTest.getSimpleName();

        //name of test class
        testClassName = classTestedSimpleName + "_O_Test";
        
        //populate method lists
        Method[] methods = classToTest.getDeclaredMethods();
        constructors = classToTest.getConstructors();
        getters = MethodSorter.getGetters(methods);
        setters = MethodSorter.getSetters(methods);
        remainingMethods = MethodSorter.getRemainingMethods(methods);

        //init body
        body = new StringBuilder();
    }

    //The method that pieces the class together
    public abstract void build();

    String buildDefaultValueFields() {
        StringBuilder defaultValueFields = new StringBuilder();

        Field[] fields = classTested.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {

            //add to imports
            appendDynamicImports(fields[i].getType());


            defaultValueFields.append("\n\n\t//");
            defaultValueFields.append(fields[i].getName());

            defaultValueFields.append("\n\tprivate static ");
            defaultValueFields.append(fields[i].getType().getSimpleName());
            defaultValueFields.append(" ");
            defaultValueFields.append(FieldNames.getDefalutValueFieldName(fields[i]));
            defaultValueFields.append(" = ");
            defaultValueFields.append(buildConstructionStringFromType(fields[i].getType(), new Constructor[0]));
            defaultValueFields.append(";");
        }


        return defaultValueFields.toString();
    }

    public <T> void appendDynamicImports(Class<T> classToImport) {
        boolean shouldBeIgnored = false;

        //Make sure: not already in dynamic imports
        for (String s : getDynamicImports()) {
            if (s.compareToIgnoreCase(classToImport.getName().replaceAll("\\$", ".")) == 0) {
                shouldBeIgnored = true;
            }
        }

        if (!classToImport.isPrimitive() && !classToImport.isArray()) {
            //make sure: not in imports from constants
            for (String s : Content.IMPORTS) {
                if (s.compareToIgnoreCase(classToImport.getName()) == 0) {
                    shouldBeIgnored = true;
                }

            }

            //make sure: not in same package
            if (classToImport.getPackage().toString().compareToIgnoreCase(
                    classTested.getPackage().toString()) == 0) {
                
                //#### Patch Submitted by Michael Cole (micole.3@gmail.com) 2/13/13
                if (!(classToImport.isMemberClass() || classToImport.isEnum()
                        || classToImport.isLocalClass())) {
                shouldBeIgnored = true;
                }

            }
            
            //make sure not private
            if(Modifier.isPrivate(classToImport.getModifiers())){
                shouldBeIgnored = true;
            }

            //make sure: not importing from java.lang unless at least 3 deep 
            //ex)java.lang.reflect
            String[] packageStructure = classToImport.getPackage().getName().split("\\.");
            int packageDepth = packageStructure.length;

            //if dataInputStream java.lang
            if (packageStructure[0].compareToIgnoreCase("java") == 0
                    && packageStructure[1].compareToIgnoreCase("lang") == 0) {

                //and less than three deep
                if (packageDepth < 3) {
                    shouldBeIgnored = true;
                    classToImport.getName();
                }
            }
        } else {
            shouldBeIgnored = true;
            if (classToImport.isArray() && !classToImport.getComponentType().isPrimitive()) {
                appendDynamicImports(classToImport.getComponentType());
            }
        }




        //if: not already in imports and not in same package
        if (!shouldBeIgnored) {
            //add to dynamic imports
            String importName = classToImport.getName();
            importName = importName.replaceAll("\\$", ".");
            getDynamicImports().add(importName);
        }
    }

    public <T> String buildConstructionStringFromType(Class<T> type, Constructor[] visitedConstructors) {


        //initialize construction String 
        String constructionString;

        //append equality method types
        appendEqualityMethodTypes(type);

        //append the type to the getterTest's dynamic imports
        appendDynamicImports(type);


        //if class is abstract, replace with concrete substitution
        if (Modifier.isAbstract(type.getModifiers()) || type.isInterface()) {
            type = Helpers.getConcreteSubstitution(type);
        }

        //type's simple name
        String name = type.getSimpleName();

        //append the type to the getterTest's dynamic imports
        appendDynamicImports(type);


        if (Helpers.PRIMITIVE_CONSTRUCTIONS.get(name) != null) {
            //get construction from PRIMITIVE_CONSTRUCTIONS
            constructionString = Helpers.PRIMITIVE_CONSTRUCTIONS.get(name);
        } else if (type.isArray()) {
            int numberOfDimensions = StringUtils.countMatches(name, "[]");

            constructionString = name.replace("[]", "");

            for (int i = 0; i < numberOfDimensions; i++) {
                constructionString = constructionString + "[0]";
            }

            constructionString = "new " + constructionString;


        } else if (Modifier.isAbstract(type.getModifiers()) || type.isInterface()) {
            constructionString = "null";
        } else if (type.getConstructors().length == 0) {
            constructionString = "null";
        } else {
            //not visited constructors
            ArrayList<Constructor> NVC = Helpers.notVisitedConstructors(type.getConstructors(), visitedConstructors);

            Constructor constructor = Helpers.getConstructorWithLeastParametersFromList(NVC);

            if (NVC.isEmpty()) {
                constructionString = "null";
            } else if (constructor.getExceptionTypes().length > 0) {
                constructionString = "null";
            } else {

                visitedConstructors = Helpers.addConstructor(visitedConstructors, constructor);
                constructionString = "new " + name + "(";
                Class[] parameters = constructor.getParameterTypes();
                for (int i = 0; i < parameters.length; i++) {
                    constructionString = constructionString + buildConstructionStringFromType(parameters[i], visitedConstructors) + ",";
                }
                if (parameters.length != 0) {
                    constructionString = constructionString.substring(0, constructionString.length() - 1);
                }
                constructionString = constructionString + ")";
            }
        }

        //this will prevent ambiguity in constructors with parmeter that 
        //cannot be constructed
        if (constructionString.contains("null")) {
            constructionString = "null";
        }

        return constructionString;
    }

    public <T> void appendEqualityMethodTypes(Class<T> type) {
        Boolean ignore = false;



        //Make sure: not already in Equality Method Types
        for (Class c : getEqualityMethodTypes()) {
            if (type.toString().compareToIgnoreCase(c.toString()) == 0) {
                ignore = true;
            }
        }


        //Make sure: not a Throwable
        Class superClass = type.getSuperclass();//first check immediate super


        if (superClass != null) {
            while (!superClass.equals(Object.class)) {//while still has superclasses

                //#### Patch Submitted by Joanna Illing (illing.joanna@gmail.com) 2/13/13
                if (superClass.equals(Throwable.class)) {
                    ignore = true;//if Throwable: ignore
                }
                superClass = superClass.getSuperclass();
            }





        }

        if (type.getSimpleName().compareToIgnoreCase("string") == 0) {
            ignore = true;
        }

        if (type.getSimpleName().compareToIgnoreCase("Exception") == 0) {
            ignore = true;
        }
        
        if (type.getSimpleName().compareToIgnoreCase("Throwable") == 0) {
            ignore = true;
        }
        

        if (Modifier.isStatic(type.getModifiers()) && type.getSuperclass() != null) {
            appendDynamicImports(type.getSuperclass());
        }

        if (!ignore) {
            getEqualityMethodTypes().add(type);
        }


    }

    public Class getClassTested() {
        return classTested;
    }

    public String buildDefaultInstance() {
        StringBuilder defaultInstance = new StringBuilder();

        defaultInstance.append("instance =");

        if (Helpers.typeHasDefaultConstructor(classTested)) {
            defaultInstance.append(" new ");
            defaultInstance.append(classTested.getSimpleName());
            defaultInstance.append("();");
        } else {
            defaultInstance.append(" null;");
        }

        return defaultInstance.toString();
    }

    /**
     * @return the currentConstructorTestNumber
     */
    public int getCurrentConstructorTestNumber() {
        return currentConstructorTestNumber;
    }

    /**
     * @param currentConstructorTestNumber the currentConstructorTestNumber to set
     */
    public void setCurrentConstructorTestNumber(int currentConstructorTestNumber) {
        this.currentConstructorTestNumber = currentConstructorTestNumber;
    }

    /**
     * @return the classTestedSimpleName
     */
    public String getClassTestedSimpleName() {
        return classTestedSimpleName;
    }

    /**
     * @param classTestedSimpleName the classTestedSimpleName to set
     */
    public void setClassTestedSimpleName(String classTestedSimpleName) {
        this.classTestedSimpleName = classTestedSimpleName;
    }

    /**
     * @return the testClassName
     */
    public String getTestClassName() {
        return testClassName;
    }

    /**
     * @param testClassName the testClassName to set
     */
    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }
    
    static <T> String buildTestClassEqualityMethodFromType(Class<T> type) {
        StringBuilder equalityMethod = new StringBuilder();

        String valueOne = "valueOne";
        String valueTwo = "valueTwo";
        String simpleName = type.getSimpleName();

        //declaration
        equalityMethod.append("\n\n\t");
        equalityMethod.append("//");
        equalityMethod.append(simpleName); 
       equalityMethod.append("\n\t");
        equalityMethod.append("private static boolean "
                + MethodNames.EQUALITY_METHOD_NAME + " ( ");
        equalityMethod.append(simpleName);
        equalityMethod.append(" ");
        equalityMethod.append(valueOne);
        equalityMethod.append(", ");
        equalityMethod.append(simpleName);
        equalityMethod.append(" ");
        equalityMethod.append(valueTwo);
        equalityMethod.append("){");

        //commented out .equals method
        equalityMethod.append("\n\n\t\t//return ");
        equalityMethod.append(valueOne);
        equalityMethod.append(".equals(");
        equalityMethod.append(valueTwo);
        equalityMethod.append(");");

        //return
        equalityMethod.append("\n\t\treturn ");
        equalityMethod.append(ClassNames.PACKAGE_EQUALITY_METHODS);
        equalityMethod.append(".areEqual(");
        equalityMethod.append(valueOne);
        equalityMethod.append(", ");
        equalityMethod.append(valueTwo);
        equalityMethod.append(");");



        //close
        equalityMethod.append("\n\t}");




        return equalityMethod.toString();

    }

    /**
     * @return the dynamicImports
     */
    public ArrayList<String> getDynamicImports() {
         return dynamicImports;
    }

    /**
     * @param dynamicImports the dynamicImports to set
     */
    public void setDynamicImports(ArrayList<String> dynamicImports) {
        this.dynamicImports = dynamicImports;
    }

    /**
     * @return the equalityMethodTypes
     */
    public ArrayList<Class> getEqualityMethodTypes() {
        return equalityMethodTypes;
    }

    /**
     * @param equalityMethodTypes the equalityMethodTypes to set
     */
    public void setEqualityMethodTypes(ArrayList<Class> equalityMethodTypes) {
        this.equalityMethodTypes = equalityMethodTypes;
    }
}
