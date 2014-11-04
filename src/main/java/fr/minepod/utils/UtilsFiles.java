package fr.minepod.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Hex;
import org.mozilla.universalchardet.UniversalDetector;

import de.schlichtherle.truezip.file.TArchiveDetector;
import de.schlichtherle.truezip.file.TFile;

public class UtilsFiles {
  public String readFile(String path) throws IOException {
    return readFile(new File(path));
  }

  public String readFile(File file) throws IOException {
    return readFileWithEncoding(new InputStreamReader(new FileInputStream(file)));
  }

  public String readFileUtf8(File file) throws IOException {
    return readFileWithEncoding(new InputStreamReader(new FileInputStream(file), "UTF-8"));
  }

  public String readFileWithEncoding(Reader reader) throws IOException {
    BufferedReader br = new BufferedReader(reader);
    try {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();

      boolean first = true;
      while (line != null) {
        if (first) {
          first = false;
        } else {
          sb.append("\n");
        }
        sb.append(line);
        line = br.readLine();
      }
      return sb.toString();
    } finally {
      br.close();
    }
  }

  public void writeFile(String path, String stringToWrite) throws IOException {
    writeFile(new File(path), stringToWrite);
  }

  public void writeFile(File file, String stringToWrite) throws IOException {
    if (!file.exists()) {
      file.createNewFile();
    }

    writeFileWithEncoding(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"),
        stringToWrite);
  }

  public void writeFileWithEncoding(Writer writer, String stringToWrite) throws IOException {
    BufferedWriter bw = new BufferedWriter(writer);
    bw.write(stringToWrite);
    bw.close();
  }

  public String getFileEncoding(File file) throws IOException {
    byte[] buf = new byte[4096];
    FileInputStream fileInputStream = new FileInputStream(file);
    UniversalDetector detector = new UniversalDetector(null);

    int nread;
    while ((nread = fileInputStream.read(buf)) > 0 && !detector.isDone()) {
      detector.handleData(buf, 0, nread);
    }

    detector.dataEnd();

    String encoding = detector.getDetectedCharset();
    detector.reset();
    fileInputStream.close();

    return encoding.toUpperCase();
  }

  public void copyFile(String input, String output) throws IOException {
    copyFile(new File(input), new File(output));
  }

  public void copyFile(File input, File output) throws IOException {
    delete(output);

    InputStream inputStream = new FileInputStream(input);
    OutputStream outputStream = new FileOutputStream(output);

    byte[] buffer = new byte[1024];
    int length;
    while ((length = inputStream.read(buffer)) > 0) {
      outputStream.write(buffer, 0, length);
    }

    inputStream.close();
    outputStream.close();
  }

  public void moveFile(String input, String output) {
    moveFile(new File(input), new File(output));
  }

  public void moveFile(File input, File output) {
    delete(output);

    input.renameTo(output);
  }

  public void delete(String path) {
    delete(new File(path));
  }

  public void delete(File file) {
    if (file.exists()) {
      if (file.isDirectory() && file.list().length != 0) {
        String files[] = file.list();

        for (String temp : files) {
          File fileDelete = new File(file, temp);
          delete(fileDelete);
        }
      }

      file.delete();
    }
  }

  public void unZip(String input, String output) throws IOException {
    unZip(new File(input), new File(output));
  }

  public void unZip(File input, File output) throws IOException {
    delete(output);

    TFile.cp_rp(input, output, TArchiveDetector.NULL);
  }

  public void mergeZip(String input, String merge, String output) throws IOException {
    mergeZip(new File(input), new File(merge), new File(output));
  }

  public void mergeZip(File input, File merge, File output) throws IOException {
    delete(output);

    TFile out = new TFile(output);
    out.mkdir(false);

    TFile[] entries = new TFile(input).listFiles();
    for (TFile temp : entries) {
      temp.cp_rp(new TFile(out, temp.getName()));
    }

    TFile[] merges = new TFile(merge).listFiles();
    for (TFile temp : merges) {
      temp.cp_rp(new TFile(out, temp.getName()));
    }
  }

  public String md5(String path) throws NoSuchAlgorithmException, IOException {
    return md5(new File(path));
  }

  public String md5(File file) throws NoSuchAlgorithmException, IOException {
    return encodeFile(file, MessageDigest.getInstance("MD5"));
  }

  public String sha256(String path) throws NoSuchAlgorithmException, IOException {
    return sha256(new File(path));
  }

  public String sha256(File file) throws NoSuchAlgorithmException, IOException {
    return encodeFile(file, MessageDigest.getInstance("SHA-256"));
  }

  public String encodeFile(File file, MessageDigest md) throws IOException {
    if ((file.exists()) && (file.length() > 0L)) {
      FileInputStream fis = new FileInputStream(file);
      byte[] dataBytes = new byte[1024];
      int nread = 0;

      while ((nread = fis.read(dataBytes)) != -1) {
        md.update(dataBytes, 0, nread);
      }

      byte[] mdbytes = md.digest();
      fis.close();

      return new String(Hex.encode(mdbytes));
    }

    return null;
  }
}
