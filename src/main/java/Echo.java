package org.jpos.rest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import org.jpos.iso.ISOChannel;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ISOServerEventListener;
import org.jpos.iso.ISOServerSocketFactory;
import org.jpos.iso.ISOSource;
import org.jpos.iso.ServerChannel;
import org.jpos.q2.QBeanSupport;
import org.jpos.q2.QFactory;
import org.jpos.space.LocalSpace;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.space.SpaceListener;
import org.jpos.util.LogSource;
import org.jpos.util.NameRegistrar;
import org.jpos.util.ThreadPool;
import org.jpos.iso.channel.*; 
import org.jpos.iso.packager.*;
import org.json.simple.parser.*;
import org.json.simple.*;
import java.util.Random;   

@Path("/jpos")
public class Echo {
  @PUT
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes(MediaType.APPLICATION_JSON)
  public Response echoGet(String mssg){ 
    Boolean succ = false;
    Map<String, Object> resp = new HashMap<>();
    try{
        JSONParser parser = new JSONParser();  
        JSONObject json = (JSONObject) parser.parse(mssg);
        ISOMsg m = new ISOMsg(); 
        m.setMTI ("0800");
        json.keySet().forEach(keyStr ->
        {
            Object keyvalue = json.get(keyStr);
            String value = keyvalue.toString();
            String key = keyStr.toString();
            m.set(key, value);  
        });
        // ISOChannel channel = new ASCIIChannel ("localhost", 1337, new ISO87APackager());
        // channel.connect();
        // channel.send(m);

        /* Creating a mock value */
        resp.put("0", "0110");
        resp.put("2", json.get("2"));
        resp.put("3", json.get("3"));
        resp.put("4", json.get("4"));
        resp.put("7", json.get("7"));
        resp.put("11", json.get("11"));
        resp.put("12", json.get("12"));
        resp.put("13", json.get("13"));
        resp.put("14", json.get("14"));
        resp.put("18", json.get("18"));
        resp.put("32", json.get("32"));
        resp.put("33", json.get("33"));
        resp.put("32", json.get("32"));
        resp.put("37", "" + generateRandom(12));
        resp.put("38", "" + generateRandom(6));
        resp.put("39", "00");
        resp.put("49", json.get("49"));
        resp.put("51", json.get("51"));

        succ = true;
    }
    catch(ISOException e){
      e.printStackTrace();
    }
    // catch(IOException e){
    //   e.printStackTrace();
    // }
    catch(ParseException e){
      e.printStackTrace();
    }
    if(!succ){
      resp = new HashMap<>();
      resp.put("success", "false");
    }
    return Response.ok(resp, MediaType.APPLICATION_JSON)
    .status(Response.Status.OK)
    .build();
  }

  public static long generateRandom(int length) {
    Random random = new Random();
    char[] digits = new char[length];
    digits[0] = (char) (random.nextInt(9) + '1');
    for (int i = 1; i < length; i++) {
        digits[i] = (char) (random.nextInt(10) + '0');
    }
    return Long.parseLong(new String(digits));
}

}