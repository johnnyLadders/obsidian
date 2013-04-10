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

package org.obsidian;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.obsidian.equality.GlobalEqualityMethod;
import org.obsidian.equality.GlobalEqualityMethodClass;
import org.obsidian.equality.PackageEqualityMethod;
import org.obsidian.equality.PackageEqualityMethodClass;
import org.obsidian.obsidianConstants.FileNames;
import org.obsidian.obsidianConstants.MethodNames;
import org.obsidian.test.ConcreteClassTest;
import org.obsidian.test.TestAbstract;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public final class Builder {

    private static String targetDirectory = "";
    private static TestAbstract test;

    public static void writeTestClassToFile(Class classToTest, String tests) {



        try {

            File outFile = new File(Builder.getTestFilePathWithTestName());

            File outFileParentDirectory = new File(outFile.getParent());

            outFileParentDirectory.mkdirs();

            FileWriter outFileWriter = new FileWriter(outFile);

            BufferedWriter outBuffer = new BufferedWriter(outFileWriter);

            outBuffer.write(tests);

            outBuffer.close();

            outFileWriter.close();


        } catch (IOException ex) {
        }

    }

    public static void buildAndWrite(Class classToTest) {

        test = new ConcreteClassTest(classToTest);
        test.build();

        //Build Test Class
        String tests = test.toString();

        boolean containsSwing = false;
        for (String s : test.getDynamicImports()) {
            if (s.matches("java.swing.*")) {
                containsSwing = true;
            } else if (s.matches("javax.swing.*")) {
                containsSwing = true;
            }
        }

        boolean containsAWT = false;
        for (String s : test.getDynamicImports()) {
            if (s.matches("java.awt.*")) {
                containsAWT = true;
            }
//            else if (s.matches("javax.awt.*")) {
//                containsAWT = true;
//            }
        }

        if (!containsSwing && !containsAWT) {
            //Write Test Class
            Builder.writeTestClassToFile(classToTest, tests);

            //Build and Write Package Equality Method Class
            Builder.buildAndWritePackageEqualityMethodClass();

            Builder.buildAndWriteGlobalEqualityMethodClass();
        }

    }
    //
    //
    //
    //***************************************
    //***************************************
    //**** Package Equality Method Class ****
    //***************************************
    //***************************************
    //
    //
    //

    private static String buildPackageEqualityMethodClass(
            PackageEqualityMethodClass equalityMethodClass) {

        //Add imports
        ArrayList<String> packageEqualityImports = test.getDynamicImports();

        for (String s : packageEqualityImports) {
            equalityMethodClass.addImport(s);
        }



        //Equality Methods
        for (Class c : test.getEqualityMethodTypes()) {
            equalityMethodClass.addMethod(c.getSimpleName());
        }

        //return
        return equalityMethodClass.toString();
    }

    private static void buildAndWritePackageEqualityMethodClass() {
        String packageEqualityMethod = "";

        //if equality method class doesn't already exist dataInputStream this package
        if (!packageHasEqualityMethodClass()) {

            //Package Equality method Class
            PackageEqualityMethodClass equalityMethodClass = new //
                    PackageEqualityMethodClass(test.getClassTested().getPackage().toString());

            //Build package equality method class
            packageEqualityMethod = Builder.buildPackageEqualityMethodClass(
                    equalityMethodClass);


        } else {
            //Package Equality method Class by extracting from file
            PackageEqualityMethodClass equalityMethodClass =
                    Builder.deserializePackageEqualityMethodClass();

            //Build package equality method class
            packageEqualityMethod = Builder.buildPackageEqualityMethodClass(
                    equalityMethodClass);



        }
        writePackageEqualityMethodClassToFile(packageEqualityMethod);


    }

    private static PackageEqualityMethodClass deserializePackageEqualityMethodClass() {

        //initialize class to be returned
        PackageEqualityMethodClass equalityMethodClass = new PackageEqualityMethodClass(
                test.getClassTested().getPackage().toString());


        try {
            //Read File In
            FileInputStream fileInputStream;
            fileInputStream = new FileInputStream(
                    Builder.getPackageEqualityMethodsFilePath());
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));


            //regular expression pattern to find method declarations
            Pattern methodDeclarationPattern = Pattern.compile(
                    ".*" + MethodNames.EQUALITY_METHOD_NAME + "\\s*\\(.*\\, .*\\)\\s*\\{",
                    Pattern.CASE_INSENSITIVE);

            //regular expression pattern for finding imports
            Pattern importsPattern = Pattern.compile(".*import.*;");

            //Check each line to see if it matches the pattern
            String line;
            String classType;
            while ((line = bufferedReader.readLine()) != null) {

                //Matcher for Method Declartion as Defined by pattern above
                Matcher methodDeclartionMatcher = methodDeclarationPattern.matcher(line);

                //Matcher for imports as Defined by pattern above
                Matcher importsMatcher = importsPattern.matcher(line);

                //If matches for method declaration
                if (methodDeclartionMatcher.matches()) {
                    //StringBuilder for gathering the contents of the method
                    StringBuilder contents = new StringBuilder();

                    contents.append(line).append("\n");

                    //initialize balanceNum to show that the braces are now 
                    //unblanced
                    int balanceNum = 1;

                    //boolean to show whether double quotes are balanced
                    boolean doubleQuotesBalanced = true;

                    //boolean to show whether single quotes are balanced
                    boolean singleQuotesBalanced = true;

                    //get next char and add to contents until braces are 
                    //balanced
                    while (balanceNum != 0) {
                        char currentChar = (char) bufferedReader.read();

                        if (currentChar == '{' && (doubleQuotesBalanced && singleQuotesBalanced)) {
                            balanceNum++;
                            contents.append(currentChar);
                        } else if (currentChar == '}' && (doubleQuotesBalanced && singleQuotesBalanced)) {
                            balanceNum--;
                            contents.append(currentChar);
                        } else if (currentChar == '\"') {
                            //toggle unbalanced
                            doubleQuotesBalanced = !doubleQuotesBalanced;
                            contents.append(currentChar);
                        } else if (currentChar == '\'' && doubleQuotesBalanced) {
                            //toggle unbalanced
                            singleQuotesBalanced = !singleQuotesBalanced;
                            contents.append(currentChar);
                        } else if (currentChar == '\\') {
                            //add current char
                            contents.append(currentChar);
                            //ignore next
                            currentChar = (char) bufferedReader.read();
                            contents.append(currentChar);
                        } else if (currentChar == '/') {
                            contents.append(currentChar);

                            //get next
                            currentChar = (char) bufferedReader.read();
                            if (currentChar == '/') {
                                contents.append(currentChar);
                                //hopefully this will ignore the rest of the line
                                contents.append(bufferedReader.readLine());
                                contents.append("\n");
                            }
                        } else {
                            contents.append(currentChar);
                        }





                    }

                    //extract classtype from line
                    classType = line.substring(line.indexOf("(") + 1, line.indexOf(",", line.indexOf("(")));
                    classType = classType.trim();
                    classType = classType.split(" ")[0];


                    PackageEqualityMethod method = new PackageEqualityMethod(
                            classType, contents.toString());
                    equalityMethodClass.addMethod(classType, method);

                }

                //if  line matches import
                if (importsMatcher.matches()) {
                    line = line.substring(line.indexOf(" ") + 1, line.length() - 1);
                    equalityMethodClass.addImport(line);
                }
            }


        } catch (IOException ex) {
            Logger.getLogger(Builder.class.getName()).log(Level.SEVERE, null, ex);
        }




        return equalityMethodClass;
    }

    public static void writePackageEqualityMethodClassToFile(String PEMC) {

        try {

            File outFile = new File(Builder.getPackageEqualityMethodsFilePath());

            File outFileParentDirectory = new File(outFile.getParent());

            outFileParentDirectory.mkdirs();

            FileWriter outFileWriter = new FileWriter(outFile);

            BufferedWriter outBuffer = new BufferedWriter(outFileWriter);

            outBuffer.write(PEMC);

            outBuffer.close();

            outFileWriter.close();


        } catch (IOException ex) {
        }

    }

    //
    //
    //
    //***************************************
    //***************************************
    //**** Global Equality Method Class ****
    //***************************************
    //***************************************
    //
    //
    //
    private static String buildGlobalEqualityMethodClass(
            GlobalEqualityMethodClass equalityMethodClass) {

        //Add imports
        ArrayList<String> globalEqualityImports = test.getDynamicImports();

//        for (String s : globalEqualityImports) {
//            equalityMethodClass.addImport(s);
//        }



        //Equality Methods
        for (Class c : test.getEqualityMethodTypes()) {
            if (Modifier.isPublic(c.getModifiers())) {
                equalityMethodClass.addMethod(c.getCanonicalName());
            }

        }

        //return
        return equalityMethodClass.toString();
    }

    private static void buildAndWriteGlobalEqualityMethodClass() {
        String globalEqualityMethod = "";

        //if equality method class doesn't already exist dataInputStream this package
        if (!projectHasGlobalEqualityMethodClass()) {

            //Global Equality method Class
            GlobalEqualityMethodClass equalityMethodClass = new //
                    GlobalEqualityMethodClass(test.getClassTested().getPackage().toString());

            //Build Global equality method class
            globalEqualityMethod = Builder.buildGlobalEqualityMethodClass(
                    equalityMethodClass);


        } else {
            //Global Equality method Class by extracting from file
            GlobalEqualityMethodClass equalityMethodClass =
                    Builder.deserializeGlobalEqualityMethodClass();

            //Build Global equality method class
            globalEqualityMethod = Builder.buildGlobalEqualityMethodClass(
                    equalityMethodClass);



        }
        writeGlobalEqualityMethodClassToFile(globalEqualityMethod);


    }

    private static GlobalEqualityMethodClass deserializeGlobalEqualityMethodClass() {

        //initialize class to be returned
        GlobalEqualityMethodClass equalityMethodClass = new GlobalEqualityMethodClass(
                test.getClassTested().getPackage().toString());


        try {
            //Read File In
            FileInputStream fileInputStream;
            fileInputStream = new FileInputStream(
                    Builder.getGlobalEqualityMethodClassFilePath());
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));


            //regular expression pattern to find method declarations
            Pattern methodDeclarationPattern = Pattern.compile(
                    ".*" + MethodNames.EQUALITY_METHOD_NAME + "\\s*\\(.*\\, .*\\)\\s*\\{",
                    Pattern.CASE_INSENSITIVE);

            //regular expression pattern for finding imports
            Pattern importsPattern = Pattern.compile(".*import.*;");

            //Check each line to see if it matches the pattern
            String line;
            String classType;
            while ((line = bufferedReader.readLine()) != null) {

                //Matcher for Method Declartion as Defined by pattern above
                Matcher methodDeclartionMatcher = methodDeclarationPattern.matcher(line);

                //Matcher for imports as Defined by pattern above
                Matcher importsMatcher = importsPattern.matcher(line);

                //If matches for method declaration
                if (methodDeclartionMatcher.matches()) {
                    //StringBuilder for gathering the contents of the method
                    StringBuilder contents = new StringBuilder();

                    contents.append(line).append("\n");

                    //initialize balanceNum to show that the braces are now 
                    //unblanced
                    int balanceNum = 1;

                    //boolean to show whether double quotes are balanced
                    boolean doubleQuotesBalanced = true;

                    //boolean to show whether single quotes are balanced
                    boolean singleQuotesBalanced = true;

                    //get next char and add to contents until braces are 
                    //balanced
                    while (balanceNum != 0) {
                        char currentChar = (char) bufferedReader.read();

                        if (currentChar == '{' && (doubleQuotesBalanced && singleQuotesBalanced)) {
                            balanceNum++;
                            contents.append(currentChar);
                        } else if (currentChar == '}' && (doubleQuotesBalanced && singleQuotesBalanced)) {
                            balanceNum--;
                            contents.append(currentChar);
                        } else if (currentChar == '\"') {
                            //toggle unbalanced
                            doubleQuotesBalanced = !doubleQuotesBalanced;
                            contents.append(currentChar);
                        } else if (currentChar == '\'' && doubleQuotesBalanced) {
                            //toggle unbalanced
                            singleQuotesBalanced = !singleQuotesBalanced;
                            contents.append(currentChar);
                        } else if (currentChar == '\\') {
                            //add current char
                            contents.append(currentChar);
                            //ignore next
                            currentChar = (char) bufferedReader.read();
                            contents.append(currentChar);
                        } else if (currentChar == '/') {
                            contents.append(currentChar);

                            //get next
                            currentChar = (char) bufferedReader.read();
                            if (currentChar == '/') {
                                contents.append(currentChar);
                                //hopefully this will ignore the rest of the line
                                contents.append(bufferedReader.readLine());
                                contents.append("\n");
                            }
                        } else {
                            contents.append(currentChar);
                        }





                    }

                    //extract classtype from line
                    classType = line.substring(line.indexOf("(") + 1, line.indexOf(",", line.indexOf("(")));
                    classType = classType.trim();
                    classType = classType.split(" ")[0];


                    GlobalEqualityMethod method = new GlobalEqualityMethod(
                            classType, contents.toString());
                    equalityMethodClass.addMethod(classType, method);

                }

                //if  line matches import
                if (importsMatcher.matches()) {
                    line = line.substring(line.indexOf(" ") + 1, line.length() - 1);
                    equalityMethodClass.addImport(line);
                }
            }


        } catch (IOException ex) {
            Logger.getLogger(Builder.class.getName()).log(Level.SEVERE, null, ex);
        }




        return equalityMethodClass;
    }

    public static void writeGlobalEqualityMethodClassToFile(String GEMC) {

        try {

            File outFile = new File(Builder.getGlobalEqualityMethodClassFilePath());

            File outFileParentDirectory = new File(outFile.getParent());

            outFileParentDirectory.mkdirs();

            FileWriter outFileWriter = new FileWriter(outFile);

            BufferedWriter outBuffer = new BufferedWriter(outFileWriter);

            outBuffer.write(GEMC);

            outBuffer.close();

            outFileWriter.close();


        } catch (IOException ex) {
        }

    }

    //
    //
    //
    //**********************
    //**********************
    //**** File Helpers ****
    //**********************
    //**********************
    //
    //
    //
    private static String getTestFilePathWithTestName() {

        String packagePath = Builder.getPackagePath(test.getClassTested());

        String path = packagePath + File.separator
                + test.getTestClassName() + ".java";

        return path;
    }

    private static String getPackageEqualityMethodsFilePath() {
        return Builder.getPackagePath(test.getClassTested())
                + File.separator + FileNames.PACKAGE_EQUALITY_METHOD_FILE_NAME;
    }

    private static String getPackagePath(Class classToTest) {
        StringBuilder path = new StringBuilder();

        path.append("test");

        Package p = classToTest.getPackage();

        String packageString = p.getName();

        String[] packageStringArray = packageString.split("\\.");

        if (packageStringArray.length > 0) {
            path.append(File.separator);
            for (int i = 0; i < packageStringArray.length; i++) {
                path.append(packageStringArray[i]);
                path.append(File.separator);
            }
            path.deleteCharAt(path.lastIndexOf(File.separator));
        }

        return targetDirectory + path.toString();

    }

    public static boolean packageHasEqualityMethodClass() {
        File abstractFile = new File(getPackageEqualityMethodsFilePath());

        return abstractFile.exists();
    }

    public static boolean projectHasGlobalEqualityMethodClass() {
        File abstractFile = new File(getGlobalEqualityMethodClassFilePath());

        return abstractFile.exists();
    }

    private static String getGlobalEqualityMethodClassFilePath() {
        return Builder.getGlobalEqualityPath(test.getClassTested())
                + File.separator + FileNames.GLOBAL_EQUALITY_METHOD_FILE_NAME;
    }

    private static String getGlobalEqualityPath(Class classToTest) {
        StringBuilder path = new StringBuilder();

        path.append("test");
        path.append(File.separator);
        path.append("obsidian");

        return targetDirectory + path.toString();

    }

    public static void setTargetDirectory(String targetDirectory) {
        Builder.targetDirectory = targetDirectory + File.separator;
    }

    public static String getTargetDirectory() {
        return targetDirectory;
    }
}
