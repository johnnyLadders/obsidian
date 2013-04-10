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

import java.lang.reflect.Field;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 * 
 * Contributors:
 *
 */
public class FieldNames {

    public static final String EXPECTED_RESULT = "expResult";
    public static final String PASSED_VALUE = "passedValue";
    public static final String METHOD_MAP = "methodMap";
    public static final String DEFAULT_INSTANCE_NAME = "instance";
    public static final String RESULT = "result";
    public static final String TEST_CASES = "testCases";
    public static final String TCI_VARIABLE = "tc";
    public static final String ACTUAL_RESULT = "actualResult";
    
    public static String getDefalutValueFieldName(Field f) {
        String name = f.getName();

        return name + "DefaultValue";
    }
}
