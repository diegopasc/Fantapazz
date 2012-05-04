#!/usr/bin/perl

$JAVA_HOME="/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home";
$LIB_DIR = "lib";

if ( scalar(@ARGV) != 3 ) {
	print "\nusage: perl bin/fake-player.pl <ID> <Host> <Port>\n\n";
	exit 0;
}

$ID = shift(@ARGV);
$host = shift(@ARGV);
$port = shift(@ARGV);

$command = "${JAVA_HOME}/bin/java -Dfile.encoding=MacRoman -classpath ";

@list = `ls $LIB_DIR`;
foreach $row (@list) {
	chop($row);
	$command .= "$LIB_DIR/$row:";
}

$command .= "conf/";
$command .= " it.fantapazz.test.core.FakePlayer $ID $host $port";

# print $command;

system($command);
