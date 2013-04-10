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

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Hunter Hegler (jhhegler@gmail.com)
 *
 * Contributors:
 *
 */
public class Main {

    public static URLClassLoader classLoader;
    public static String targetDirectory = "";
    public static ArrayList<Class> classes;

    public static void main(String[] args) throws Exception {


        //Split argument
        String[] paths = args[0].split(File.pathSeparator);

        targetDirectory = args[1];

        //ArrayList of URLs to be added to the classLoader
        ArrayList<URL> urls = new ArrayList<URL>();

        //Stack for traversing directory tree
        Stack<String> fileDirStack = new Stack<String>();

        //Array of fully qualified class names
        ArrayList<String> classNames = new ArrayList<String>();

        //ArrayList of class file names
        classes = new ArrayList<Class>();


        for (String path : paths) {

            //make File object from path
            File target = new File(path);

            //add path to urls
            urls.add(target.toURI().toURL());

            if (target.isDirectory()) {

                for (String s : target.list()) {
                    fileDirStack.push(target.getPath()
                            + File.separator + s);
                }
            }
            while (!fileDirStack.empty()) {
                File tempFile = new File(fileDirStack.pop());

                if (tempFile.isDirectory()) {
                    for (String s : tempFile.list()) {
                        fileDirStack.push(tempFile.getPath()
                                + File.separator + s);
                    }
                } else if (tempFile.isFile()) {
                    //get extension from file name
                    String fileName = tempFile.getName();

                    //if extension is .class
                    if (fileName.endsWith(".class")) {
                        String name;

                        name = tempFile.getCanonicalPath();
                        name = name.substring(target.getCanonicalPath().length());
                        name = name.substring(name.indexOf(File.separator) + 1,
                                name.lastIndexOf("."));
                        name = name.replace(File.separator, ".");

                        classNames.add(name);
                    } else if (fileName.endsWith(".jar")) {
                        urls.add(tempFile.toURI().toURL());
                    }

                }
            }


        }


        //ClassLoader
        classLoader = new URLClassLoader(urls.toArray(new URL[0]));
        System.out.println("\n");
        System.out.println("#############################");
        System.out.println("###### Loading Classes ######");
        System.out.println("#############################");
        for (String className : classNames) {
            System.out.println("Loading " + className);
            Class tempClass = Class.forName(className, true, classLoader);
            classes.add(tempClass);
        }
        System.out.println("");
        System.out.println("##### Total Loaded Classes: " + classNames.size()
                + " #####");


        System.out.println("\n");
        System.out.println("###########################");
        System.out.println("###### Writing Tests ######");
        System.out.println("###########################");

//        ArrayList<Class> classes = new ArrayList<Class>();
//        classes.add(PracticeClass.class);
//        classes.add(PracticeClassTwo.class);
//        classes.add(PracticeClassThree.class);


        for (Class c : classes) {
            
            boolean ignoreClass = false;

            //ignore if enum
            if (c.isEnum()) {
                ignoreClass = true;
            }
            //ignore if member class
            if (c.isMemberClass()) {
                ignoreClass = true;
            }
            if (c.isLocalClass()){
                ignoreClass = true;
            }
            //ignore if null name
            if (c.getSimpleName().compareToIgnoreCase("") == 0){
                ignoreClass = true;
            }
            if (!ignoreClass) {
                System.out.println("Writing Test For " + c.getSimpleName());

                if (!Modifier.isAbstract(c.getModifiers()) && !Modifier.isPrivate(c.getModifiers())) {
                    Builder.setTargetDirectory(targetDirectory);
                    Builder.buildAndWrite(c);

                }

            }

        }

        System.out.println("##### Total Test Classes Written: " + classes.size());


    }
}
