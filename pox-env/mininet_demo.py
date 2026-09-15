from mininet.net import Mininet
from mininet.node import RemoteController, OVSKernelSwitch
from mininet.topo import SingleSwitchTopo
from mininet.log import setLogLevel
import time
import os

setLogLevel('info')
# Create 1 Switch and 3 Hosts, connecting to our POX controller
net = Mininet(topo=SingleSwitchTopo(3), switch=OVSKernelSwitch, controller=lambda name: RemoteController(name, ip='127.0.0.1', port=6633), autoSetMacs=True)
net.start()

print("\n[*] Waiting 5 seconds for switch to establish TCP connection to POX...")
time.sleep(5)

h1, h2 = net.get('h1', 'h2')

print("\n=======================================================")
print("[*] PING 1: Host 1 -> Host 2 (Table Miss -> Controller)")
print("=======================================================")
print(h1.cmd('ping -c 1 ' + h2.IP()))

print("\n=======================================================")
print("[*] CHECKING SWITCH HARDWARE: The Controller installed a Flow Entry!")
print("=======================================================")
os.system('ovs-ofctl dump-flows s1 -O OpenFlow10')

print("\n=======================================================")
print("[*] PING 2: Host 1 -> Host 2 (Matches Entry -> Handled by Switch)")
print("=======================================================")
print(h1.cmd('ping -c 1 ' + h2.IP()))

print("\n[*] Demo sequence complete. Shutting down virtual network...")
net.stop()
