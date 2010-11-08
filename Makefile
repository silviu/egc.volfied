.SUFFIXES : .class .java
.PHONY : clean build run
GREP:=/bin/grep --color=always
SRC := $(shell echo *.java)
CLS := $(patsubst %.java,%.class, $(filter %.java,$(SRC)))

DEPS:=
FLAGS=-g
#FLAGS=-verbose

run-gui:build
	java Volfied
run:build
	nohup chromium-browser Main.htm&

build:$(CLS)

.java.class:
	javac $(FLAGS) $^
clean:
	rm -f *.class *.o *~ *.swp *.out
