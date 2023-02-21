#!/bin/bash

path=$1

detection_criteria=()

if [ -f "$path/Makefile" ]; then
    detection_criteria+=('C')
fi

if [ -f "$path/app/pom.xml" ]; then
    detection_criteria+=('Java')
fi

if [ -f "$path/app/main.bf" ]; then
    detection_criteria+=('Befunge')
fi

if [ -f "$path/package.json" ]; then
    detection_criteria+=('Javascript')
fi

if [ -f "$path/requirements.txt" ]; then
    detection_criteria+=('Python')
fi

if [ ${#detection_criteria[@]} -eq 0 ]; then
    echo "Unknown"
    exit 84
elif [ ${#detection_criteria[@]} -eq 1 ]; then
    echo "${detection_criteria[0]}"
else
    echo "A single Whanos-compatible repository cannot match multiple detection criterion at the same time."
    exit 84
fi

# Usage example
#language=$(detect_language '/path/to/repository')
#echo "Language detected: $language"

