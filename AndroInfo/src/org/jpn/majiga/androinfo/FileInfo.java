package org.jpn.majiga.androinfo;

public class FileInfo {
	static final int TYPE_FILE = 0;
	static final int TYPE_DIRECTORY = 1;
	static final int TYPE_LINK = 2;
	static final int TYPE_SOCKET = 3;
	static final int TYPE_BLOCK = 4;
	static final int TYPE_CHARACTER = 5;
	static final int TYPE_PIPE = 6;

	String permission;
	String owner;
	String group;
	long size;
	String updateTime;
	String name;
	String dir;
	private int type;
	private String destPath;
	private int major;
	private int minor;

	public static FileInfo create(String lsout) {
		String[] sp = lsout.split("\\s+");
		FileInfo fileInfo = new FileInfo();
		fileInfo.permission = sp[0].substring(1);
		switch (sp[0].charAt(0)) {
		case '-':
			fileInfo.type = TYPE_FILE;
			break;
		case 'd':
			fileInfo.type = TYPE_DIRECTORY;
			break;
		case 'l':
			fileInfo.type = TYPE_LINK;
			break;
		case 'b':
			fileInfo.type = TYPE_BLOCK;
			break;
		case 'c':
			fileInfo.type = TYPE_CHARACTER;
			break;
		default:
		}
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
		} else if (fileInfo.isCharacterDevice() || fileInfo.isBlockDevice()) {
			fileInfo.updateTime = sp[5] + " " + sp[6];
			fileInfo.major = Integer.parseInt(sp[3].substring(0,
					sp[3].length() - 1));
			fileInfo.minor = Integer.parseInt(sp[4]);
			fileInfo.name = lsout.substring(lsout.indexOf(fileInfo.updateTime)
					+ fileInfo.updateTime.length() + 1);
		} else {
			fileInfo.size = Long.parseLong(sp[3]);
			fileInfo.updateTime = sp[4] + " " + sp[5];
			fileInfo.name = lsout.substring(lsout.indexOf(fileInfo.updateTime)
					+ fileInfo.updateTime.length() + 1);
		}

		return fileInfo;
	}

	public boolean isDir() {
		return type == TYPE_DIRECTORY;
	}

	/**
	 * このファイルがリンクであるかどうか返す.
	 * 
	 * @return リンクならばtrue、リンクでなければfalse
	 */
	public boolean isLink() {
		return type == TYPE_LINK;
	}

	/**
	 * このリンクが参照しているファイルのパスを返す.
	 * 
	 * @return 参照ファイルの絶対パス。このFileInfoがリンクでない場合はnull。
	 */
	public String getDestPath() {
		return destPath;
	}

	/**
	 * このファイルがキャラクタデバイスであるかどうか返す.
	 * 
	 * @return キャラクタデバイスならばtrue、キャラクタデバイスでなければfalse
	 */
	public boolean isCharacterDevice() {
		return type == TYPE_CHARACTER;
	}

	/**
	 * このファイルがブロックデバイスであるかどうか返す.
	 * 
	 * @return ブロックデバイスならばtrue、ブロックデバイスでなければfalse
	 */
	public boolean isBlockDevice() {
		return type == TYPE_BLOCK;
	}

	/**
	 * このデバイスファイルのメジャー番号を返す.
	 * 
	 * @return メジャー番号。このFileInfoがキャラクタデバイスでもブロックデバイスでもない場合は0。
	 */
	public int getMajorNumber() {
		return major;
	}

	/**
	 * このデバイスファイルのマイナー番号を返す.
	 * 
	 * @return マイナー番号。このFileInfoがキャラクタデバイスでもブロックデバイスでもない場合は0。
	 */
	public int getMinorNumber() {
		return minor;
	}
}
