grep-maven-plugin
=================

a maven plugin that greps through files and prints the results

usage example:

<pre>

    <build>
        <plugins>
            <plugin>
                <groupId>net.radai</groupId>
                <artifactId>grep-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>grep</goal>
                        </goals>
                        <phase>test</phase>
                        <configuration>
                            <greps>
                                <grep>
                                    <file>src/main/resources/file.txt</file>
                                    <grepPattern>prop1</grepPattern>
                                </grep>
                                <grep>
                                    <file>src/main/resources/archive.zip/directory/file.txt</file>
                                    <grepPattern>prop1</grepPattern>
                                    <outputPattern>found in file ${fileName} at line ${lineNumber} : ${line}</outputPattern>
                                </grep>
                            </greps>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</pre>