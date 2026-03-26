#!/bin/sh

APP_BASE_NAME=${0##*/}
APP_HOME=$( cd "${0%/*}" && pwd )

DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD=maximum

GRADLE_HOME="${APP_HOME}/gradle"

exec "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
