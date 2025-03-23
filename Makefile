vpath %.java src

sources = $(wildcard src/*.java)
classes = $(patsubst src/%, %, $(sources:.java=.class))
classes_out = $(patsubst %, out/%, $(classes))

all: $(classes_out)

out/%.class: %.java | out
	javac -d out -classpath src $<

out:
	mkdir -p out

clean:
	rm -rf out empty.png solution.png crossword.jar

run: all
	java -cp out Main

jar: all
	jar cvfe crossword.jar Main -C out .

.PHONY: all run jar clean