package logic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by nik on 11.11.14.
 */
@Component
public class Version {
    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    @Value("${artifactId}")
    String artifactId;

    @Value("${version}")
    String version;
}
