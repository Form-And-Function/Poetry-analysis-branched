package com.poetryanalyzer.lit;

import java.util.ArrayList;

//A list of devices within a section
public class DeviceList {
	
	private ArrayList<ArrayList<Device>> extantDevices;
	private ArrayList<Device> alliterationSound;
	private ArrayList<Device> alliterationVisual;
	private ArrayList<Device> internalRhyme;
	private ArrayList<Device> homophone;
	private ArrayList<Device> anaphora;
	private ArrayList<Device> assonance;
	private ArrayList<Device> consonance;
	private ArrayList<Device> polysyndeton;
	private ArrayList<Device> asyndeton;
	
	public DeviceList() {
		extantDevices = new ArrayList<ArrayList<Device>>();
	}
	
	public ArrayList<Device> getAlliterationSound() {
		return alliterationSound;
	}
	public void setAlliterationSound(ArrayList<Device> alliterationSound) {
		this.alliterationSound = alliterationSound;
	}
	public ArrayList<Device> getAlliterationVisual() {
		return alliterationVisual;
	}
	public void setAlliterationVisual(ArrayList<Device> alliterationVisual) {
		this.alliterationVisual = alliterationVisual;
	}
	public ArrayList<Device> getInternalRhyme() {
		return internalRhyme;
	}
	public void setInternalRhyme(ArrayList<Device> rhyme) {
		internalRhyme = rhyme;
	}
	public ArrayList<Device> getHomophone() {
		return homophone;
	}
	public void setHomophone(ArrayList<Device> homophone) {
		this.homophone = homophone;
	}
	public ArrayList<Device> getAnaphora() {
		return anaphora;
	}
	public void setAnaphora(ArrayList<Device> anaphora) {
		this.anaphora = anaphora;
	}
	public ArrayList<Device> getAssonance() {
		return assonance;
	}
	public void setAssonance(ArrayList<Device> assonance) {
		this.assonance = assonance;
	}
	public ArrayList<Device> getConsonance() {
		return consonance;
	}
	public void setConsonance(ArrayList<Device> consonance) {
		this.consonance = consonance;
	}
	public ArrayList<Device> getPolysyndeton() {
		return polysyndeton;
	}
	public void setPolysyndeton(ArrayList<Device> polysyndeton) {
		this.polysyndeton = polysyndeton;
	}
	public ArrayList<Device> getAsyndeton() {
		return asyndeton;
	}
	public void setAsyndeton(ArrayList<Device> asyndeton) {
		this.asyndeton = asyndeton;
	}
	public ArrayList<ArrayList<Device>> getExtantDevices() {
		return extantDevices;
	}
	public void setExtantDevices(ArrayList<ArrayList<Device>> extantDevices) {
		this.extantDevices = extantDevices;
	}
	
	
}
