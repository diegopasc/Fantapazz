#!/usr/bin/perl

$JAVA_HOME="/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home";
$LIB_DIR = "lib";

if ( scalar(@ARGV) != 2 ) {
	print "\nusage: perl bin/fantapazz-asta.pl <AstaID> <Port>\n\n";
	exit 0;
}

$astaId = shift(@ARGV);
$port = shift(@ARGV);

$command = "${JAVA_HOME}/bin/java -Dfile.encoding=MacRoman -classpath ";

@list = `ls $LIB_DIR`;
foreach $row (@list) {
	chop($row);
	$command .= "$LIB_DIR/$row:";
}

$command .= "conf/";
$command .= " it.fantapazz.FantapazzAstaMain $astaId $port";

# print $command;

system($command);
