package dk.geo.jonas.jsonreader.jonaslarm;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by lars on 09-07-15.
 */
public interface JsonFetcher {

    public JSONObject getThisMofo(String url) throws IOException;
}
