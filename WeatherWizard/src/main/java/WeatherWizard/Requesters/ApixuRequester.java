package WeatherWizard.Requesters;

import WeatherWizard.Requests.ApixuCurrentWeatherRequest;
import WeatherWizard.Responses.ApixuCurrentWeatherResponse;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpEntity;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import akka.stream.Materializer;

import java.util.concurrent.CompletionStage;


/**
 * This class implements the methods needed to communicate with ApixuRequester's API.
 * When a correct request is sent to this actor, it will connect to ApixuRequester's
 * servers and process the response, returning it to the sender.
 * Currently it supports:
 *  - Get current weather
 */
public class ApixuRequester extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private Http httpGate;
    private Materializer materializer;

    // Sender to which return the response
    private ActorRef target;

    // Actor's name
    public static String name = "apixu";

    public ApixuRequester(Http httpGate, Materializer materializer) {
        this.httpGate = httpGate;
        this.materializer = materializer;
    }

    public static Props props(Http httpGate, Materializer materializer) {
        return Props.create(ApixuRequester.class, () -> new ApixuRequester(httpGate, materializer));
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(ApixuCurrentWeatherRequest.class, request -> {
                    target = getSender();
                    requestCurrentWeather(request)
                            .thenAccept(resp -> {log.info(resp.toString()); target.tell(resp, getSelf());});
                })
                .build();
    }

    private CompletionStage<ApixuCurrentWeatherResponse>
            requestCurrentWeather(ApixuCurrentWeatherRequest request) {
        CompletionStage<HttpResponse> responseFuture = httpGate.singleRequest(request.create(), materializer);
        Unmarshaller<HttpEntity, ApixuCurrentWeatherResponse> decoder
                = Jackson.unmarshaller(ApixuCurrentWeatherResponse.class);
        return responseFuture.thenCompose(resp -> decoder.unmarshal(resp.entity(), this.materializer));
    }
}