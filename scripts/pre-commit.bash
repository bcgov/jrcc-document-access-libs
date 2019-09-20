#!/bin/bash

# save the file as <git_directory>/.git/hooks/pre-commit

echo "Running Maven clean installl for build errors"
# retrieving current working directory
CWD=`pwd`
MAIN_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# go to main project dir
cd $MAIN_DIR/../../
# running maven clean install
mvn clean install
if [ $? -ne 0 ]; then
  "Error while testing the code"
  # go back to current working dir
  cd $CWD
  exit 1
fi
# go back to current working dir
cd $CWD