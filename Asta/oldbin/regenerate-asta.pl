#!/usr/bin/perl

`mvn clean install assembly:assembly`;

`mkdir target/Asta-0.0.1-SNAPSHOT-AstaOnline/aste/libs_signed`;

`jarsigner -storepass fantapazz -keystore fantapazz.keystore -signedjar target/Asta-0.0.1-SNAPSHOT-AstaOnline/aste/libs_signed/Asta-0.0.1-SNAPSHOT_signed.jar target/Asta-0.0.1-SNAPSHOT-AstaOnline/aste/lib/Asta-0.0.1-SNAPSHOT.jar fantapazz`;

`cp target/Asta-0.0.1-SNAPSHOT-AstaOnline/aste/libs_signed/Asta-0.0.1-SNAPSHOT_signed.jar /Users/mastrogiovannim/Sites/fantapazz/libs_signed/`;

