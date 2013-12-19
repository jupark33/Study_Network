package snmp;

import java.net.InetAddress;
import java.util.List;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

public class SubTreeExample {

	private void getError() throws Exception {
		String ip = "127.0.0.1";
		String[] oids = {
				".x.x.x.x.x.x.x.x.x.x.x.x.x.x.x"
		};

		PDU pdu = new PDU();

		for (int i = 0; i < oids.length; i++) {
			pdu.add(new VariableBinding(new OID(oids[i])));
		}
		pdu.setType(PDU.GETNEXT);

		CommunityTarget target = new CommunityTarget();
		UdpAddress targetAddress = new UdpAddress();
		targetAddress.setInetAddress(InetAddress.getByName(ip));
		targetAddress.setPort(161);
		target.setAddress(targetAddress);
		target.setCommunity(new OctetString("public"));
		target.setVersion(SnmpConstants.version1);

		TransportMapping transport = new DefaultUdpTransportMapping();
		Snmp snmp = new Snmp(transport);
		transport.listen();
		
		OID oid = new OID(oids[0]);
		
		TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
		List<TreeEvent> events = treeUtils.getSubtree(target, oid);
		if (events == null || events.size() == 0) {
			System.out.println("No result, events");
			return;
		}
		
		for (TreeEvent event : events) {
			if (event != null) {
				if (event.isError()) {
					System.out.println("error oid[" + oid + "]:" + event.getErrorMessage());
				}
				
				VariableBinding[] varBindings = event.getVariableBindings();
				if (varBindings == null || varBindings.length == 0) {
					System.out.println("No result, varBindings");
				}
				
				for (VariableBinding varBinding : varBindings) {
					System.out.println(varBinding.getOid() + ", " + varBinding.getVariable().getSyntaxString() + ", " + varBinding.getVariable());
				}
			}
		}
		
		snmp.close();
		
	}
	
	public static void main(String[] args) {
		
		SubTreeExample ste = new SubTreeExample();
		try {
			ste.getError();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}