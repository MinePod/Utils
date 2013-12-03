package fr.minepod.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Hex;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Files {	
	public static String ReadFile(String path) throws IOException {
		return ReadFile(new File(path));
	}
	
	public static String ReadFile(File file) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(file));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        
	        boolean first = true;
	        while (line != null) {
	        	if(first)
	        		first = false;
	        	else
	        		sb.append("\n");
	            sb.append(line);
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
	public static void WriteFile(String path, String stringToWrite) throws IOException {
		WriteFile(new File(path), stringToWrite);
	}
	
	public static void WriteFile(File file, String stringToWrite) throws IOException {
		if(!file.exists()) {
			file.createNewFile();
		}
 
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stringToWrite);
		bw.close();
	}
	
	public static void Delete(String path) {
		Delete(new File(path));
	}
	
	public static void Delete(File file) {		
		if(file.exists()) {
			if(file.isDirectory() && file.list().length != 0) {
				String files[] = file.list();
				for (String temp : files) {
	        		File fileDelete = new File(file, temp);
	        		Delete(fileDelete);
	        	}
			}
			file.delete();
		}
	}
	
	 public static void UnZip(String input, String dirOutput, String fileOutputName) throws ZipException {
		 Files.Delete(new File(dirOutput + fileOutputName));
		 ZipFile zipFile = new ZipFile(input);
	     zipFile.extractAll(dirOutput);
	 }
	 
	 public static void Zip(String input, String output) throws ZipException {
		 Zip(new File(input), new File(output));
	 }
	 
	 public static void Zip(File input, File output) throws ZipException {
		 File[] inputFiles = new File[]{input};
		 Zip(inputFiles, output);
	 }
	 
	 public static void Zip(File[] input, File output) throws ZipException {
		 output.delete();
		 ZipFile zipFile = new ZipFile(output);
		 ZipParameters parameters = new ZipParameters();
		 parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		 parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			
		 for(int i = 0; i < input.length; i++) {
			 if(input[i].isDirectory()) {
				 zipFile.addFolder(input[i].getAbsoluteFile(), parameters);
			 } else {
				 zipFile.addFile(input[i].getAbsoluteFile(), parameters);
			 }
		 }
	 }
	 
	 public static String md5(String path) {
		 return md5(new File(path));
	 }
	 
	 public static String md5(File file) {
	     if ((file.exists()) && (file.length() > 0L)) {
	    	 try {
	    		MessageDigest md = MessageDigest.getInstance("MD5");
	    		FileInputStream fis = new FileInputStream(file);
	    		byte[] dataBytes = new byte[1024];
	    		int nread = 0;

	    		while ((nread = fis.read(dataBytes)) != -1) {
	    			md.update(dataBytes, 0, nread);
	    		}

    		    byte[] mdbytes = md.digest();
    		    fis.close();
	        
    		    return new String(Hex.encode(mdbytes));
	    	} catch (NoSuchAlgorithmException e) {
	    		e.printStackTrace();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	     }
	     return null;
	  }
	 
	 public static String sha256(String path) {
		 return sha256(new File(path));
	 }
	 
	 public static String sha256(File file) {
	     if ((file.exists()) && (file.length() > 0L)) {
	    	 try {
	    		MessageDigest md = MessageDigest.getInstance("SHA-256");
	    		FileInputStream fis = new FileInputStream(file);
	    		byte[] dataBytes = new byte[1024];
	    		int nread = 0;

	    		while ((nread = fis.read(dataBytes)) != -1) {
	    			md.update(dataBytes, 0, nread);
	    		}

    		    byte[] mdbytes = md.digest();
    		    fis.close();
	        
    		    return new String(Hex.encode(mdbytes));
	    	} catch (NoSuchAlgorithmException e) {
	    		e.printStackTrace();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	     }
	     return null;
	  }
}
