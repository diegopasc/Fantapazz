#!/usr/bin/perl

$JAVA_HOME="/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home";
$LIB_DIR = "lib";

$command = "${JAVA_HOME}/bin/java -Dfile.encoding=MacRoman -classpath ";

@list = `ls $LIB_DIR`;
foreach $row (@list) {
	chop($row);
	$command .= "$LIB_DIR/$row:";
}

$command .= "conf/";
$command .= " other.fantapazz.GUIMain";

# print $command;

system($command);
