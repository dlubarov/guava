import java.io.*;
import java.util.*;

import common.*;
import d.God;

import a.SourceFile;

import parse.SourceFileParser;
import util.FileUtils;

public class Main {
    private static final boolean
            printA = false,
            printB = false,
            printC = false,
            printD = false;

    public static void main(String[] args) throws IOException {
        // Process arguments.
        List<String> guavaFiles = new ArrayList<String>(), programArgs = new ArrayList<String>();
        for (String arg : args) {
            if (arg.endsWith(".guava"))
                guavaFiles.add(arg);
            else
                programArgs.add(arg);
        }

        // Parse source files.
        List<SourceFile> sourceFiles = new ArrayList<SourceFile>();
        for (String sourceFileName : guavaFiles) {
            String source = FileUtils.readFileAsUTF8(new File(sourceFileName));
            try {
                SourceFile sf = SourceFileParser.singleton.parse(source, 0).value;
                sourceFiles.add(sf);
            } catch (RuntimeException e) {
                throw new NiftyException(e, "Parse error in file %s.", sourceFileName);
            }
        }

        // Create a project from the source files.
        a.Project aProj = new a.Project(sourceFiles.toArray(new SourceFile[sourceFiles.size()]));
        if (printA) {
            System.out.println("--- PROJECT A ---");
            System.out.println(aProj);
            System.out.println();
        }

        // Refine (a -> b).
        b.Project bProj = aProj.refine();
        if (printB) {
            System.out.println("--- PROJECT B ---");
            System.out.println(bProj);
            System.out.println();
        }

        // Refine (b -> c).
        c.Project cProj = bProj.refine();
        if (printC) {
            System.out.println("--- PROJECT C ---");
            System.out.println(cProj);
            System.out.println();
        }

        // Compile (c -> d).
        d.TypeDef[] dProj = cProj.compile();
        if (printD) {
            System.out.println("--- PROJECT D ---");
            for (d.TypeDef typeDef : dProj) {
                System.out.println(typeDef);
                System.out.println();
            }
        }

        // Link and initialize each type.
        God.linkAll();
        God.runStaticInitializers();

        // Run main methods.
        long t0 = System.nanoTime();
        for (d.TypeDef typeDef : dProj)
            for (d.ConcreteMethodDef methodDef : typeDef.staticMethods) {
                d.RawMethod desc = methodDef.desc;
                if (!desc.name.equals("main"))
                    continue;
                if (desc.numGenericParams != 0)
                    continue;
                if (desc.paramTypes.length != 1)
                    continue;

                d.ty.desc.TypeDesc first = desc.paramTypes[0];
                if (!(first instanceof d.ty.desc.ParameterizedTypeDesc))
                    continue;
                d.ty.desc.ParameterizedTypeDesc firstParam = (d.ty.desc.ParameterizedTypeDesc) first;
                if (!firstParam.rawType.equals(RawType.coreSequence))
                    continue;
                if (!firstParam.genericArgs[0].equals(d.ty.desc.TypeDesc.coreString))
                    continue;

                d.BaseObject[] argStrings = new d.BaseObject[programArgs.size()];
                for (int i = 0; i < programArgs.size(); ++i)
                    argStrings[i] = d.VMUtils.makeString(programArgs.get(i));
                d.ty.ConcreteType arrayType = new d.ty.ConcreteType(
                        d.nat.NativeArray.TYPE,
                        new d.ty.ConcreteType[] {
                                new d.ty.ConcreteType(God.resolveType(RawType.coreString))
                        });
                d.BaseObject argsArray = new d.nat.NativeArray(arrayType, argStrings);
                methodDef.invoke(new d.BaseObject[] {argsArray}, d.ty.ConcreteType.NONE);
            }
        long dt = System.nanoTime() - t0;
        System.out.printf("Tests took %.2f seconds.\n", dt * 1e-9);
    }
}
