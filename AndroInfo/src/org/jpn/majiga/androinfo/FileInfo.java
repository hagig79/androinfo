package org.jpn.majiga.androinfo;

public class FileInfo {
	String permission;
	String owner;
	String group;
	long size;
	String updateTime;
	String name;
	String dir;
	boolean isDir;

	public static FileInfo create(String lsout) {
		String[] sp = lsout.split("\\s+");
		FileInfo fileInfo = new FileInfo();
		fileInfo.permission = sp[0].substring(1);
		fileInfo.isDir = sp[0].charAt(0) == 'd';
		fileInfo.owner = sp[1];
		fileInfo.group = sp[2];
		if (!fileInfo.isDir()) {
			fileInfo.size = Long.parseLong(sp[3]);
			fileInfo.updateTime = sp[4] + " " + sp[5];
		} else {
			fileInfo.updateTime = sp[3] + " " + sp[4];
		}
		fileInfo.name = lsout.substring(lsout.indexOf(fileInfo.updateTime)
				+ fileInfo.updateTime.length() + 1);

		return fileInfo;
	}

	public boolean isDir() {
		return isDir;
	}
}
