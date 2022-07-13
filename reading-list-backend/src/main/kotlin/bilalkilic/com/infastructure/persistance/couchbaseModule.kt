package bilalkilic.com.infastructure.persistance

import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.typesafe.config.Config
import org.koin.core.qualifier.named
import org.koin.dsl.module

val couchbaseModule = module {

    single(named("articleDatabase")) {
        CouchbaseLite.init()

        val config = get<Config>()
        val databaseConfiguration = DatabaseConfiguration().apply {
            this.directory = config.getString("couchbase.directory")
        }
        Database("article-database", databaseConfiguration)
    }

    single(named("feedDatabase")) {
        CouchbaseLite.init()

        val config = get<Config>()
        val databaseConfiguration = DatabaseConfiguration().apply {
            this.directory = config.getString("couchbase.directory")
        }
        Database("feeds-database", databaseConfiguration)
    }

    single { ArticleRepository(get(named("articleDatabase"))) }
}