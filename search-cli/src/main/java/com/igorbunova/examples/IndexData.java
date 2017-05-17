package com.igorbunova.examples;

import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.typesafe.config.Config;
import com.igorbunova.db.DataSourceFactory;
import com.igorbunova.model.Song;
import rx.Observable;
import com.igorbunova.service.async.AsyncOperation;
import com.igorbunova.utils.SongJsonConverter;
import com.igorbunova.utils.Util;

/**
 * IndexData.
 */
@Parameters(commandDescription = "index the passed data to the configured database and " +
    "the configured engine"
)
public class IndexData implements Runnable {

    private Song object = null;
    private AsyncOperation<List<Song>, Long> engine = null;

    @Parameter(names = {"--data", "-d"}, converter =
        SongJsonConverter.class, required = true)
    public void setObject(Song object) {
        this.object = object;
    }

    @Parameter(names = {"--engine", "-e"}, converter = PutConverter.class,
        description = "possible values: es, solr",
        required = true)
    public void setEngine(AsyncOperation<List<Song>, Long> engine) {
        this.engine = engine;
    }

    public void run() {
        Config dbConf = Util.loadConfig("ds.conf");

        DataSource ds = DataSourceFactory.create(dbConf);

        com.igorbunova.db.service.async.PutSong dbPut = new com.igorbunova.db.service.async.PutSong(ds);

        Observable.just(object)
            .flatMap(dbPut::apply)
            .map(Collections::singletonList)
            .flatMap(engine::apply)
            .subscribe();
    }
}
