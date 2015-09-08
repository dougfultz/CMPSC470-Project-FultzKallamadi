#CMPSC470-Project-FultzKallamadi

Doug Fultz

Sujay Kallamadi

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

## Compile and execute code
1. Change to directory containing P1.java
`# cd PATH/TO/SOURCE`
2. Compile program
`# javac ./P1.java`
3. Execute program
`# java P1`

### Shortcut
1. Change to directory containing P1.java
`# cd PATH/TO/SOURCE`
2. Compile and execute in one big command
`# prog="P1"; javac ./$prog.java && java $prog`
