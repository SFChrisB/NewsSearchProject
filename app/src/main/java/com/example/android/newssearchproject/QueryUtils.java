package com.example.android.newssearchproject;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.newssearchproject.NewsActivity.LOG_TAG;

/**
 * Created by AgiChrisPC on 09/07/2017.
 */

public class QueryUtils {

    public QueryUtils() {
}

        public static List<News> fetchNewsData(String requestUrl){
            URL url = createUrl(requestUrl);
            String jsonResponse = null;
            try { jsonResponse = makeHttpRequest(url);}
            catch(IOException e){Log.e(LOG_TAG, "HTTP request failure", e);}

            return extractFeatureFromJson(jsonResponse);
        }

        private static URL createUrl(String urlString) {
            URL url = null;
            try {url = new URL(urlString);}
            catch (MalformedURLException e) {Log.e(LOG_TAG, "URL creation failed", e);}
            return url;
        }

        private static String makeHttpRequest(URL requestUrl) throws IOException {
            String jsonResponse = "";
            if (requestUrl == null) return jsonResponse;
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) requestUrl.openConnection();
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
                else Log.e(LOG_TAG, "Server response error: " + urlConnection.getResponseCode());
            } catch (IOException e) { Log.e(LOG_TAG, "Connection failed", e);
            }finally {
                if (urlConnection != null) urlConnection.disconnect();
                if (inputStream != null) inputStream.close();
            }
            return jsonResponse;
        }

        private static String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line != null){
                    output.append(line);
                    line = bufferedReader.readLine();
                }
            }
            return output.toString();
        }

        private static List<News> extractFeatureFromJson(String jsonResponse){
            if (TextUtils.isEmpty(jsonResponse))return null;
            List<News> news = new ArrayList<>();

            try {
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);
                JSONObject newsResponse = baseJsonResponse.getJSONObject("response");
                JSONArray newsArray = newsResponse.getJSONArray("results");

                String title, sect, nUrl, rawPubD, pubD, type;

                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject currentNews = newsArray.getJSONObject(i);

                    title = currentNews.getString("webTitle");
                    type = currentNews.getString("type");
                    sect = currentNews.getString("sectionName");
                    nUrl = currentNews.getString("webUrl");

                    //The date comes up with 21-06-2017 time after rawPubD has been rearranged
                    rawPubD = currentNews.getString("webPublicationDate");
                    if (rawPubD != null) {
                        String yyyy, mm, dd;
                        yyyy = rawPubD.substring(0, 4);
                        mm = rawPubD.substring(5, 7);
                        dd = rawPubD.substring(8, 10);
                        pubD = dd + "-" + mm + "-" + yyyy;
                    }
                    else pubD = null;

                    news.add(new News(title, type, sect, nUrl, pubD));
                }
            }catch (JSONException e){
                Log.e(LOG_TAG, "Fetching data from JSON failed", e);}
            return news;
        }
}
