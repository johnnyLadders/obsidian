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
 *
 */
public class Content {
    
        public static final String[] OBSIDIAN_FIELDS = {
            "private static MethodMap methodMap;",
//        "private static Logger logger;",
//        "private static FileHandler filehandler;",
//        "private static MyHtmlFormatter myHtmlFormatter;"
        };
//        public static final String[] IMPORTS = {
//            //        "import java.util.logging.FileHandler;",
//            //        "import java.util.logging.Level;",
//            //        "import java.util.logging.Logger;",
//            "import org.junit.After;",
//            "import org.junit.AfterClass;",
//            "import org.junit.Before;",
//            "import org.junit.BeforeClass;",
//            "import org.junit.Test;",
//            "import static org.junit.Assert.*;",
//            "import java.lang.reflect.Field;",
//            "import org.obsidian.util.MethodMap;"
//        };
        public static final String[] IMPORTS = {
            //        "import java.util.logging.FileHandler;",
            //        "import java.util.logging.Level;",
            //        "import java.util.logging.Logger;",
            "org.junit.After",
            "org.junit.AfterClass",
            "org.junit.Before",
            "org.junit.BeforeClass",
            "org.junit.Test",
            "static org.junit.Assert.*",
            "java.lang.reflect.Field",
            "org.obsidian.util.MethodMap"
        };
        public static final String TEST_CLASS_INTRODUCTION = "/**"
                + "\n\t* The following test class is structured in the Following "
                + "manner:"
                + "\n\t* 1.Test ALL constructors(using reflection)"
                + "\n\t*"
                + "\n\t* 2. Once the Constructors have been validated, then Test "
                + "all Getters that have"
                + "\n\t* a paired setter(getters without a setter will be tested "
                + "later)"
                + "\n\t*"
                + "\n\t* 3.  When the Getters have passed their tests, then Test "
                + "the Setters paired"
                + "\n\t* with the previously tested Getters."
                + "\n\t*"
                + "\n\t* note: If any one of the tests mentioned in 1-3 fails, "
                + "then the remaining"
                + "\n\t* tests will no longer be valid, Since all of these will "
                + "be using in the"
                + "\n\t* testing of the following tests."
                + "\n\t*"
                + "\n\t* 4.  Test the remaining methods"
                + "\n\t*"
                + "\n\t*"
                + "\n\t* Test that are currently not ready for testing are Logged "
                + "but do not fail"
                + "\n\t*"
                + "\n\t*"
                + "\n\t*/";
        public static final String FAIL_STATEMENT = "fail(\"This is a Generated"
                + " Unit Test and Should be Modified\");";
}
