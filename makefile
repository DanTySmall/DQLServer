##
# Project Title
#
# @file
# @version 0.1

make:
	javac -g Parser.java DQLServer.java
	java DQLServer

test:
	javac -g Parser.java Test.java
	java Test


# end
