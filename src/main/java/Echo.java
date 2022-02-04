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

@Path("/jpos")
public class Echo {
  @PUT
  @Produces({MediaType.APPLICATION_JSON})
  @Consumes(MediaType.APPLICATION_JSON)
  public Response echoGet(String mssg){ 
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
        ISOChannel channel = new ASCIIChannel ("localhost", 9999, new ISO87APackager());
        channel.connect();
        channel.send(m);
    }
    catch(ISOException e){
      e.printStackTrace();
    }
    catch(IOException e){
      e.printStackTrace();
    }
    catch(ParseException e){
      e.printStackTrace();
    }
    Map<String, Object> resp = new HashMap<>();
    resp.put("success", "true");
    return Response.ok(resp, MediaType.APPLICATION_JSON)
    .status(Response.Status.OK)
    .build();
  }
}