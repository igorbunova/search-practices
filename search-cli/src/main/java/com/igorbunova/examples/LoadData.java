package com.igorbunova.examples;

import java.util.List;
import javax.sql.DataSource;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.typesafe.config.Config;
import com.igorbunova.db.DataSourceFactory;
import com.igorbunova.db.service.async.GetAll;
import com.igorbunova.model.Song;
import com.igorbunova.service.async.AsyncOperation;
import com.igorbunova.utils.Util;

/**
 * LoadData.
 */
@Parameters(
    commandDescription = "load all data from the configured database to the configured engine"
)
public class LoadData implements Runnable {

    private AsyncOperation<List<Song>, ?> engine = null;

    @Parameter(names = {"--engine", "-e"}, converter = PutConverter.class,
        description = "possible values: es, solr",
        required = true)
    public void setEngine(AsyncOperation<List<Song>, ?> engine) {
        this.engine = engine;
    }

    public void run() {
        Config dbConf = Util.loadConfig("ds.conf");
        DataSource ds = DataSourceFactory.create(dbConf);

        GetAll dbGet = new GetAll(ds);
        
        dbGet.apply()
            .buffer(10)
            .flatMap(engine::apply)
            .subscribe();        
    }
}
