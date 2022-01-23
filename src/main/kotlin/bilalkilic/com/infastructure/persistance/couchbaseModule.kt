package bilalkilic.com.infastructure.persistance

import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import org.koin.core.qualifier.named
import org.koin.dsl.module

val couchbaseModule = module {


    single(named("articleDatabase")) {
        CouchbaseLite.init()


        val databaseConfiguration = DatabaseConfiguration().apply {
            this.directory = "/Users/bilal.kilic/Documents/cb"
        }
        Database("article-database", databaseConfiguration)
    }

    single(named("feedDatabase")) {
        CouchbaseLite.init()

        val databaseConfiguration = DatabaseConfiguration().apply {
            this.directory = "/Users/bilal.kilic/Documents/cb"
        }
        Database("feeds-database", databaseConfiguration)
    }
}