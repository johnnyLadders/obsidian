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
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 * 
 * Contributors:
 *
 */
public class MethodMap {

    //Hashmap used to implement map structure
    private HashMap<String, String> hashmap = new HashMap<String, String>();
    
    //Class short name
    private String shortClassName;
    
    //method array of methods in class
    private Method[] classMethods;
    
    //Constructor array of the classes constructors
    private Constructor[] classConstructors;

    public MethodMap(Class classObject) throws ClassNotFoundException {


        //retrieve declared methods using the class object
        classMethods = classObject.getDeclaredMethods();

        //retrieve constructors using the class object
        classConstructors = classObject.getConstructors();
        
        //short name
        shortClassName = classObject.getSimpleName();

////        //get the list of untested methods and put them into a String[]
////        String[] untestedMethods = MethodDictionary.getUntestedMethods(shortClassName);

        //for each regular method
        for (Method m : classMethods) {
            //put into hashmap and initialize as untested
            hashmap.put(m.getName(), "UNTESTED");
        }

        //for each Constructor
        for (Constructor c : classConstructors) {
            //put into hashmap and initialize as untested
            hashmap.put(c.toString(), "UNTESTED");
        }

    }

    public String getUnaccountedMethods() {
        
        StringBuilder unaccountedMethods = new StringBuilder();

        for (Constructor c : classConstructors) {
            if (hashmap.get(c.toString()).compareToIgnoreCase("UNTESTED") == 0) {
                unaccountedMethods.append("\n\n    ##### Constructor #####");
                unaccountedMethods.append("    -" + c.toString() + "()");
                unaccountedMethods.append("    #######################");
            }
        }
        for (Method m : classMethods) {
            if (hashmap.get(m.getName()).compareToIgnoreCase("UNTESTED") == 0) {
                unaccountedMethods.append("\n    -" + m.getName() + "()");
            }
        }



        return unaccountedMethods.toString();
    }

    public void setTested(String method) {
        hashmap.put(method, "TESTED");
    }

    public void setIgnored(String method) {
        hashmap.put(method, "IGNORED");
    }

    public Boolean containsUntested() {
        return hashmap.containsValue("UNTESTED");
    }

    public void report() {
        String untestedLineOne = "The Following Methods/Constructors in "
                + shortClassName;
        String untestedLineTwo = "Have No Associated Test:";

        String testedOrIgnoredLineOne = "All Methods/Constructors in " + shortClassName
                + " Have";

        String testedOrIgnoredLineTwo = "Been Either Tested Or Ignored";

        String methodAccountability = "Method Accountability";

        int headerAndFootLength = untestedLineOne.length();
        
        
        System.out.print("\n");

        //first line of header
        for (int i = 0; i < headerAndFootLength - 1; i++) {
            System.out.print("*");
        }

        //open of middle line
        System.out.println("*");
        for (int i = 0;
                i < (((headerAndFootLength - methodAccountability.length()) - 2) / 2);
                i++) {
            System.out.print("*");
        }
        if(shortClassName.length() % 2 != 0){
            System.out.print("*");
        }
        System.out.print(" ");
        System.out.print(methodAccountability);
        System.out.print(" ");
        for (int i = 0;
                i < (((headerAndFootLength - methodAccountability.length()) - 2) / 2) - 2;
                i++) {
            System.out.print("*");
        }

        //last line of header
        System.out.println("*");
        for (int i = 0; i < headerAndFootLength; i++) {
            System.out.print("*");
        }

        if (hashmap.containsValue("UNTESTED")) {

            System.out.println("\n" + untestedLineOne);
            System.out.println(untestedLineTwo);
            System.out.println(this.getUnaccountedMethods());

        } else {
            System.out.println("\n" + testedOrIgnoredLineOne);
            System.out.println(testedOrIgnoredLineTwo);
        }
        //first line of header
        for (int i = 0; i < headerAndFootLength; i++) {
            System.out.print("*");
        }
    }
}
