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

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *      Michael Cole (micole.3@gmail.com)
 *
 */
public class Methods {
    //Default String Equality Method

    public static final String DEFAULT_STRING_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " String valueOne,"
            + " String valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne.compareToIgnoreCase(valueTwo) == 0){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    //Default Exception Equality Method
    public static final String DEFAULT_EXCEPTION_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " Exception valueOne,"
            + " Exception valueTwo) {"
            + "\n\n\t\treturn valueOne.toString().compareToIgnoreCase(valueTwo.toString()) == 0;"
            + "\n\t}";
    public static final String DEFAULT_THROWABLE_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " Throwable valueOne,"
            + " Throwable valueTwo) {"
            + "\n\n\t\treturn valueOne.toString().compareToIgnoreCase(valueTwo.toString()) == 0;"
            + "\n\t}";
    public static final String METHOD_ACCOUNTABILITY = "\n\t@Test"
            + "\n\tpublic void methodAccountability() {"
            + "\n\n\t\tmethodMap.report();"
            + "\n\n\t\tassertFalse(//"
            + "\n\t\t\t\"Class contains methods that are unaccounted for\",//"
            + "\n\t\t\tmethodMap.containsUntested());"
            + "\n\t}";   
    
    // Primitive comparison
    public static final String DEFAULT_BYTE_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " byte valueOne,"
            + " byte valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne == valueTwo){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_SHORT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " short valueOne,"
            + " short valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne == valueTwo){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_INT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " int valueOne,"
            + " int valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne == valueTwo){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_LONG_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " long valueOne,"
            + " long valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne == valueTwo){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_FLOAT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " float valueOne,"
            + " float valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne == valueTwo){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_DOUBLE_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " double valueOne,"
            + " double valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne == valueTwo){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_BOOLEAN_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " boolean valueOne,"
            + " boolean valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne == valueTwo){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_CHAR_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " char valueOne,"
            + " char valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne == valueTwo){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    
    // Primitive Object
    public static final String DEFAULT_BYTE_OBJECT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " Byte valueOne,"
            + " Byte valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne.equals(valueTwo)){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_SHORT_OBJECT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " Short valueOne,"
            + " Short valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne.equals(valueTwo)){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_INTEGER_OBJECT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " Integer valueOne,"
            + " Integer valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne.equals(valueTwo)){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_LONG_OBJECT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " Long valueOne,"
            + " Long valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne.equals(valueTwo)){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_FLOAT_OBJECT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " Float valueOne,"
            + " Float valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne.equals(valueTwo)){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_DOUBLE_OBJECT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " Double valueOne,"
            + " Double valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne.equals(valueTwo)){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_BOOLEAN_OBJECT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " Boolean valueOne,"
            + " Boolean valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne.equals(valueTwo)){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    public static final String DEFAULT_CHARACTER_OBJECT_EQUALITY_METHOD = "\tstatic boolean areEqual ("
            + " Character valueOne,"
            + " Character valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\tif(valueOne.equals(valueTwo)){"
            + "\n\t\t\tareEqual = true;"
            + "\n\t\t}\n\n\t\treturn areEqual;"
            + "\n\t}";
    
    
    public static final String DEFAULT_SINGLETON_ARENOTEQUAL_METHOD = "\tstatic <T> boolean areNotEqual ("
            + " T valueOne,"
            + " T valueTwo) {"
            + "\n\n\t\tBoolean areEqual = false;"
            + "\n\n\t\ttry{"
            + "\n\t\t\tareEqual = areEqual(valueOne, valueTwo);"
            + "\n\t\t}"
            + "\n\t\tfinally{}"
            + "\n\n\t\treturn !areEqual;"
            + "\n\t}";
}
