package nl.gijspeters.pubint.tools;

import com.vividsolutions.jts.geom.Coordinate;
import nl.gijspeters.pubint.mongohandler.MorphiaHandler;
import nl.gijspeters.pubint.pghandler.PgConnection;
import nl.gijspeters.pubint.structure.Agent;
import nl.gijspeters.pubint.twitter.Tweet;
import nl.gijspeters.pubint.twitter.TwitterUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by gijspeters on 16-10-16.
 */
public class PgMongoMigrator {

    private PgConnection pcon;
    private MorphiaHandler mcon;

    public PgMongoMigrator() throws SQLException, ClassNotFoundException {
        pcon = new PgConnection(false);
        mcon = MorphiaHandler.getInstance();
    }

    public void migrateTweetsAndUsers(long limit) throws SQLException {
        String limitStr;
        if (limit > 0) {
            limitStr = " ORDER BY tweet_name LIMIT " + String.valueOf(limit);
        } else {
            limitStr = "";
        }
        String sql = "SELECT tweet_name FROM scraped_tweets GROUP BY tweet_name" + limitStr;
        ResultSet userSet = pcon.select(sql);
        HashMap<String, TwitterUser> userMap = new HashMap<String, TwitterUser>();
        while (userSet.next()) {
            String name = userSet.getString("tweet_name");
            Agent agent = new Agent();
            TwitterUser user = new TwitterUser(name, agent);
            userMap.put(name, user);
            mcon.saveAgent(agent);
            mcon.saveUser(user);
        }
        sql = "SELECT tweet_id, tweet_name, tijddatum, lon, lat FROM scraped_tweets" + limitStr;
        ResultSet tweetSet = pcon.select(sql);
        while (tweetSet.next()) {
            String id = tweetSet.getString("tweet_id");
            String name = tweetSet.getString("tweet_name");
            Date date = tweetSet.getTimestamp("tijddatum");
            double lon = tweetSet.getDouble("lon");
            double lat = tweetSet.getDouble("lat");
            Coordinate coord = new Coordinate(lon, lat);
            TwitterUser user = userMap.get(name);
            Tweet tweet = new Tweet(coord, date, user, "", Long.parseLong(id));
            mcon.saveAnchor(tweet);
        }
    }
}
