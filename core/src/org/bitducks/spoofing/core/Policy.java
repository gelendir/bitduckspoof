package org.bitducks.spoofing.core;

import java.util.LinkedList;
import java.util.List;

import jpcap.packet.Packet;

/**
 * This class is used to know if a set of rules is
 * valid or not. Each service has his own policy and 
 * can add or remove rules.
 * @author Frédérik Paradis
 */
public class Policy {
	/**
	 * The collection of rules
	 */
	private List<Rule> rules = new LinkedList<Rule>();
	
	/**
	 * Indicates if all rules have to be correct
	 * or just one rule to validate the policy. 
	 * If true, all rules have to be correct. If 
	 * false, just one rule have to be correct.
	 */
	private boolean strict;
	
	/**
	 * This constructor initialize the policy
	 * with strict at false.
	 * @see #getStrict()
	 */
	public Policy() {
		this(false);
	}

	/**
	 * 
	 * @param strict
	 * @see #getStrict()
	 */
	public Policy(boolean strict) {
		this.strict = strict;
	}

	/**
	 * This method add a rule to this policy.
	 * @param r The rule to add.
	 */
	public void addRule(Rule r) {
		this.rules.add(r);
	}

	/**
	 * This method remove a rule to this policy.
	 * @param r The rule to remove.
	 * @return Return true if removal succeed; false otherwise.
	 */
	public boolean removeRule(Rule r) {
		return this.rules.remove(r);
	}

	/**
	 * This method indicates if all rules have to 
	 * be correct or just one rule to validate the 
	 * policy. If true, all rules have to be correct. 
	 * If false, just one rule have to be correct.
	 * @return The strict value.
	 */
	public boolean getStrict() {
		return this.strict;
	}
	
	/**
	 * This method is used to modify the strict value.
	 * @param strict The new value
	 * @see #getStrict()
	 */
	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	/**
	 * This method check if the packet is valid with the
	 * rules of the policy and it strict value.
	 * @param p The packet to verify
	 * @return Return true if the policy is valid; false otherwise.
	 * @see #getStrict()
	 */
	public boolean checkIfPolicyValid(Packet p) {

		int count = 0;
		for (Rule rule : this.rules) {

			boolean valid = rule.checkRule(p);

			if (valid && !this.strict) {
				return true;
			} else if (!valid && this.strict) {
				return false;
			} else if (valid) {
				++count;
			}

		}

		if (count == this.rules.size()) { // All of them are valid
			return true;
		} else {
			return false;
		}

		/*if(!this.strict) {
			for (Rule rule : this.rules) {

				boolean valid = rule.checkRule(p);
				if(valid) {
					return true;
				}
			}

			return false;
		} else {

			for (Rule rule : this.rules) {

				boolean valid = rule.checkRule(p);
				if(!valid) {
					return false;
				}
			}
			return true;
		}*/
	}
}
