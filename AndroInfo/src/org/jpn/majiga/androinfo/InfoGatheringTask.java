package org.jpn.majiga.androinfo;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
		List<String> out = new ArrayList<String>();
		List<String> pacageTexts = getPackageDatas(context);
		out.addAll(pacageTexts);
		List<String> fileInfoTexts = analyzeFileSystem();
		out.addAll(fileInfoTexts);
		writeTextFile(out);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
	}

	private static List<String> analyzeFileSystem() {
		List<FileInfo> files = FileInfo.getFileInfoRecursive("/");

		Collections.sort(files, new Comparator<FileInfo>() {
			public int compare(FileInfo lhs, FileInfo rhs) {
				return lhs.getAbsolutePath().compareTo(rhs.getAbsolutePath());
			}
		});
		List<String> lines = new ArrayList<String>();
		for (FileInfo fileInfo : files) {
			StringBuffer line = new StringBuffer();
			line.append(fileInfo.getAbsolutePath());
			line.append(",");
			line.append(fileInfo.permission);
			line.append(",");
			line.append(fileInfo.owner);
			line.append(",");
			line.append(fileInfo.group);
			line.append(",");
			if (fileInfo.isFile()) {
				line.append(fileInfo.size);
			}
			lines.add(line.toString());
		}
		return lines;
	}

	private static void writeTextFile(List<String> lines) {
		FileOutputStream fos;
		String fileName = Build.DEVICE + "-" + Build.DISPLAY + "-"
				+ System.currentTimeMillis() + ".txt";
		try {
			fos = new FileOutputStream("/sdcard/" + fileName, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			for (String line : lines) {
				bw.write(line + "\n");
			}

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

	private static List<String> getPackageDatas(Context context) {

		List<String> packageDatas = new ArrayList<String>();

		PackageManager packageManager = context.getPackageManager();
		List<ApplicationInfo> list = packageManager
				.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo appInfo : list) {
			StringBuffer data = new StringBuffer();
			try {
				PackageInfo packageInfo = packageManager.getPackageInfo(
						appInfo.packageName, PackageManager.GET_META_DATA);
				data.append(appInfo.packageName);// パッケージ名
				data.append(",");
				data.append(packageInfo.versionName); // バージョン
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			packageDatas.add(data.toString());
		}
		Collections.sort(packageDatas, new Comparator<String>() {

			public int compare(String lhs, String rhs) {
				return lhs.compareTo(rhs);
			}
		});
		return packageDatas;
	}
}
