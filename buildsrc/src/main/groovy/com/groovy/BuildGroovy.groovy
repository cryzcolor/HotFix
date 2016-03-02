package com.groovy

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.CtNewMethod

public class BuildGroovy {

    /**
     * 植入字节码
     * @param resourcePath 项目源码所在目录
     * @param buildDir 项目编译class所在目录
     * @param hackBuildDir hackdex的class所在目录
     * @param fixedClass 修复的类名
     */
    public
    static void process(String resourcePath, String buildDir, String hackBuildDir, String fixedClass) {
        List<String> invokeClass = getInvokedClass(resourcePath, fixedClass);
        invokeClass.each { String string ->
            insertCode(buildDir, hackBuildDir, "com.hotfix." + string)
        }
    }

    /**
     * 获取调用到修复类的类集合
     * @param resourcePath 项目源码所在目录
     * @param fixedClass 修复的类名
     * @return
     */
    private static List<String> getInvokedClass(String resourcePath, String fixedClass) {
        List<String> invokeClass = new ArrayList<String>();
        String packageName = fixedClass.substring(0, fixedClass.lastIndexOf("."));
        String className = fixedClass.substring(fixedClass.lastIndexOf(".") + 1);
        def files = new File(resourcePath).listFiles()
        getAllInvokedClass(packageName, className, files, invokeClass);
        return invokeClass;
    }

    /**
     * 从包根目录逐级获取调用到修复类的类
     * @param packageName 修复类包名
     * @param className 修复类的类名
     * @param file 文件
     * @param invokeClass 调用到修复类的类集合
     */
    private
    static void getAllInvokedClass(String packageName, String className, File[] files, List<String> invokeClass) {
        files.each { File file ->
            if (file.isFile()) {
                def fileName = file.name.substring(0, file.name.indexOf("."));
                if (fileName != className) {
                    List<String> lines = file.readLines()
                    for (s in lines) {
                        if (s.contains(packageName) || s.contains(className)) {
                            invokeClass.add(fileName)
                            break;
                        }
                    }
                }
            } else {
                getAllInvokedClass(packageName, className, file.listFiles(), invokeClass);
            }
        }
    }

    /**
     * 植入字节码
     * @param buildDir 项目编译class所在目录
     * @param hackBuildDir hackdex的class所在目录
     * @param className 调用到修复类的类
     */
    private static void insertCode(String buildDir, String hackBuildDir, String className) {
        ClassPool classes = ClassPool.getDefault()
        classes.appendClassPath(buildDir)
        classes.appendClassPath(hackBuildDir)

        CtClass c = classes.getCtClass(className)
        if (c.isFrozen()) {
            c.defrost()
        }
        CtMethod mthd = CtNewMethod.make("private void toHack() {com.hackdex.HackDex.toHack();}", c);
        c.addMethod(mthd);
        c.writeFile(buildDir)
    }

}