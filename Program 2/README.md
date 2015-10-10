#CMPSC470-Project-FultzKallamadi

Doug Fultz

Sujay Kallamadi

## Running JFlex from command line
1. Get the help text.
http://jflex.de/manual.html#running-jflex
`# java -jar JFlex.jar --help`
2. Run JFlex against input file.
`# java -jar JFlex.jar csx.jflex`

## Compile and execute code (Yylex)
1. Change to directory containing Yylex.java
`# cd PATH/TO/SOURCE`
2. Compile program
`# javac ./Yylex.java`
3. Execute program
`# java Yylex PATH/TO/CODE.csx`

### Shortcut
1. Change to directory containing P1.java
`# cd PATH/TO/SOURCE`
2. Compile and execute in one big command
`# prog="P2"; javac ./$prog.java && java $prog`

## Configure GitHub on Sun Machine
1. Set you GitHub username
`# git config --global user.name "USERNAME"`
2. Set your email address
`# git config --global user.email "USERNAME@users.noreply.github.com"`
3. Configure push type to simple
`# git config --global push.default simple`
4. Configure credentials to be cached for 1 hour (OPTIONAL)
`# git config --global credential.helper 'cache --timeout=3600'`
4. Clone the repository
`# git clone https://github.com/dougfultz/CMPSC470-Project-FultzKallamadi.git`

## Common git commands
- Fetch updated code from GitHub
`# git fetch`
- Show changed files
`# git status`
- Show changes since last commit
`# git diff`
- Commit all changes, with a message
`# git commit -a -m "COMMIT MESSAGE"`
- Push all local commits to GitHub
`# git push`

