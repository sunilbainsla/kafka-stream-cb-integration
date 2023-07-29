#!/bin/ksh

# Script to simulate the file arrival event

# Path to the file to be considered as the arrival event
file_path="/path/to/arrival_file.txt"

# Check if the file exists
if [[ -f "$file_path" ]]; then
    echo "File has arrived: $file_path"
    exit 0  # Success
else
    echo "File has not arrived yet"
    exit 1  # Failure
fi

(cd /cdirect/ndm/bin/; ./direct) << EOJ
submit
testjob process snode=${snode}
    step1
    copy from (file=${fromfile} pnode)
         to (dsn=${tofile})
pend;
EOJ