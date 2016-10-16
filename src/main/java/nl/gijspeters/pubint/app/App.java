package nl.gijspeters.pubint.app;

import nl.gijspeters.pubint.tools.PgMongoMigrator;

/**
 * Created by gijspeters on 16-10-16.
 */
public class App {

    public static void main(String[] args) {
        if (args.length > 0) {
            String com = args[0];
            if (com.equals("migrate")) {
                migrate();
            } else if (com.equals("help")) {
                help();
            } else {
                invalidCom();
            }
        } else {
            invalidCom();
        }
    }

    public static void invalidCom() {
        System.out.println("PubInt - Invalid command.\n" +
                "Type 'pubint help' for help page");
    }

    public static void migrate() {
        PgMongoMigrator migrator;
        System.out.println("Migrating PotsgreSQL Tweets and Users to MongoDB...");
        try {
            migrator = new PgMongoMigrator();
            migrator.migrateTweetsAndUsers(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Migration failed. \n" +
                    "Data in MongoDB may be corrupt.");
        }
        System.out.println("Migration finished.");
    }

    public static void help() {
        System.out.println("PubInt Help \n" +
                "Commands: \n" +
                "migrate  -- migrates data from PostgreSQL to MongoDB \n" +
                "help -- shows this help");
    }
}
