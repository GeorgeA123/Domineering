#!/bin/bash
shopt -s expand_aliases

checkstyleJarName="checkstyle-6.15-all.jar"
checkstyleConfigFile="style_checks.xml"

# locate the directory that contains this script
myLocation=$(cd $(dirname $0) && pwd -P)

checkstyleJar=${myLocation}/${checkstyleJarName}
checkstyleConfig=${myLocation}/${checkstyleConfigFile}

tracker="${myLocation}/track_game.py"

# execute java code in a sandbox
alias sandjava="java -Djava.security.manager"

testsDir=${myLocation}/bst-tests

base=$(pwd)

gradedir=$2
if [ -z $2 ]
then
    gradedir="${base}/reports"
fi

our_src="${myLocation}/src-override/Board.java ${myLocation}/src-override/GameTree.java ${myLocation}/src-override/MoveChannel.java ${myLocation}/src-override/Player.java"

junit="${myLocation}/dependencies/junit-4.12.jar"
hamcrest="${myLocation}/dependencies/core-1.3.jar"
javaRuntype="${myLocation}/dependencies/javaruntype-1.2.jar"
quickcheckCore="${myLocation}/dependencies/junit-quickcheck-core-0.6-beta-1.jar"
quickcheckGenerators="${myLocation}/dependencies/junit-quickcheck-generators-0.6-beta-1.jar"
slf4j="${myLocation}/dependencies/slf4j-api-1.7.18.jar"
slf4jSimple="${myLocation}/dependencies/slf4j-simple-1.7.18.jar"
generics="${myLocation}/dependencies/generics-resolver-2.0.1.jar"
antlr="${myLocation}/dependencies/antlr-4.5.2-complete.jar"
ognl="${myLocation}/dependencies/ognl-3.1.2.jar"


mkdir -p $gradedir

zipfile=$1

if [ -z $1 ]
then
    defaultZip=$(ls *.zip|head -1)
    read -p "No zipfile supplied. Use default '${defaultZip}'? [Y/n]: " useDefaultZip
    case $useDefaultZip in
	n) exit;;
	*) zipfile=$defaultZip;;
    esac
fi

if [[ "$zipfile" =~ [^a-zA-Z0-9._-] ]]; then
    echo "INVALID filename, please restrict to [a-zA-Z0-9._-]. In particular, don't use spaces in your filename."
    exit
fi

zipdir="${base}/.${zipfile}.tmp"

gradefile="${gradedir}/${zipfile%.zip}.txt"

# Clean gradefile
rm $gradefile 2> /dev/null

# Clean existing tmp dir
rm -r $zipdir 2> /dev/null

alias mark="cat >> '$gradefile'"
die() { echo "ERROR: $@" | tee -a $gradefile ; exit 1; }
lineTop() { echo "=================================================================" | mark ; }
lineBot() { lineTop ; echo | mark ; }


echo "Unzipping..." | mark

unzip -n -j -q $zipfile -d $zipdir || die "Could not read zipfile ${zipfile}"
# Deleting annoying files generated on OSX
cd ${zipdir}
rm ./._* -f 2> /dev/null
rm -r ./._* -f 2> /dev/null

echo "Extracted ${zipfile}" | mark
echo | mark

## START file sanitation
# Replace the interface files with our own to make sure students did not modify them.
cp $our_src $zipdir
## END file sanitation

## START compilation
# This gathers all the files and compiles them.
# TODO: replace students' interface files with our own
echo "Compiling..." | mark

lineTop
build_dir=${zipdir}/build
mkdir -p $build_dir
classpath=${build_dir}:${junit}:${hamcrest}:${javaRuntype}:${slf4j}:${generics}:${antlr}:${ognl}:${quickcheckCore}:${quickcheckGenerators}:${slf4jSimple}
compilation_output=$((javac -cp $classpath -Xlint -d $build_dir ${zipdir}/*.java) 2>&1)
echo "$compilation_output" | mark
lineBot
## END compilation


# marking code goes here. note that the *.java and *.class files are in the pwd at this point
# use sandjava to execute classes in a sandbox (to prevent students from setting your cat on fire and launching the missiles)


## START junit tests
run_tests()
{
    if [ -z "$1" ]
    then
        my_tests=$TESTS
    else
        my_tests="$1"
    fi
    echo Running the following tests: | mark
    echo $my_tests | mark
    tests_passed="0"
    tests_ran=0
    for test in $my_tests
    do
        output=$(java -cp $classpath org.junit.runner.JUnitCore $test)
        scores=$((echo " = award 0" && echo "${output}") | grep " = award [.0-9]*$" | sed -r 's/.* award ([.0-9]*)$/\1/' | paste -sd+ | bc)
        tests_passed=$(echo $tests_passed + $scores | bc)
        # if [[ $? == 0 ]]
        # then
        #     tests_passed=$((tests_passed+1))
        # fi
        tests_ran=$((tests_ran+1))
        echo "$output" | mark
    done
    echo $tests_passed
}
echo "Compiling unit tests..." | mark
lineTop
compilation_output=$((javac -cp $classpath -Xlint -d $build_dir ${myLocation}/tests4students/*.java) 2>&1)
echo "$compilation_output" | mark
lineBot
echo "Running unit tests..." | mark
lineTop
cp -r ${myLocation}/test_data $zipdir
cd $zipdir
java -cp ${classpath} org.junit.runner.JUnitCore TestsDomineeringClean | mark
cd - >/dev/null
# other_score=$( run_tests "$OTHER_TESTS" )
lineBot
## END junit tests

### START final report
echo | mark
echo "----------------------------" | mark
echo "End of part 1 marking script" | mark
echo "----------------------------" | mark
echo | mark

# Length of compilation output is an indication of code quality
# echo "Compilation output size: $(echo -n "$compilation_output" | wc -l) lines (expected 0)" | mark

# number of passed tests
### END final report

cd ..

rm -r ${zipdir}
