#!/bin/ksh

# Connect:Direct details
CDIRECT_BIN="/cdirect/ndm/bin/direct"
CDIRECT_JOB_FILE="/path/to/cdirect_job.txt"

# Source file details
FROM_FILE="source_file.txt"
SOURCE_NODE="source_node"

# Destination file details
TO_FILE="destination_file.txt"
DESTINATION_PATH="/path/to/destination"
DESTINATION_NODE="destination_node"

# Create the Connect:Direct job definition file
echo "submit" > $CDIRECT_JOB_FILE
echo "testjob process snode=${SOURCE_NODE}" >> $CDIRECT_JOB_FILE
echo "    step1" >> $CDIRECT_JOB_FILE
echo "    copy from (file=${FROM_FILE} pnode)" >> $CDIRECT_JOB_FILE
echo "         to (dsn=${DESTINATION_NODE},${DESTINATION_PATH}/${TO_FILE})" >> $CDIRECT_JOB_FILE
echo "pend;" >> $CDIRECT_JOB_FILE

# Submit the Connect:Direct job
$CDIRECT_BIN < $CDIRECT_JOB_FILE

# Check the exit status of the Connect:Direct job
exit_status=$?
if [ $exit_status -eq 0 ]; then
    echo "File copy successful."
else
    echo "File copy failed. Exit status: $exit_status"
fi

# Clean up the Connect:Direct job definition file
rm $CDIRECT_JOB_FILE
