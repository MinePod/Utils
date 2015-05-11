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
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.mozilla.universalchardet.UniversalDetector;

import de.schlichtherle.truezip.file.TArchiveDetector;
import de.schlichtherle.truezip.file.TConfig;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.fs.archive.zip.JarDriver;
import de.schlichtherle.truezip.fs.archive.zip.ZipDriver;
import de.schlichtherle.truezip.socket.sl.IOPoolLocator;

public class UtilsFiles {
  public UtilsFiles() {
    TConfig.get().setArchiveDetector(
        new TArchiveDetector(TArchiveDetector.NULL, new Object[][] {
            {"jar", new JarDriver(IOPoolLocator.SINGLETON)},
            {"zip", new ZipDriver(IOPoolLocator.SINGLETON)}}));
  }

  public String readFile(String path) throws IOException {
    return readFile(new File(path));
  }

  public String readFile(File file) throws IOException {
    if (!file.exists() || file.length() == 0) {
      return null;
    }

    return readFileUtf8(file);
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
    file.getParentFile().mkdirs();

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
    output.getParentFile().mkdirs();
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
    input.getParentFile().mkdirs();
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
    input.getParentFile().mkdirs();

    TFile out = new TFile(output);
    if (out.exists()) {
      out.rm_r();
    }

    new TFile(input).cp_rp(out);
  }

  public void mergeZip(String input, String merge, String output) throws IOException {
    mergeZip(new File(input), new File(merge), new File(output));
  }

  public void mergeZip(File input, File merge, File output) throws IOException {
    input.getParentFile().mkdirs();

    TFile out = new TFile(output);
    if (out.exists()) {
      out.rm_r();
    }

    new TFile(input).cp_rp(out);

    TFile[] merges = new TFile(merge).listFiles();
    for (TFile temp : merges) {
      TFile target = new TFile(out, temp.getName());
      if (target.exists()) {
        target.rm_r();
      }

      temp.cp_rp(target);
    }
  }

  public String md5(String path) throws NoSuchAlgorithmException, IOException {
    return md5(new File(path));
  }

  public String md5(File file) throws NoSuchAlgorithmException, IOException {
    if ((file.exists()) && (file.length() > 0L)) {
      return DigestUtils.md5Hex(new FileInputStream(file));
    }

    return null;
  }

  public String sha256(String path) throws NoSuchAlgorithmException, IOException {
    return sha256(new File(path));
  }

  public String sha256(File file) throws NoSuchAlgorithmException, IOException {
    if ((file.exists()) && (file.length() > 0L)) {
      return DigestUtils.sha256Hex(new FileInputStream(file));
    }

    return null;
  }
}
