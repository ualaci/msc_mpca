from nox.lib.core import Component
import nox.lib.openflow as openflow
from nox.lib.core import CONTINUE

class simplehub(Component):
    """A basic NOX application that makes a switch act like a network Hub."""

    def __init__(self, ctxt):
        Component.__init__(self, ctxt)

    def install(self):
        # Register to listen for 'Packet-In' events
        self.register_for_packet_in(self.packet_in_handler)
        print("SimpleHub app installed and waiting for packets...")

    def getInterface(self):
        return str(simplehub)

    def packet_in_handler(self, dpid, inport, reason, length, bufid, packet):
        # OFPP_FLOOD means "forward this out of all ports EXCEPT the incoming port"
        action = [[openflow.OFPAT_OUTPUT, [0, openflow.OFPP_FLOOD]]]

        # Send the instruction back to the switch to execute the action
        self.send_openflow_packet(dpid, bufid, packet, action, inport)

        return CONTINUE
