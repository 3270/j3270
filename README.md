j3270: Simple tn3270 Support for Java
=====================================

j3270 is a set of Java libraries that uses [s3270](http://x3270.bgp.nu/Unix/s3270-man.html "s3270"), exposing its capabilities to software running on the JVM.

The design is layered, instead of having a single "high-level" library for using the tn3270 protocol, j3270 provides a [base](base) layer that wraps over [s3270](http://x3270.bgp.nu/Unix/s3270-man.html "s3270") and additional libraries built on top. This reduces the problems of trying to build an entire tn3270 implementation from the ground up and building a complete API on top of it. Anything that can be done using [s3270](http://x3270.bgp.nu/Unix/s3270-man.html "s3270") should be possible to do using the same API in [j3270's wrapper layer](base).
