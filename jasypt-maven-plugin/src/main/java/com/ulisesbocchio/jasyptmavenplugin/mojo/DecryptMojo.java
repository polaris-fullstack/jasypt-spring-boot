package com.ulisesbocchio.jasyptmavenplugin.mojo;

import java.nio.file.Path;

import com.ulisesbocchio.jasyptmavenplugin.encrypt.EncryptionService;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Goal which decrypts demarcated values in properties files.
 */
@Mojo(name = "decrypt", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class DecryptMojo extends AbstractJasyptMojo {
  private static final Logger LOGGER = LoggerFactory.getLogger(DecryptMojo.class);

  @Override
  protected void run(final EncryptionService service, final Path path) throws
      MojoExecutionException {
    LOGGER.info("Decrypting file " + path);

    String contents = FileService.read(path);
    String decryptedContents = service.decrypt(contents);

    LOGGER.info("\n" + decryptedContents);
  }
}
