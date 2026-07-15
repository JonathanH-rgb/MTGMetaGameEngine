#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"

for i in $(seq 1 30); do
  code=$(curl -s -o /dev/null -w '%{http_code}' "$BASE_URL/actuator/health" || true)
  [ "$code" = "200" ] && {
    echo "ready"
    break
  }
  sleep 1
  [ "$i" = "30" ] && {
    echo "app never became ready"
    exit 1
  }
done
