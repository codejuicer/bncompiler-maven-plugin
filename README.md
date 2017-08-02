bncompiler-maven-plugin
===========

maven plugin to generate java classes from asn1 model. 
This project is a fork of BinaryNotes compiler project.
Please refer to 

http://bnotes.sourceforge.net/



## Installation

bncompiler plugin is available at [Maven Central] (http://search.maven.org/#browse%7C-1189551211).

### Integration with Maven

To use the official release of java2csharp, please use the following snippet in your pom.xml

```xml
    <build>
		<plugins>
			<plugin>
				<groupId>org.codejuicer</groupId>
				<artifactId>bncompiler-maven-plugin</artifactId>
				<version>1.6.0-SNAPSHOT</version>
				<executions>
					<execution>
						<id>generate-sources</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>bncompiler</goal>
						</goals>
						<configuration>
							<moduleName>java</moduleName>
							<namespace>your namespace</namespace>
							<inputFileName>path to your asn1 model file</inputFileName>
							<outputDir>path to your output directory</outputDir>
						</configuration>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
```
