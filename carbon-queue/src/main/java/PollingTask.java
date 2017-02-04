import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

/**
 * @author ubuntu 2017/01/15.
 */
public class PollingTask implements Runnable {
    private String queue;
    private AmazonSQSClient client;

    public PollingTask(String queueName) {
        queue = queueName;
        client = new AmazonSQSClient(new BasicAWSCredentials("x", "x"));
        client.setEndpoint("http://localhost:9324");
    }

    @Override
    public void run() {
        String queueUrl = client.getQueueUrl(queue).getQueueUrl();

        ReceiveMessageRequest receiveRequest = new ReceiveMessageRequest(queueUrl);
        receiveRequest.setMaxNumberOfMessages(10);

        while (true) {
            ReceiveMessageResult receiveResult= client.receiveMessage(receiveRequest);
            List<Message> messages = receiveResult.getMessages();

            messages.forEach(System.out::println);
        }
    }
}
