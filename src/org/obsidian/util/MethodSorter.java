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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public class MethodSorter {

    //
    //
    //
    //************************
    //************************
    //**** Method Getters ****
    //************************
    //************************
    //
    //
    //
    /**
     * getGetters takes an array of Methods and finds the methods
     * dataInputStream the array that are "getters" defined as a method that:
     *
     * 1.Is not private 2.Has no parameters 3.returns a value
     *
     *
     *
     * @param allMethods - a Method array of all methods dataInputStream the
     * class
     * @return returns an ArrayList<Method> of the "getters" found
     * dataInputStream the Method[]
     */
    public static ArrayList<Method> getGetters(Method[] allMethods) {
        //arraylist to put getters dataInputStream when found
        ArrayList<Method> getters = new ArrayList<Method>();

        Boolean notAGetter;

        for (int i = 0; i < allMethods.length; i++) {

            //reset notAGetter to false
            notAGetter = false;
            //if method is private ignore it
            if (Modifier.isPrivate(allMethods[i].getModifiers())) {
                notAGetter = true;
            }
            //if method has parameters ignore it
            if (!(allMethods[i].getParameterTypes().length == 0)) {
                notAGetter = true;
            }
            //if method's returntype is void
            if (allMethods[i].getReturnType().toString().compareToIgnoreCase("void") == 0) {
                notAGetter = true;
            }
            //if access$000 method
            if(allMethods[i].getName().matches("access.*")){
                notAGetter = true;
            }
            //not notAGetter == getter
            if (!notAGetter) {
                //add to array list of getters
                getters.add(allMethods[i]);
            }



        }

        return getters;
    }

    /**
     * setSetters takes an array of Methods and find the methods dataInputStream
     * the array that are "setters." Defined as a method that:
     *
     * 1.Is not private 2.Has ONE parameter (no more, no less) 3.Returns void
     *
     * @param allMethods - a Method array of all methods dataInputStream the
     * class
     * @return returns an ArrayList<Method> of the "setters" found
     * dataInputStream the Array
     */
    public static ArrayList<Method> getSetters(Method[] allMethods) {

        //Arraylist for setters, to be returned
        ArrayList<Method> setters = new ArrayList<Method>();

        //assume that method is a setter unless proven otherwise
        Boolean notASetter;

        for (int i = 0; i < allMethods.length; i++) {

            //reset notASetter to false
            notASetter = false;
            //if method is private ignore it
            if (Modifier.isPrivate(allMethods[i].getModifiers())) {
                notASetter = true;
            }
            //if method has more than one parameter ignore it
            if (allMethods[i].getParameterTypes().length != 1) {
                notASetter = true;
            }
            //if method's returntype isn't void
            if (allMethods[i].getReturnType().toString().compareToIgnoreCase("void") != 0) {
                notASetter = true;
            }
            if (allMethods[i].getName().compareToIgnoreCase("main") == 0) {
                notASetter = true;
            }
            //if access$000 method
            if(allMethods[i].getName().matches("access.*")){
                notASetter = true;
            }
            
            //not notASetter == setter
            if (!notASetter) {
                //add to array list of getters
                setters.add(allMethods[i]);
            }



        }
                
        return setters;
    }

    public static ArrayList<Method> getRemainingMethods(Method[] allMethods) {
        ArrayList<Method> methodsToReturn = new ArrayList<Method>();

        //add all methods from class to array list
        for (int i = 0; i < allMethods.length; i++) {
            if (!Modifier.isPrivate(allMethods[i].getModifiers()) && !(allMethods[i].getName().matches("access.*"))) {
                methodsToReturn.add(allMethods[i]);
            }

        }

//        //remove methods from interfaces in java.lang
//        //this is due to a bug in reflection.
//        //reflection thinks that these methods are defined by the class under test
//        if (allMethods.length > 0) {
//            Class declaringClass = allMethods[0].getDeclaringClass();
//
//            Class[] interfaces = declaringClass.getInterfaces();
//
//            for (Class c : interfaces) {
//                String packageName = c.getName();
//                packageName = packageName.substring(0, packageName.lastIndexOf("."));
//
//                //if in java.lang
//                
//                if (packageName.compareToIgnoreCase("java.lang") == 0) {
//                    for (Method m : c.getDeclaredMethods()) {
//                        System.out.println(m.getName());
//                        methodsToReturn.remove(m);
//                    }
//                }
//            }
//        }
//        
//        //remove methods from Object.class
//        for(Method m: Object.class.getDeclaredMethods()){
//            if(methodsToReturn.contains(m)){
//                methodsToReturn.remove(m);
//            }
//        }
//




        //remove getters
        for (Method m : getGetters(allMethods)) {
            methodsToReturn.remove(m);
        }

        //remove setters
        for (Method m : getSetters(allMethods)) {
            methodsToReturn.remove(m);
        }

        //remove private
        for (Method m : getPrivateMethods(allMethods)) {
            methodsToReturn.remove(m);
        }

        //remove Object CompareTo
        for (int i = 0; i < methodsToReturn.size(); i++) {
            Method m = methodsToReturn.get(i);
            if (m.getName().compareToIgnoreCase("compareTo") == 0) {
                if ((m.getParameterTypes().length > 0)
                        && m.getParameterTypes()[0].getSimpleName().compareToIgnoreCase("Object") == 0) {
                    methodsToReturn.remove(m);
                }
            }
        }

        //remove Object CompareTo
        for (int i = 0; i < methodsToReturn.size(); i++) {
            Method m = methodsToReturn.get(i);
            if (m.getName().compareToIgnoreCase("Compare") == 0) {
                if ((m.getParameterTypes().length > 0)
                        && m.getParameterTypes()[0].getSimpleName().compareToIgnoreCase("Object") == 0) {
                    methodsToReturn.remove(m);
                }
            }
        }

        return methodsToReturn;
    }

    public static ArrayList<Method> getPrivateMethods(Method[] allMethods) {
        ArrayList<Method> methodsToReturn = new ArrayList<Method>();

        for (int i = 0; i < allMethods.length; i++) {
            if (Modifier.isPrivate(allMethods[i].getModifiers())) {
                methodsToReturn.add(allMethods[i]);
            }
        }

        return methodsToReturn;
    }
}
