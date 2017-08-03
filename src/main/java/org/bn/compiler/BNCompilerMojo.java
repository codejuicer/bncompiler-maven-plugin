package org.bn.compiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.bn.compiler.parser.ASNLexer;
import org.bn.compiler.parser.ASNParser;
import org.bn.compiler.parser.model.ASN1Model;
import org.bn.compiler.parser.model.ASNModule;

@Mojo(name = "bncompiler", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, requiresDependencyResolution = ResolutionScope.TEST)
public class BNCompilerMojo extends AbstractMojo {
    private final static String version = "1.6.0";

    @Parameter(required = false, defaultValue = "/modules")
    private String modulesPath;

    @Parameter(required = true)
    private String moduleName;

    @Parameter(required = false, defaultValue = "output/")
    private String outputDir;

    @Parameter(required = true)
    private String inputFileName;

    @Parameter(required = true)
    private String namespace;

    @Parameter(required = false)
    private Boolean generateModelOnly;

    @Parameter(readonly = true, defaultValue = "${project}")
    private MavenProject project;

    private String targetDirectory = "./";

    public BNCompilerMojo() {
        generateModelOnly = false;
    }

    private void createModel(OutputStream outputXml, Module module)
        throws PropertyException, Exception, JAXBException {
        JAXBContext jc = JAXBContext.newInstance("org.bn.compiler.parser.model");
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        ASN1Model model = createModelFromStream();

        if (module != null) {
            model.moduleDirectory = module.getModulesPath() + File.separator + module.getModuleName();
            model.outputDirectory = module.getOutputDir();
            if (namespace != null)
                model.moduleNS = namespace;
            else
                model.moduleNS = model.module.moduleIdentifier.name.toLowerCase();
        }
        marshaller.marshal(model, outputXml);

    }

    private InputStream retrieveASN1file(String path) throws URISyntaxException, IOException {
        InputStream ret = null;
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            ret = new FileInputStream(path);
        } else {
            URL resource = this.getClass().getResource(path);
            if (resource != null) {
                String inputFile = resource.getFile();
                ret = new FileInputStream(inputFile);
            } else {
                throw new FileNotFoundException("Wrong asn1 input file " + path);
            }
        }
        return ret;
    }

    private ASN1Model createModelFromStream() throws Exception {
        InputStream stream = retrieveASN1file(inputFileName);
        ASNLexer lexer = new ASNLexer(stream);
        ASNParser parser = new ASNParser(lexer);
        ASNModule module = new ASNModule();

        parser.module_definition(module);

        ASN1Model model = new ASN1Model();

        model.module = module;

        return model;
    }

    public void execute() throws MojoExecutionException {
        try {
            modulesPath = modulesPath.replace("\\", "/");
            System.out.println("BinaryNotes compiler v" + version);
            System.out.println("        (c) 2006-2011 Abdulla G. Abdurakhmanov");
            System.out.println("modulesPath = " + modulesPath);
            System.out.println("moduleName = " + moduleName);
            System.out.println("outputDir = " + outputDir);
            System.out.println("inputFileName = " + inputFileName);
            System.out.println("namespace = " + namespace);
            System.out.println("generateModelOnly = " + generateModelOnly);
            start();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public void start() throws Exception {
        String outputDireWithNamespace = outputDir + File.separator + namespace.replace(".", File.separator);
        if (project != null) {
            Model model = project.getModel();
            Build build = model.getBuild();
            targetDirectory = build.getDirectory();
        }
        Module module = new Module(modulesPath, moduleName, outputDireWithNamespace, targetDirectory);
        startForModule(module);
    }

    private void startForModule(Module module) throws Exception {
        if (!generateModelOnly) {
            System.out.println("Current directory: " + new File(".").getCanonicalPath());
            System.out.println("Compiling file: " + inputFileName);
            ByteArrayOutputStream outputXml = new ByteArrayOutputStream(65535);
            createModel(outputXml, module);
            InputStream stream = new ByteArrayInputStream(outputXml.toByteArray());
            module.translate(stream);
        } else {
            createModel(System.out, null);
        }
    }
}
