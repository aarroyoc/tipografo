compile:
    javac -d classes src/*.java

run:
    cd classes && java Main

sync:
    rsync -avh output/ /data/webs/tipografo-output
