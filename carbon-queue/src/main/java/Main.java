import java.util.stream.IntStream;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Shota Oda 2017/01/15.
 */
public class Main {
    public static class Message {
        private int id;
        private String from;
        private String to;
        private String body;

        public Message(int id, String from, String to, String body) {
            this.id = id;
            this.from = from;
            this.to = to;
            this.body = body;
        }

        public int getId() {
            return id;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getBody() {
            return body;
        }
    }
    public static void main(String[] args) throws Exception {
        final String queueName = "carbon";

        AmazonSQSClient client = new AmazonSQSClient(new BasicAWSCredentials("x", "x"));
        client.setEndpoint("http://localhost:9324");


        PollingTask task = new PollingTask(queueName);
        Thread thread = new Thread(task);
        thread.start();

        String url = client.getQueueUrl(queueName).getQueueUrl();

        sendMessageRange(url, 0, client);

        Thread.sleep(5000);

        sendMessageRange(url, 100, client);

        Thread.sleep(5000);

        sendMessageRange(url, 200, client);
    }

    public static void sendMessageRange(String url, int from, AmazonSQSClient client) {
        ObjectMapper jsonMapper = new ObjectMapper();
        IntStream.range(from, from + 100)
                .mapToObj(i -> new Message(i,"from: hogehoge", "to: piyopiyo", "body: fugafugafuga"))
                .map(message -> {
                    try {
                        return jsonMapper.writeValueAsString(message);
                    } catch (JsonProcessingException e) {
                        return "";
                    }
                })
                .map(json -> new SendMessageRequest(url, json))
                .forEach(client::sendMessage);
    }
}
