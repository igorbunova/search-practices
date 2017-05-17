package com.igorbunova.examples;

import javax.sql.DataSource;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.igorbunova.db.DataSourceFactory;
import com.igorbunova.db.service.async.DeleteSong;
import com.igorbunova.service.async.AsyncOperation;
import com.igorbunova.utils.Util;
import com.typesafe.config.Config;

/**
 * DeleteData.
 */
@Parameters(commandDescription = "delete the specified id from the configured database and " +
    "the configured engine"
)
public class DeleteData implements Runnable {
    private long id;
    private AsyncOperation<Long, ?> engine = null;

    @Parameter(names = {"--engine", "-e"}, converter = DeleteConverter.class,
        description = "possible values: es, solr",
        required = true)
    public void setEngine(AsyncOperation<Long, ?> engine) {
        this.engine = engine;
    }

    @Parameter(names = {"--id", "-i"}, required = true)
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void run() {
        Config dbConf = Util.loadConfig("ds.conf");

        DataSource ds = DataSourceFactory.create(dbConf);

        DeleteSong dbDel = new DeleteSong(ds);

        dbDel.apply(id)
            .flatMap(engine::apply)
            .subscribe();
    }
}
