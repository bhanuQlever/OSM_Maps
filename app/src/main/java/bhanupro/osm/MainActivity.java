package bhanupro.osm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import bhanupro.osm.Model.Node;
import bhanupro.osm.Model.Osm;

public class MainActivity extends AppCompatActivity {

    private MapView map = null;

    private static final String TAG = "MainACtivty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the map
        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setUseDataConnection(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        GeoPoint startPoint = new GeoPoint(18.465960, 73.823380);
        mapController.setCenter(startPoint);
        mapController.setZoom(9.5);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                InputStream responseInputStream = null;
                OutputStream requestOutputStream = null;

                HttpURLConnection httpURLConnection = null;

                try {

                    // Form the URL
                    URL url = new URL("http://overpass-api.de/api/interpreter/");

                    // Set the HTTP URL connection
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    httpURLConnection.setRequestProperty("Content-Type", "application/xml");

                    // Send request
                    requestOutputStream = httpURLConnection.getOutputStream();
                    requestOutputStream.write("node[waterway=waterfall](18.46,73.82,22,80);out;".getBytes("UTF-8"));

                    // Receive response, then do anything with it
                    //responseInputStream = httpURLConnection.getInputStream();
                    //Log.e("TAG","response is : "+responseInputStream.read());
                    String reply;
                    InputStream in = httpURLConnection.getInputStream();
                    StringBuffer sb = new StringBuffer();
                    try {
                        int chr;
                        while ((chr = in.read()) != -1) {
                            sb.append((char) chr);
                        }
                        reply = sb.toString();
                    } finally {
                        in.close();
                    }
                    Log.e("TAG", reply);
                    parseXml(reply);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void parseXml(String xmString) {

        Osm osm = null;

        StringReader sr = null;
        Serializer serializer = new Persister(new AnnotationStrategy());

        sr = new StringReader(xmString);
        try {
            osm = serializer.read(Osm.class, new StringReader(xmString), false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (sr != null) {
                sr.close();
            }
        }
        if (osm != null) {
            Log.e(TAG, "version is : " + osm.getVersion() + "generator is " + osm.getGenerator());
            Log.e(TAG, "node size is: " + osm.getNode().size());
            for (int i=0;i<osm.getNode().size();i++){
                Node n = osm.getNode().get(i);
                addMarker(this,map,Double.valueOf(n.getLat()),Double.valueOf(n.getLon()),n.getTag().get(0).getV().toString());
            }
        } else {
            Log.e(TAG, "osm class is null");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
    public void addMarker(final Context context, final MapView map, final Double lat, final Double lon, final String title) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Marker marker = new Marker(map);
                marker.setPosition(new GeoPoint(lat, lon));
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(marker);
                //map.invalidate();
                marker.setIcon(context.getResources().getDrawable(R.drawable.ic_location_on_blue));

                marker.setTitle(title);
            }
        });



    }
}
