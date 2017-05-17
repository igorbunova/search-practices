#!/bin/bash
echo "Args: $@"

ROOT_PATH=$(cd $(dirname $0) && pwd);
echo $ROOT_PATH;
cd $ROOT_PATH
exec java -classpath "search-practices.jar:../lib/*:../config" com.igorbunova.examples.Main "$@"