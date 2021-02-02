package dangqu.powertrade.automation.common.repository;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import org.springframework.stereotype.Component;


@Component
public class UuidIdGenerator {

    // different ProcessEngines on the same classloader share one generator.
    protected static volatile TimeBasedGenerator timeBasedGenerator;

    public UuidIdGenerator() {
        ensureGeneratorInitialized();
    }

    protected void ensureGeneratorInitialized() {
        if (timeBasedGenerator == null) {
            synchronized (UuidIdGenerator.class) {
                if (timeBasedGenerator == null) {
                    timeBasedGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
                }
            }
        }
    }

    public String generateId() {
        return timeBasedGenerator.generate().toString();
    }

}

