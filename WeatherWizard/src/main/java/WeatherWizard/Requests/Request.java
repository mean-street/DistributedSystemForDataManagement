package WeatherWizard.Requests;

import WeatherWizard.Configurations.KafkaConfig;
import akka.http.javadsl.model.HttpRequest;

public abstract class Request {
    public abstract HttpRequest create();

    public abstract Location getLocation();

    public abstract KafkaConfig.Topic getTopic();
}