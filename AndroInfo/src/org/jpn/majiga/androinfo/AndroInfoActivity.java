package org.jpn.majiga.androinfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

public class AndroInfoActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	private static List<FileInfo> getFileInfo(String path) {
		ProcessBuilder pb = new ProcessBuilder("ls", "-l", path);
		InputStream in = null;
		try {
			Process p = pb.start();
			in = p.getInputStream();
			byte[] buffer = new byte[10240];
			int length = in.read(buffer);
			String str = new String(buffer, 0, length);
			String[] files = str.split("\n");
			List<FileInfo> fileInfos = new ArrayList<FileInfo>();
			for (String lsline : files) {
				fileInfos.add(FileInfo.create(lsline));
			}
			return fileInfos;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}