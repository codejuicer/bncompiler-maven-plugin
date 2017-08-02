/*
 Copyright 2006-2011 Abdulla Abdurakhmanov (abdulla@latestbit.com)
 Original sources are available at www.latestbit.com

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.bn.compiler;

import java.io.File;
import java.lang.reflect.Field;

import junit.framework.TestCase;

public class MainTest extends TestCase {
    public MainTest(String sTestName) {
        super(sTestName);
    }

    public void testStart() throws Exception {
        BNCompilerMojo compiler = new BNCompilerMojo();
        Field modulesPath = compiler.getClass().getDeclaredField("modulesPath");
        modulesPath.setAccessible(true);
        modulesPath.set(compiler, "xslsrc" + File.separator + "modules");
        
        Field moduleName = compiler.getClass().getDeclaredField("moduleName");
        moduleName.setAccessible(true);
        moduleName.set(compiler, "java");
        
        Field outputDir = compiler.getClass().getDeclaredField("outputDir");
        outputDir.setAccessible(true);
        outputDir.set(compiler, "testworkdir" + File.separator + "output");
        
        Field fileName = compiler.getClass().getDeclaredField("inputFileName");
        fileName.setAccessible(true);
        fileName.set(compiler, "testworkdir" + File.separator + "test.asn");
        
        Field ns = compiler.getClass().getDeclaredField("namespace");
        ns.setAccessible(true);
        ns.set(compiler, "org.bn.coders.test_asn");
        
        compiler.start();
    }
    
    public void testCSStart() throws Exception {
        BNCompilerMojo compiler = new BNCompilerMojo();
        Field modulesPath = compiler.getClass().getDeclaredField("modulesPath");
        modulesPath.setAccessible(true);
        modulesPath.set(compiler, "xslsrc" + File.separator + "modules");
        
        Field moduleName = compiler.getClass().getDeclaredField("moduleName");
        moduleName.setAccessible(true);
        moduleName.set(compiler, "cs");
        
        Field outputDir = compiler.getClass().getDeclaredField("outputDir");
        outputDir.setAccessible(true);
        outputDir.set(compiler, "testworkdir" + File.separator + "output-cs");
        
        Field fileName = compiler.getClass().getDeclaredField("inputFileName");
        fileName.setAccessible(true);
        fileName.set(compiler, "testworkdir" + File.separator + "test.asn");
        
        Field ns = compiler.getClass().getDeclaredField("namespace");
        ns.setAccessible(true);
        ns.set(compiler, "org.bn.coders.test_asn");
        
        compiler.start();
    }
}
