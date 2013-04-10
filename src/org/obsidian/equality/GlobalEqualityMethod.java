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

import org.obsidian.obsidianConstants.MethodNames;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public class GlobalEqualityMethod {
    //Name of class for which this equality method exists to compare
    private String classType;
    
    //The contents of the method starting at the declaration and ending at the 
    //final curly brace
    private String contents;
    
    public GlobalEqualityMethod(String classType){
        this.classType = classType;
        
        //String Builder to construct equality method
        StringBuilder equalityMethod = new StringBuilder();

        String valueOne = "valueOne";
        String valueTwo = "valueTwo";
        
        if(classType.compareToIgnoreCase("valueone") == 0){
            for(int i = 0; i < 100; i++){
                System.out.println("got one");
            }
        }

        //declaration
        equalityMethod.append("\n\n\t");
        equalityMethod.append("//");
        equalityMethod.append(classType);
        equalityMethod.append("\n\t");
        equalityMethod.append("public static boolean "
                + MethodNames.EQUALITY_METHOD_NAME + " ( ");
        equalityMethod.append(classType);
        equalityMethod.append(" ");
        equalityMethod.append(valueOne);
        equalityMethod.append(", ");
        equalityMethod.append(classType);
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
        equalityMethod.append("\n\t\treturn false;");

        //close
        equalityMethod.append("\n\t}");
        
        contents = equalityMethod.toString();
    }
    
    public GlobalEqualityMethod(String classType, String contents){
        this.classType = classType;
        
        //StringBuilder for contents
        StringBuilder contentsStringBuilder = new StringBuilder();
        contentsStringBuilder.append("\n\n\t");
        contentsStringBuilder.append("//");
        contentsStringBuilder.append(classType);
        contentsStringBuilder.append("\n");
        contentsStringBuilder.append(contents);
        
        this.contents = contentsStringBuilder.toString();
    }

    /**
     * @return the classType
     */
    public String getClassType() {
        return classType;
    }

    /**
     * @param classType the classType to set
     */
    public void setClassType(String classType) {
        this.classType = classType;
    }

    /**
     * @return the contents
     */
    public String getContents() {
        return contents;
    }

    /**
     * @param contents the contents to set
     */
    public void setContents(String contents) {
        this.contents = contents;
    }
    
}
