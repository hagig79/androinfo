package org.jpn.majiga.androinfo;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

public class InfoGatheringTask extends AsyncTask<String, Integer, Void> {
	private ProgressDialog progressDialog;
	private Context context;

	public InfoGatheringTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setProgress(0);
		progressDialog.show();
	}

	@Override
	protected Void doInBackground(String... arg0) {
		analyzeFileSystem();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
	}

	private static void analyzeFileSystem() {
		List<FileInfo> files = FileInfo.getFileInfoRecursive("/");

		Collections.sort(files, new Comparator<FileInfo>() {
			public int compare(FileInfo lhs, FileInfo rhs) {
				return lhs.getAbsolutePath().compareTo(rhs.getAbsolutePath());
			}
		});

		StringBuffer sb = new StringBuffer();
		for (FileInfo fileInfo : files) {
			sb.append(fileInfo.getAbsolutePath());
			sb.append(",");
			sb.append(fileInfo.permission);
			sb.append(",");
			sb.append(fileInfo.owner);
			sb.append(",");
			sb.append(fileInfo.group);
			sb.append(",");
			if (fileInfo.isFile()) {
				sb.append(fileInfo.size);
			}
			sb.append("\n");
		}
		FileOutputStream fos;
		String fileName = Build.DEVICE + "-" + Build.DISPLAY
				+ System.currentTimeMillis() + ".txt";
		try {
			fos = new FileOutputStream("/sdcard/" + fileName, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);

			bw.write(sb.toString());
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
