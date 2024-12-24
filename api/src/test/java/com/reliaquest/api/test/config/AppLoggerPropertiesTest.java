package come.reliaquest.api.test.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.reliaquest.api.ApiApplication;
import com.reliaquest.api.config.AppLoggerProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = ApiApplication.class)
@TestPropertySource(properties = "app.logger.logLevel=INFO") // Sets the log level for this test
public class AppLoggerPropertiesTest {

    @Autowired
    private AppLoggerProperties appLoggerProperties;

    @Test
    void shouldLoadDefaultLogLevel() {
        AppLoggerProperties defaultLoggerProperties = new AppLoggerProperties();
        assertThat(defaultLoggerProperties.getLogLevel()).isEqualTo("DEBUG");
    }

    @Test
    void shouldOverrideLogLevelFromConfig() {
        assertThat(appLoggerProperties.getLogLevel()).isEqualTo("INFO");
    }
}
