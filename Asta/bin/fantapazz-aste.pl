#!/usr/bin/perl

$JAVA_HOME="/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home";

$LIB_DIR = "lib";

$PWD=`pwd`;
chop($PWD);
$ENV{'JWEBSOCKET_HOME'} = $PWD;

$command = "${JAVA_HOME}/bin/java -Dfile.encoding=MacRoman -classpath ";

@list = `ls $LIB_DIR`;
foreach $row (@list) {
	chop($row);
	$command .= "$LIB_DIR/$row:";
}

$command .= "conf/";
$command .= " it.fantapazz.WebSocketAsta";

# print $command;

system($command);

