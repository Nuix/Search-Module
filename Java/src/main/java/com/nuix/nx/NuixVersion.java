package com.nuix.nx;

import java.lang.Package;
import java.util.regex.Pattern;

public class NuixVersion implements Comparable<NuixVersion> {
	public static NuixVersion currentVersion;
	public static void setCurrentVersion(String version){
		currentVersion = NuixVersion.parse(version);
	}
	
	public static Pattern previewVersionInfoRemovalPattern = Pattern.compile("[^0-9\\.].*$");
	
	private int major = 0;
	private int minor = 0;
	private int build = 0;
	
	public NuixVersion(){
		this(0,0,0);
	}
	
	public NuixVersion(int majorVersion){
		this(majorVersion,0,0);
	}
	
	public NuixVersion(int majorVersion, int minorVersion){
		this(majorVersion,minorVersion,0);
	}
	
	public NuixVersion(int majorVersion, int minorVersion, int buildVersion){
		major = majorVersion;
		minor = minorVersion;
		build = buildVersion;
	}
	
	public static NuixVersion parse(String versionString){
		try {
			String[] versionParts = NuixVersion.previewVersionInfoRemovalPattern.matcher(versionString.trim()).replaceAll("").split("\\.");
			int[] versionPartInts = new int[versionParts.length];
			for(int i=0;i<versionParts.length;i++){
				versionPartInts[i] = Integer.parseInt(versionParts[i]);
			}
			switch(versionParts.length){
				case 1:
					return new NuixVersion(versionPartInts[0]);
				case 2:
					return new NuixVersion(versionPartInts[0],versionPartInts[1]);
				case 3:
					return new NuixVersion(versionPartInts[0],versionPartInts[1],versionPartInts[2]);
				default:
					return new NuixVersion();
			}
		}catch(Exception exc){
			System.out.println("Error while parsing version: "+versionString);
			System.out.println("Pretending version is 100.0.0");
			return new NuixVersion(100,0,0);
		}
	}
	
	public int getMajor() {
		return major;
	}
	public void setMajor(int major) {
		this.major = major;
	}
	
	public int getMinor() {
		return minor;
	}
	public void setMinor(int minor) {
		this.minor = minor;
	}
	
	public int getBuild() {
		return build;
	}
	public void setBuild(int build) {
		this.build = build;
	}
	
	public static NuixVersion getCurrent(){
		if(currentVersion != null){
			return NuixVersion.currentVersion;
		} else {
			String versionString = "0.0.0";
			for(Package p : Package.getPackages()){
				if(p.getName().matches("com\\.nuix\\..*")){
					versionString = p.getImplementationVersion();
					break;
				}
			}
			return NuixVersion.parse(versionString);
		}
	}

	public boolean isLessThan(NuixVersion other){
		return this.compareTo(other) < 0;
	}
	
	public boolean isAtLeast(NuixVersion other){
		return this.compareTo(other) >= 0;
	}
	
	public boolean isLessThan(String other){
		return isLessThan(parse(other));
	}
	
	public boolean isAtLeast(String other){
		return isAtLeast(parse(other));
	}
	
	@Override
	public int compareTo(NuixVersion other) {
		if(this.major == other.major){
			if(this.minor == other.minor){
				return Integer.compare(this.build, other.build);
			}
			else{
				return Integer.compare(this.minor, other.minor);
			}
		}
		else{
			return Integer.compare(this.major, other.major);
		}
		
	}
	
	@Override
	public String toString(){
		return Integer.toString(this.major) + "." +
				Integer.toString(this.minor) + "." +
				Integer.toString(this.build);
	}
}
