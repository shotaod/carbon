package org.carbon.sample;

import org.carbon.web.WebStarter;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

import java.util.concurrent.TimeUnit;

import static org.influxdb.InfluxDB.ConsistencyLevel;

/**
 * @author Shota Oda 2016/10/02
 */
public class CarbonStarter {
	public static void main(String[] args) throws Exception {
		new WebStarter().start(ScanBase.class);
	}

	private static void connectInfulxdb() {
        InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:28086", "root", "root");
        String dbName = "timeSeries";
        influxDB.createDatabase(dbName);

        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .tag("async", "true")
                .retentionPolicy("autogen")
                .consistency(ConsistencyLevel.ALL)
                .build();
        Point point1 = Point.measurement("cpu")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("idle", 90L)
                .addField("user", 9L)
                .addField("system", 1L)
                .build();
        Point point2 = Point.measurement("disk")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("used", 80L)
                .addField("free", 1L)
                .build();
        batchPoints.point(point1);
        batchPoints.point(point2);
        influxDB.write(batchPoints);
        Query query = new Query("SELECT idle FROM cpu", dbName);
        influxDB.query(query);
        influxDB.deleteDatabase(dbName);
    }
}
