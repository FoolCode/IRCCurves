package curves.trigger.fileserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import curves.main.Bot;
import curves.trigger.IPeriodicHandler;

public class P_Crawl implements IPeriodicHandler {

	int skip = 0;
	Logger log = Logger.getLogger(P_Crawl.class);

	ArrayList<String> filelist;
	String[] extensions;
	String root;
	long latestDate = 0;
	String latestFile;

	public boolean isReady(Bot bot, Hashtable<String, Object> storage) {
		skip = (skip + 1) % 60;
		return skip == 1;
	}

	public void process(Bot bot, Hashtable<String, Object> storage) {
		root = (String) storage.get("file root");
		extensions = ((String) storage.get("file extensions")).split("|");
		filelist = new ArrayList<String>();
		traverse(new File(root));
		storage.put("file list", filelist);
		storage.put("latest file", latestFile);
		log.info(filelist.size() + " file(s) found in " + root);
		log.info("The newest file is " + latestFile);
	}

	boolean checkExtension(String filename) {
		for (String extension : extensions) {
			if (filename.endsWith(extension))
				return true;
		}
		return false;
	}

	public void traverse(File directory) {
		File[] files = directory.listFiles();
		String filename = "";
		for (File file : files) {
			if (file.isDirectory()) {
				traverse(file);
			} else {
				try {
					filename = file.getCanonicalPath();
					if (!checkExtension(filename))
						continue;
					filename = filename.replace(root, "");
					filelist.add(filename);
					if (file.lastModified() > latestDate){
						latestDate = file.lastModified();
						latestFile = filename;
					}
				} catch (IOException e) {
					log.error("Traversing file tree failed.", e);
				}
			}
		}
	}
}
