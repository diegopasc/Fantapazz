#!/usr/bin/perl

$targetDir = "target/Asta-0.0.1-SNAPSHOT-AstaOnline/aste";

`cp site/index.html $targetDir`;

`cp site/app_begin.jnlp $targetDir/app.jnlp`;

`mkdir $targetDir/libs_signed`;

@list = `ls $targetDir/lib/`;

open( OUT, ">> $targetDir/app.jnlp" );
foreach $row (@list) {
	chop($row);
	if ( $row =~ /(.*?)\.jar$/ ) {
		$file = $1;
		`jarsigner -storepass fantapazz -keystore fantapazz.keystore -signedjar $targetDir/libs_signed/$1_signed.jar $targetDir/lib/$1.jar fantapazz`;
		# print "File: '$1'.jar\n";
		if ( $row =~ /Asta-0.0.1-SNAPSHOT/ ) {
			print OUT "\t<jar href=\"libs_signed/${file}_signed.jar\" main=\"true\" />\n";
		}
		else {
			print OUT "\t<jar href=\"libs_signed/${file}_signed.jar\" main=\"false\" />\n";
		}
	}
}
close( OUT );

`cat site/app_end.jnlp >> $targetDir/app.jnlp`;


