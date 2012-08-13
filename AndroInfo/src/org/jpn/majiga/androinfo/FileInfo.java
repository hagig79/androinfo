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
	private boolean isLink;
	private String destPath;

	public static FileInfo create(String lsout) {
		String[] sp = lsout.split("\\s+");
		FileInfo fileInfo = new FileInfo();
		fileInfo.permission = sp[0].substring(1);
		fileInfo.isDir = sp[0].charAt(0) == 'd';
		fileInfo.isLink = sp[0].charAt(0) == 'l';
		fileInfo.owner = sp[1];
		fileInfo.group = sp[2];
		if (fileInfo.isDir()) {
			fileInfo.updateTime = sp[3] + " " + sp[4];
			fileInfo.name = lsout.substring(lsout.indexOf(fileInfo.updateTime)
					+ fileInfo.updateTime.length() + 1);
		} else if (fileInfo.isLink()) {
			fileInfo.updateTime = sp[3] + " " + sp[4];
			String nameAndDistPath = lsout.substring(lsout
					.indexOf(fileInfo.updateTime)
					+ fileInfo.updateTime.length() + 1);
			int delim = nameAndDistPath.indexOf(" -> ");
			fileInfo.name = nameAndDistPath.substring(0, delim);
			fileInfo.destPath = nameAndDistPath.substring(delim
					+ " -> ".length());
		} else {
			fileInfo.size = Long.parseLong(sp[3]);
			fileInfo.updateTime = sp[4] + " " + sp[5];
			fileInfo.name = lsout.substring(lsout.indexOf(fileInfo.updateTime)
					+ fileInfo.updateTime.length() + 1);
		}

		return fileInfo;
	}

	public boolean isDir() {
		return isDir;
	}

	/**
	 * このファイルがリンクであるかどうか返す.
	 * 
	 * @return リンクならばtrue、リンクでなければfalse
	 */
	public boolean isLink() {
		return isLink;
	}

	/**
	 * このリンクが参照しているファイルのパスを返す.
	 * 
	 * @return 参照ファイルの絶対パス。このFileInfoがリンクでない場合はnull。
	 */
	public String getDestPath() {
		return destPath;
	}
}
