package Model;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.model.AqlQueryOptions;
import com.arangodb.util.MapBuilder;
import com.arangodb.velocypack.VPackSlice;
import com.arangodb.velocypack.exception.VPackException;
import com.arangodb.entity.BaseDocument;
import com.arangodb.util.MapBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class Search {

    public static String getCommentByID(int id) {
        ArangoDB arangoDB = new ArangoDB.Builder().build();
        String dbName = "scalable";
        String collectionName = "Comments";
        JSONObject commentObjectM = new JSONObject();
        JSONArray commentArray = new JSONArray();
        //Read Document
        //haygeely id harod b list ids
        try {
            BaseDocument myDocument = arangoDB.db(dbName).collection(collectionName).getDocument("" + id,
                    BaseDocument.class);

            commentObjectM.put("VideoID",myDocument.getAttribute("video_id "));
            commentObjectM.put("Text",myDocument.getAttribute("text"));
            commentObjectM.put("Likes",myDocument.getAttribute("likes"));
            commentObjectM.put("Dislikes",myDocument.getAttribute("dislikes"));
            commentObjectM.put("ChannelID",myDocument.getAttribute("user"));

            ArrayList<Integer> mentionsArray = new ArrayList<>();
            mentionsArray = (ArrayList) myDocument.getAttribute("mentions");
            ArrayList<JSONObject> mentionsNamesArray = new ArrayList<>();

            for (int i = 0; i < mentionsArray.size(); i++) {
                try {
                    BaseDocument myDocument2 = arangoDB.db(dbName).collection("Channels").getDocument("" + mentionsArray.get(i),
                            BaseDocument.class);
                    JSONObject channelObject = new JSONObject();
                    JSONObject info = new JSONObject();
                    info.put("Info",myDocument2.getAttribute("info"));
                    String name = ""; //info.get("name");
                    String profpic = ""; //info.get("profile_pic");
                    channelObject.put("Name", name);
                    channelObject.put("Profile Picture", profpic);
                    mentionsNamesArray.add(channelObject);
                }
                catch (ArangoDBException e) {
                    System.err.println("Failed to get document: myKey; " + e.getMessage());
                }
            }
            commentObjectM.put("mentions",mentionsNamesArray);
        } catch (ArangoDBException e) {
            System.err.println("Failed to get document: myKey; " + e.getMessage());
        }


        return commentObjectM.toString();

    }
    //Search for Videos by title and for Channel by name
//    public static String getSearch(String s) {
//        ArangoDB arangoDB = new ArangoDB.Builder().build();
//        String dbName = "scalable";
//
//        //First get by channel name
//        String collectionName = "Comments";
//        JSONObject searchObjectTotal = new JSONObject();
//
//        System.out.println("String we got:" + s);
//        try {
//            String query = "FOR doc IN Comments\n" +
//                    "        FILTER doc.`Name` == @value\n" +
////                    "        FILTER CONTAINS(doc.`Name`, @value)" +
//                    "        RETURN doc";
//            Map<String, Object> bindVars = new MapBuilder().put("value", s).get();
//
//            ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, bindVars, null,
//                    BaseDocument.class);
//            if(cursor.hasNext()) {
//                BaseDocument cursor2 = null;
//                JSONArray searchArray = new JSONArray();
//                int id=0;
//                for (; cursor.hasNext(); ) {
//                    JSONObject searchObjectM = new JSONObject();
//                    cursor2 = cursor.next();
//                    BaseDocument myDocument2 = arangoDB.db(dbName).collection(collectionName).getDocument(cursor2.getKey(),
//                            BaseDocument.class);
//                    id= Integer.parseInt(cursor2.getKey());
//                    searchObjectM.put("ID", id);
//                    searchObjectM.put("Name", myDocument2.getAttribute("Name"));
//                    searchObjectM.put("Category", myDocument2.getAttribute("Category"));
//                    searchObjectM.put("Profile Picture", myDocument2.getAttribute("ProfilePicture"));
//                    searchArray.add(searchObjectM);
//                }
//                searchObjectTotal.put("Channel "+id,searchArray);
//            }
////            else{
//                JSONArray searchArray = new JSONArray();
//                query = "FOR doc IN Videos\n" +
//                       // "        FILTER doc.`title` like @value\n" +
//                        "        FILTER CONTAINS(doc.`title`, @value)" +
//                        "        RETURN doc";
//                bindVars = new MapBuilder().put("value", s).get();
//
//                cursor = arangoDB.db(dbName).query(query, bindVars, null,
//                        BaseDocument.class);
//                if(cursor.hasNext()) {
//                    BaseDocument cursor2=null;
//                    int id=0;
//                    for (; cursor.hasNext(); ) {
//                        cursor2 = cursor.next();
//                        System.out.println(cursor2.getKey());
//                        JSONObject searchObjectM = new JSONObject();
//                        BaseDocument myDocument2 = arangoDB.db(dbName).collection("Videos").getDocument(cursor2.getKey(),
//                                BaseDocument.class);
//                        id= Integer.parseInt(cursor2.getKey());
//                        searchObjectM.put("ID", id);
//                        searchObjectM.put("Likes", Integer.parseInt(""+myDocument2.getAttribute("likes")));
//                        searchObjectM.put("Dislikes", Integer.parseInt(""+myDocument2.getAttribute("dislikes")));
//                        searchObjectM.put("Views", Integer.parseInt(""+myDocument2.getAttribute("views")));
//                        searchObjectM.put("Thumbnail", myDocument2.getAttribute("thumbnail"));
//                        searchObjectM.put("Title", myDocument2.getAttribute("title"));
//                        searchObjectM.put("Category", myDocument2.getAttribute("category"));
//                        searchObjectM.put("Duration", Integer.parseInt(""+myDocument2.getAttribute("duration")));
//                        searchObjectM.put("Date Created", myDocument2.getAttribute("date_created"));
//                        searchArray.add(searchObjectM);
//                    }
//                    searchObjectTotal.put("Video "+id,searchArray);
//                }
//                else{
//                    searchObjectTotal.put("Not found ",s);
//                }
////            }
//        } catch (ArangoDBException e) {
//            System.err.println("Failed to execute query. " + e.getMessage());
//        }
//        System.out.println("Search Object" + searchObjectTotal.toString());
//        return searchObjectTotal.toString();
//
//
//    }


}
