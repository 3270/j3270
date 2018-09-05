j3270.base
==========

Provides a simple wrapper around a [s3270](http://x3270.bgp.nu/Unix/s3270-man.html "s3270") process.

The primary mode of interaction is using the [J3270 class](src/main/java/com/j3270/base/J3270.java), that offers a type safe access to the underlying [s3270](http://x3270.bgp.nu/Unix/s3270-man.html "s3270") process, see [CallIT](src/test/java/com/j3270/base/J3270IT.java) for an example. 

It's possible to build a series of [Call objects](src/main/java/com/j3270/external/Call.java), each representing an arbitrary action call, see [CallIT](src/test/java/com/j3270/external/CallIT.java) for an example.