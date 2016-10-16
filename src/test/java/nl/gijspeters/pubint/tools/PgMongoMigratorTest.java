package nl.gijspeters.pubint.tools;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by gijspeters on 16-10-16.
 */
public class PgMongoMigratorTest {

    PgMongoMigrator migrator;

    @Before
    public void setUp() throws Exception {
        migrator = new PgMongoMigrator();
    }

    @Test
    public void migrateTweetsAndUsers() throws Exception {
        //migrator.migrateTweetsAndUsers(1);
    }

}