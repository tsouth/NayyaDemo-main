#!/bin/bash

test_method="surefire:test"
while getopts d:s:e:l flag
do
    case "${flag}" in
        d)  driver=${OPTARG};;

        s)  suite=${OPTARG};;

        e)  event=${OPTARG};;

        l)  eval nextopt=${!OPTIND}
            if [[ -n ${nextopt} && ${nextopt} != -* ]]; then
                OPTIND=$((OPTIND + 1))
            fi
            test_method="test"
            ;;

        *)  break;;
    esac
done

if [[ ${event} == smoke_tests ]]; then
  suite="smokeTests"
fi

DRIVER="${driver}" EVENT="${event}" EXECUTION="${execution}" mvn -P suite -DsuiteFile="${suite}".xml "${test_method}" --errors
