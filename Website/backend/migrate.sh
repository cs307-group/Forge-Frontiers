#!/bin/bash

# Check the number of arguments provided
if [[ $# -lt 1 ]]; then
    echo "Usage: migrate.sh <command> [args]"
    echo "Commands:"
    echo "  init"
    echo "  migrate <message>"
    exit 1
fi

# Get the command from the first argument
command="$1"

# Execute the appropriate command
if [[ "$command" == "init" ]]; then
    flask --app runner:core_app db init
elif [[ "$command" == "migrate" ]]; then
    # Check that a message was provided
    if [[ $# -lt 2 ]]; then
        echo "Usage: migrate.sh migrate <message>"
        exit 1
    fi
    # Get the message from the second argument
    message="$2"
    flask --app runner:core_app db migrate -m "$message"
elif [[ "$command" == "upgrade" ]]; then
    flask --app runner:core_app db  upgrade
else
    echo "Invalid command: $command"
    exit 1
fi

exit 0
