from pox.core import core
import pox.openflow.libopenflow_01 as of

log = core.getLogger()

def _handle_PacketIn(event):
    """Triggered when a switch forwards an unmatched packet to the controller."""
    
    # Create a message to send instructions back to the switch
    msg = of.ofp_packet_out()
    
    # CONTROL: Add an action to the message (Flood / broadcast the packet)
    msg.actions.append(of.ofp_action_output(port = of.OFPP_FLOOD))
    
    # Crucial: Attach the original packet data so the switch knows WHAT to flood!
    msg.data = event.ofp
    msg.in_port = event.port
    
    # Send the instruction back to the specific switch that asked
    event.connection.send(msg)
    log.debug("Packet flooded out of all ports")

def launch():
    """Starts the application."""
    # OBSERVATION: Register to listen for PacketIn events
    core.openflow.addListenerByName("PacketIn", _handle_PacketIn)
    log.info("SimpleHub application running and listening for packets!")
