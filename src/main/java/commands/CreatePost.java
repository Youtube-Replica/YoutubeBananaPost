package commands;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import model.Post;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

public class CreatePost extends Command {
   public  void execute(){
       HashMap<String, Object> props = parameters;

       Channel channel = (Channel) props.get("channel");
       System.out.println("IN Comment");
       JSONParser parser = new JSONParser();
       int channel_id  = 0;
       String context = "";
       JSONArray likes =null;
       JSONArray dislikes =null;
       int user_id = 0;
       JSONArray mentions = null;
       try {
           JSONObject body = (JSONObject) parser.parse((String) props.get("body"));

           JSONObject params = (JSONObject) parser.parse(body.get("body").toString());

           channel_id = Integer.parseInt( params.get("channel_id").toString());
           context = params.get("context").toString();
           likes = (JSONArray) params.get("likes");
           mentions = (JSONArray) params.get("mentions");
           dislikes = (JSONArray) params.get("dislikes");
           user_id = Integer.parseInt(params.get("user").toString());

       } catch (ParseException e) {
           e.printStackTrace();
       }
       AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
       AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
       Envelope envelope = (Envelope) props.get("envelope");
       String response = Post.createPost(channel_id,context,likes,dislikes,user_id,mentions,new JSONArray());
//        String response = (String)props.get("body");
       try {
           channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
       } catch (IOException e) {
           e.printStackTrace();
       }

}
}

