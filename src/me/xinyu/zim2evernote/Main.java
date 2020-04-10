package me.xinyu.zim2evernote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
	static final String ZIM_PATH_STRING = "/Users/xinyung/Downloads/Notes/Mine";
	
	static final String EVERNOTE_PATH_STRING = "/Users/xinyung/Downloads/Notes/Evernote/";
	
	public static void main(String[] args) throws IOException {
		File zimPathFile = new File(ZIM_PATH_STRING);
		if (!zimPathFile.isDirectory()) {
			System.out.println("Zim path string is not directory");
			return;
		}
		File[] zimFiles = zimPathFile.listFiles();
		for (File zimFile : zimFiles){
			if(zimFile.isDirectory()) {
				// 文件夹，遍历内部文件生成evernote文件
				System.out.println("Path : " + zimFile.getAbsolutePath() + "|" + zimFile.getName());
				String evernoteFileString = EVERNOTE_PATH_STRING + zimFile.getName() + ".enex";
				File evernoteFile = new File(evernoteFileString);
				if (!evernoteFile.exists()) {
					evernoteFile.createNewFile();
				}
				FileOutputStream evernoteFileOutputStream = new FileOutputStream(evernoteFile);
				PrintWriter evernotePrintWriter = new PrintWriter(evernoteFileOutputStream);
				evernotePrintWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				evernotePrintWriter.append(System.getProperty("line.separator"));
				evernotePrintWriter.append("<!DOCTYPE en-export SYSTEM \"http://xml.evernote.com/pub/evernote-export3.dtd\">");
				evernotePrintWriter.append(System.getProperty("line.separator"));
				evernotePrintWriter.append("<en-export export-date=\"20200402T073224Z\" application=\"Evernote\" version=\"Evernote Mac 9.3.3 (459821)\">");
				evernotePrintWriter.append(System.getProperty("line.separator"));
				File[] sonZimFiles = zimFile.listFiles();
				for(File sonZimFile : sonZimFiles){
					if(sonZimFile.isFile()) {
						String filenameString = sonZimFile.getName();
						String[] tokenStrings = filenameString.split("\\.");
						if (tokenStrings.length != 2) {
							continue;
						}
						String noteNameString = tokenStrings[0];
						StringBuffer oneNoteStringBuffer = new StringBuffer();
						oneNoteStringBuffer.append("<note><title>");
						oneNoteStringBuffer.append(noteNameString);
						oneNoteStringBuffer.append("</title><content><![CDATA[<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\"><en-note>");
						
						FileInputStream sonZimFileInputStream = new FileInputStream(sonZimFile);
						InputStreamReader sonZimInputStreamReader = new InputStreamReader(sonZimFileInputStream);
						BufferedReader sonZimBufferedReader = new BufferedReader(sonZimInputStreamReader);
						
						String sonZimDataLineString = null;
						boolean startAppend = false;
						for (int i = 0; (sonZimDataLineString = sonZimBufferedReader.readLine()) != null; i++){
							if (startAppend) {
								oneNoteStringBuffer.append("<div>");
								oneNoteStringBuffer.append(sonZimDataLineString);
								oneNoteStringBuffer.append("</div>");
							}
							if (sonZimDataLineString.contains("======")) {
								startAppend = true;
							}
			            }
			 
						oneNoteStringBuffer.append("</en-note>]]></content><created>20200402T060138Z</created><updated>20200402T060149Z</updated><note-attributes><source>desktop.mac</source><reminder-order>0</reminder-order></note-attributes></note>");
						oneNoteStringBuffer.append(System.getProperty("line.separator"));
						sonZimBufferedReader.close();
						sonZimInputStreamReader.close();
						sonZimFileInputStream.close();
						evernotePrintWriter.append(oneNoteStringBuffer.toString());
					}
				}
				evernotePrintWriter.append("</en-export>");
				evernotePrintWriter.append(System.getProperty("line.separator"));
				evernotePrintWriter.flush();
				evernotePrintWriter.close();
				evernoteFileOutputStream.close();
			} else if(zimFile.isFile()) {
				// 文件，直接生成evernote文件
				System.out.println("File : " + zimFile.getAbsolutePath()+ "|" + zimFile.getName());
				String filenameString = zimFile.getName();
				String[] tokenStrings = filenameString.split("\\.");
				if (tokenStrings.length != 2) {
					continue;
				}
				String noteNameString = tokenStrings[0];
				
				String evernoteFileString = EVERNOTE_PATH_STRING + noteNameString + ".enex";
				File evernoteFile = new File(evernoteFileString);
				if (!evernoteFile.exists()) {
					evernoteFile.createNewFile();
				}
				FileOutputStream evernoteFileOutputStream = new FileOutputStream(evernoteFile);
				PrintWriter evernotePrintWriter = new PrintWriter(evernoteFileOutputStream);
				evernotePrintWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				evernotePrintWriter.append(System.getProperty("line.separator"));
				evernotePrintWriter.append("<!DOCTYPE en-export SYSTEM \"http://xml.evernote.com/pub/evernote-export3.dtd\">");
				evernotePrintWriter.append(System.getProperty("line.separator"));
				evernotePrintWriter.append("<en-export export-date=\"20200402T073224Z\" application=\"Evernote\" version=\"Evernote Mac 9.3.3 (459821)\">");
				evernotePrintWriter.append(System.getProperty("line.separator"));
				
				StringBuffer oneNoteStringBuffer = new StringBuffer();
				oneNoteStringBuffer.append("<note><title>");
				oneNoteStringBuffer.append(noteNameString);
				oneNoteStringBuffer.append("</title><content><![CDATA[<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\"><en-note>");
				
				FileInputStream zimFileInputStream = new FileInputStream(zimFile);
				InputStreamReader zimInputStreamReader = new InputStreamReader(zimFileInputStream);
				BufferedReader zimBufferedReader = new BufferedReader(zimInputStreamReader);
				
				String zimDataLineString = null;
				boolean startAppend = false;
				for (int i = 0; (zimDataLineString = zimBufferedReader.readLine()) != null; i++){
					if (startAppend) {
						oneNoteStringBuffer.append("<div>");
						oneNoteStringBuffer.append(zimDataLineString);
						oneNoteStringBuffer.append("</div>");
					}
					if (zimDataLineString.contains("======")) {
						startAppend = true;
					}
	            }
	 
				oneNoteStringBuffer.append("</en-note>]]></content><created>20200402T060138Z</created><updated>20200402T060149Z</updated><note-attributes><source>desktop.mac</source><reminder-order>0</reminder-order></note-attributes></note>");
				oneNoteStringBuffer.append(System.getProperty("line.separator"));
				zimBufferedReader.close();
				zimInputStreamReader.close();
				zimFileInputStream.close();
				evernotePrintWriter.append(oneNoteStringBuffer.toString());

				evernotePrintWriter.append("</en-export>");
				evernotePrintWriter.append(System.getProperty("line.separator"));
				evernotePrintWriter.flush();
				evernotePrintWriter.close();
				evernoteFileOutputStream.close();
			}
		}
	}
}
