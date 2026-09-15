#!/bin/bash
# NOX requires python components to be registered in its source tree
mkdir -p /opt/nox/src/nox/coreapps/simplehub
cp /workspace/simplehub.py /opt/nox/src/nox/coreapps/simplehub/simplehub.py

# Create the required meta.json for the NOX component registry
cat <<EOF > /opt/nox/src/nox/coreapps/simplehub/meta.json
{
    "components": [
        {
            "name": "simplehub",
            "python": "nox.coreapps.simplehub.simplehub"
        }
    ]
}
EOF

echo "Rebuilding NOX to register simplehub..."
cd /opt/nox/build
make -j4

echo "Starting NOX controller with simplehub..."
cd /opt/nox/build/src
./nox_core -v -i ptcp:6633 simplehub
