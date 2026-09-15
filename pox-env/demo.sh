#!/bin/bash

echo "========================================="
echo " Starting Software-Defined Network Demo  "
echo "========================================="

# 1. Start the Open vSwitch background service (required by Mininet)
service openvswitch-switch start

# Clean up any previous Mininet instances just in case
mn -c > /dev/null 2>&1

# Copy our simplehub application into POX's extension directory
cp /workspace/simplehub.py /opt/pox/ext/simplehub.py

# 2. Start the POX Controller in the background
echo "[*] Starting POX Controller with 'simplehub' application..."
# We run it in the background and save the log to a file
export PYTHONPATH=/opt/pox:$PYTHONPATH
python3 /opt/pox/pox.py log.level --DEBUG simplehub > /workspace/pox.log 2>&1 &
POX_PID=$!

# Wait a moment for the controller to boot up
sleep 3
echo "[*] Controller is running! (Logs saved to pox-env/pox.log)"

# 3. Start Mininet
# --topo single,3 : Creates 1 switch connected to 3 hosts
# --mac : Assigns simple MAC addresses (00:00:00:00:00:01, etc.)
# --controller remote : Tells the switch to look for our POX controller on port 6633
# --test pingall : Automatically makes every host ping every other host to test connectivity
echo "[*] Starting Mininet Virtual Network (1 Switch, 3 Hosts)..."
echo "[*] Running Ping Test between all hosts..."

mn --topo single,3 --mac --controller remote,ip=127.0.0.1,port=6633 --test pingall

# 4. Clean up and Shutdown
echo "[*] Shutting down Controller and Network..."
kill $POX_PID
mn -c > /dev/null 2>&1

echo "========================================="
echo " Demo Complete!"
echo " Check 'pox.log' to see the Controller's exact thought process!"
echo "========================================="
