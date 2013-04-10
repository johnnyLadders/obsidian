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

package org.obsidian.equality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.obsidian.obsidianConstants.ClassNames;
import org.obsidian.obsidianConstants.Methods;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 * 
 * Contributors:
 *
 */
public class PackageEqualityMethodClass {

    private HashMap<String, PackageEqualityMethod> methods;
    private String classPackage;
    private ArrayList<String> imports = new ArrayList<String>();

    public PackageEqualityMethodClass(String classPackage) {

        //initialize ArrayList
        methods = new HashMap<String, PackageEqualityMethod>();

        //initialize classPackage
        this.classPackage = classPackage;
        
        //add global equality methods to imports
        imports.add("obsidian.GlobalEqualityMethods");

    }

    public void addMethod(String classType) {

        //if not already in methods
        if (!(methods.containsKey(classType))) {

            //add
            methods.put(classType, new PackageEqualityMethod(classType));
        }
    }

    public void addMethod(String classType, PackageEqualityMethod method) {

        //if not already in methods
        if (!(methods.containsKey(classType))) {

            //add
            methods.put(classType, method);
        }
    }

    public void addImport(String Import) {
//        try {
        boolean ignore = false;

        //Class object for import
        //Class c = Class.forName(Import, true, Main.classLoader);
        //Class c = Class.forName(Import);

        //Make sure: not already in imports
        if (imports.contains(Import)) {
            ignore = true;
        }
        
        if (Import.compareToIgnoreCase("org.cirdles.obsidian.util.MethodMap") == 0){
            ignore = true;
        }

        //Make sure: not an Exception
//        Class superClass = c.getSuperclass();//first check immediate super

//            if (superClass != null) {
//                while (!superClass.equals(Object.class)) {//while still has superclasses
//
//
//                    if (superClass.equals(Exception.class)) {
//
//                        //ignore: it's an exception
//                        ignore = true;
//
//                    }
//
//                    superClass = superClass.getSuperclass();
//                }
//            }



        if (!ignore) {
            imports.add(Import);
        }
//        } catch (ClassNotFoundException ex) {
//            System.out.println(Import);
//            Logger.getLogger(PackageEqualityMethodClass.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public String toString() {
        StringBuilder equalityMethodsClass = new StringBuilder();


        //Package Declaration
        equalityMethodsClass.append(classPackage).append(";\n\n");

        //Add imports
        for (int i = 0; i < imports.size(); i++) {
            equalityMethodsClass.append("import ").
                    append(imports.get(i)).
                    append(";\n");
        }

        //Open Header Build
        int inSet = 3;//number of * before and after class name dataInputStream header

        String packageName = classPackage;
        String simplePackageName = packageName.substring(packageName.lastIndexOf(".") + 1);
        String headEM = "Equality Methods";

        int eMheaderLength = simplePackageName.length() + //length of simple package name
                (inSet * 2) + 3 + //an inset on either side plus a buffer space on either side and middle
                headEM.length();//length of the file name

        equalityMethodsClass.append("\n//");

        for (int i = 0; i < eMheaderLength; i++) {
            equalityMethodsClass.append("*");
        }

        equalityMethodsClass.append("\n//");

        for (int i = 0; i < eMheaderLength; i++) {
            equalityMethodsClass.append("*");
        }

        equalityMethodsClass.append("\n//");

        for (int i = 0; i < inSet; i++) {
            equalityMethodsClass.append("*");
        }

        equalityMethodsClass.append(" ");
        equalityMethodsClass.append(simplePackageName);
        equalityMethodsClass.append(" ");
        equalityMethodsClass.append(headEM);
        equalityMethodsClass.append(" ");

        for (int i = 0; i < inSet; i++) {
            equalityMethodsClass.append("*");
        }

        equalityMethodsClass.append("\n//");

        for (int i = 0; i < eMheaderLength; i++) {
            equalityMethodsClass.append("*");
        }

        equalityMethodsClass.append("\n//");

        for (int i = 0; i < eMheaderLength; i++) {
            equalityMethodsClass.append("*");
        }
        equalityMethodsClass.append("\n//");
        //end Header Build

        //class declaration
        equalityMethodsClass.append("\n\nclass ");
        equalityMethodsClass.append(ClassNames.PACKAGE_EQUALITY_METHODS);
        equalityMethodsClass.append("{");

        //Equality Methods
        if (!(methods.containsKey("String"))) {
            this.addMethod("String", new PackageEqualityMethod(
                    "String", Methods.DEFAULT_STRING_EQUALITY_METHOD));
        }

        if (!(methods.containsKey("Exception"))) {
            this.addMethod("Exception", new PackageEqualityMethod(
                    "Exception", Methods.DEFAULT_EXCEPTION_EQUALITY_METHOD));
        }
        
        if (!(methods.containsKey("Throwable"))) {
            this.addMethod("Throwable", new PackageEqualityMethod(
                    "Throwable", Methods.DEFAULT_THROWABLE_EQUALITY_METHOD));
        }



        //Sort and add each method
        for (String s : this.sortMethods()) {

            equalityMethodsClass.append(methods.get(s).getContents());
        }

        equalityMethodsClass.append("\n}");

        return equalityMethodsClass.toString();
    }

    private ArrayList<String> sortMethods() {
        String[] keys = methods.keySet().toArray(new String[0]);
        ArrayList<String> keysArrayList = new ArrayList<String>();
        Collections.addAll(keysArrayList, keys);

        for (int i = 1; i < keysArrayList.size(); i++) {
            int newIndex = i;

            for (int j = i - 1; j >= 0; j--) {
                if (keysArrayList.get(i).compareToIgnoreCase(
                        keysArrayList.get(j)) < 0) {
                    newIndex = j;
                }
            }

            String temp = keysArrayList.get(i);

            keysArrayList.remove(i);

            keysArrayList.add(newIndex, temp);
        }

        //return sorted arraylist
        return keysArrayList;


    }
}
