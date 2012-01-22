package org.bitducks.spoofing.util.dhcp;

import java.nio.ByteBuffer;

/**
 * Cette classe est une classe abstraite servant à 
 * implémenter une option DHCP.
 * @author Frédérik Paradis
 */
public abstract class DHCPOption {
	
	/**
	 * Le code de l'option DHCP
	 */
	private byte code = 0;
	
	/**
	 * Le ByteBuffer qui permettra d'avoir les données sous
	 * forme d'un tableau de byte.
	 */
	protected ByteBuffer b;
	
	/**
	 * Cette méthode retourne le code actuel de l'option
	 * DHCP.
	 * @return Retourne le code actuel de l'option
	 * DHCP.
	 */
	public byte getCode() {
		return code;
	}
	
	/**
	 * Cette méthode modifie le code de l'option DHCP.
	 * @param code Le code de l'option
	 */
	public void setCode(byte code) {
		this.code = code;
	}
	
	/**
	 * Cette méthode retourne la taille des données de
	 * l'option DHCP.
	 * @return Retourne la taille des données de
	 * l'option DHCP.
	 */
	public byte getDataLength() {
		if (this.b != null) {
			return (byte) this.b.capacity();
		} else {
			return (byte) 0;
		}
	}
	
	/**
	 * Cette méthode retourne un tableau de byte qui correspond
	 * aux données de l'option DHCP.
	 * @return Retourne un tableau de byte qui correspond
	 * aux données de l'option DHCP.
	 */
	public byte[] getDataBytes() {
		if (this.b != null) {
			return this.b.array();
		} else {
			return new byte[0];
		}
	}
}
