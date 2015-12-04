# CMPSC470 - Project 4 - Type Checker

Doug Fultz

Sujay Kallamadi

## Requirements

1. Sun Lab machine

## How To: CSX

1. Process CUP file

   `# java -jar tools/java-cup-10l.jar csx.cup`

2. Process JFlex file

   `# java -jar tools/JFlex.jar csx.jflex`

3. Compile java files

   `# javac -cp tools/java-cup-10l.jar:tools/JFlex.jar:./ *.java`

4. Execute P4 program against test file

   `# java -cp tools/java-cup-10l.jar:tools/JFlex.jar:./ P4 test.csx`

5. Clean up directory

   `# rm ./*.class`

## How To: CSX lite

1. Process CUP file

   `# java -jar tools/java-cup-10l.jar lite.cup`

2. Process JFlex file

   `# java -jar tools/JFlex.jar lite.jflex`

3. Compile java files

   `# javac -cp tools/java-cup-10l.jar:tools/JFlex.jar:./ *.java`

4. Execute P4 program against test file

   `# java -cp tools/java-cup-10l.jar:tools/JFlex.jar:./ P4 test.lite`

5. Clean up directory

   `# rm ./*.class`
