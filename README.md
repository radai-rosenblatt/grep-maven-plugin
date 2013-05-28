grep-maven-plugin
=================

a maven plugin that greps through files and prints the results

usage example:

<pre>
<code>
&lt;build&gt;
    &lt;plugins&gt;
		&lt;plugin&gt;
			&lt;groupId&gt;net.radai&lt;/groupId&gt;
			&lt;artifactId&gt;grep-maven-plugin&lt;/artifactId&gt;
			&lt;version&gt;1.0-SNAPSHOT&lt;/version&gt;
			&lt;executions&gt;
				&lt;execution&gt;
					&lt;goals&gt;
						&lt;goal&gt;grep&lt;/goal&gt;
					&lt;/goals&gt;
					&lt;phase&gt;test&lt;/phase&gt;
					&lt;configuration&gt;
						&lt;greps&gt;
							&lt;grep&gt;
								&lt;file&gt;src/main/resources/file.txt&lt;/file&gt;
								&lt;grepPattern&gt;prop1&lt;/grepPattern&gt;
							&lt;/grep&gt;
							&lt;grep&gt;
								&lt;file&gt;src/main/resources/archive.zip/directory/file.txt&lt;/file&gt;
								&lt;grepPattern&gt;prop1&lt;/grepPattern&gt;
								&lt;outputPattern&gt;found in file ${fileName} at line ${lineNumber} : ${line}&lt;/outputPattern&gt;
							&lt;/grep&gt;
						&lt;/greps&gt;
					&lt;/configuration&gt;
				&lt;/execution&gt;
			&lt;/executions&gt;
		&lt;/plugin&gt;
	&lt;/plugins&gt;
&lt;/build&gt;
</code>
</pre>
