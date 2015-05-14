package fr.minepod.utils;

import java.io.File;
import java.io.IOException;

import de.schlichtherle.truezip.file.TArchiveDetector;
import de.schlichtherle.truezip.file.TConfig;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.fs.archive.zip.JarDriver;
import de.schlichtherle.truezip.fs.archive.zip.ZipDriver;
import de.schlichtherle.truezip.socket.sl.IOPoolLocator;

public class UtilsArchive {
  public UtilsArchive() {
    TConfig.get().setArchiveDetector(
        new TArchiveDetector(TArchiveDetector.NULL, new Object[][] {
            {"jar", new JarDriver(IOPoolLocator.SINGLETON)},
            {"zip", new ZipDriver(IOPoolLocator.SINGLETON)}}));
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
}
