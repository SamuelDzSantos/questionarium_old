#!/bin/bash
set -e

# Create the auth_db database by inserting a dummy document
mongosh <<EOF
use questionarium_auth
db.createCollection("init_collection")
db.init_collection.insertOne({ initialized: true })
EOF