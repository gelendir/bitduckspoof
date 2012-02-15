package org.bitducks.spoofing.services;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import jpcap.packet.ARPPacket;
import jpcap.packet.Packet;

import org.bitducks.spoofing.core.Server;
import org.bitducks.spoofing.core.Service;
import org.bitducks.spoofing.customrules.ARPReplyReceiverRule;
import org.bitducks.spoofing.packet.PacketGenerator;

public class ARPQueryService extends Service {

	private ARPReplyReceiverRule rule = new ARPReplyReceiverRule();
	private PacketGenerator arpGen = new PacketGenerator(Server.getInstance().getInfo().getDevice());
	private ActiveARPProtectionService callback;
	private InetAddress next;
	private Set<ByteArrayWrapper> mac = new HashSet<ByteArrayWrapper>();

	public ARPQueryService(InetAddress next, byte[] mac, ActiveARPProtectionService callback) {
		this.rule.setInetAddress(next);
		this.getPolicy().addRule(this.rule);
		this.callback = callback;
		this.next = next;
		this.mac.add(new ByteArrayWrapper(mac));
	}

	@Override
	public void run() {
		try {
			Server.getInstance().sendPacket(this.arpGen.arpRequest(this.next));
			Thread.sleep(5000);
			Packet p = this.getNextNonBlockingPacket();
			while(p != null && !p.equals(Packet.EOF)) {
				ARPPacket arp = (ARPPacket)p;
				if(arp.getSenderProtocolAddress().equals(this.next) &&
						!this.mac.contains(new ByteArrayWrapper(arp.sender_hardaddr))) {
					this.callback.setBadAddress(this.next);
					return;
				}

				p = this.getNextNonBlockingPacket();
			}

			this.callback.setGoodAddress(this.next);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Server.getInstance().removeService(this);
		}
	}

	private static class ByteArrayWrapper
	{
		private final byte[] data;

		public ByteArrayWrapper(byte[] data)
		{
			if (data == null)
			{
				throw new NullPointerException();
			}
			this.data = data;
		}

		@Override
		public boolean equals(Object other)
		{
			if (!(other instanceof ByteArrayWrapper))
			{
				return false;
			}
			return Arrays.equals(data, ((ByteArrayWrapper)other).data);
		}

		@Override
		public int hashCode()
		{
			return Arrays.hashCode(data);
		}
	}
}

