#!/bin/bash

# Connect:Direct server details
CD_HOST="connect_direct_server"
CD_USER="connect_direct_username"
CD_PASS="connect_direct_password"

# Google File Transfer Hub details
GCP_PROJECT="google_project"
GCP_LOCATION="google_location"
GCP_BUCKET="google_bucket"

# File details
SOURCE_FILE="/path/to/source_file"
DESTINATION_FILE="destination_file"

# Connect:Direct transfer command
transfer_command="submit transfer \
source=($SOURCE_FILE) \
destination=($GCP_PROJECT, $GCP_LOCATION, $GCP_BUCKET, $DESTINATION_FILE) \
compress=no \
compress-extension=none \
stripe-count=1"

# Run the Connect:Direct transfer command
echo "Transferring file from Connect:Direct to Google File Transfer Hub..."
echo "Command: $transfer_command"
ndmcli -x "$transfer_command"

# Check the exit status of the transfer
exit_status=$?
if [ $exit_status -eq 0 ]; then
    echo "File transfer successful."
else
    echo "File transfer failed. Exit status: $exit_status"
fi