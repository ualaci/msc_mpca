    from nox.lib.core import Component
    import nox.lib.openflow as openflow

    class SimpleHub(Component):
        """A basic NOX application that makes a switch act like a network Hub."""

        def __init__(self, ctxt):
            Component.__init__(self, ctxt)

        def install(self):
            # OBSERVATION PHASE:
            # Tell NOX we want this app to receive 'Packet-In' events.
            # (This happens when a switch doesn't know what to do with a packet
            # and forwards it to the controller, as seen in the first diagram).
            self.register_for_packet_in(self.packet_in_handler)

        def getInterface(self):
            return str(SimpleHub)

        def packet_in_handler(self, dpid, inport, reason, length, bufid, packet):
            """This function runs every time an unmatched packet arrives."""

            # 'dpid' is the ID of the switch that sent the packet.
            # 'inport' is the port the packet arrived on.

            # CONTROL PHASE: Define the OpenFlow Action.
            # OFPP_FLOOD means "forward this out of all ports EXCEPT the incoming port"
            action = [[openflow.OFPAT_OUTPUT, [0, openflow.OFPP_FLOOD]]]

            # Send the instruction back to the switch to execute the action
            self.send_openflow_packet(dpid, bufid, packet, action, inport)

            # Tell NOX to continue processing other potential events
            return CONTINUE