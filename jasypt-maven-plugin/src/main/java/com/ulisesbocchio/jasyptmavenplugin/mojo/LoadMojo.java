package com.ulisesbocchio.jasyptmavenplugin.mojo;

import java.nio.file.Path;
import java.util.Properties;

import com.ulisesbocchio.jasyptmavenplugin.encrypt.EncryptionService;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The 'load' goal loads and decrypts property files for the supplied environment(s). The properties
 * can then be used in pom.xml configurations as '${something.prop}' for other plugins, e.g. flyway
 * or the mybatis generator.
 *
 * @author Rupert Madden-Abbott
 */
@Mojo(name = "load", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class LoadMojo extends AbstractJasyptMojo {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoadMojo.class);

  /**
   * Prefix that will be added before name of each property. Can be useful for distinguishing the
   * source of the properties from other maven properties.
   */
  @Parameter(property = "jasypt.plugin.keyPrefix")
  private String keyPrefix = null;

  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  @Override
  protected void run(final EncryptionService service, final Path path) throws
      MojoExecutionException {
    Properties properties = service.getEncryptableProperties();
    FileService.load(path, properties);

    if (properties.isEmpty()) {
      LOGGER.info("  No properties found");
    } else {
      for (String key : properties.stringPropertyNames()) {
        LOGGER.info("  Loaded '" + key + "' property");
      }
    }

    Properties projectProperties = project.getProperties();
    for (String key : properties.stringPropertyNames()) {
      if (keyPrefix != null) {
        projectProperties.put(keyPrefix + key, properties.get(key));
      } else {
        projectProperties.put(key, properties.get(key));
      }
    }
  }
}
