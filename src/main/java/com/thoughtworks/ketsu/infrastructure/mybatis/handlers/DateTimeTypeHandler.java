package com.thoughtworks.ketsu.infrastructure.mybatis.handlers;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.joda.time.DateTime;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * DateTimeHandler is used to convert DateTime into UTC before try to store it in DB. And read it from DB, covert it to
 * application server timezone. This is important, if we convert all Timestamp into DateTime in UTC, that will may cause
 * wrong business logic since some business logic is depend on timestamp such as midnight.
 */
@MappedTypes(DateTime.class)
public class DateTimeTypeHandler implements TypeHandler<DateTime> {

    private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    @Override
    public void setParameter(PreparedStatement ps, int i, DateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, parameter != null ? new Timestamp(parameter.getMillis()) : null, UTC_CALENDAR);
    }

    @Override
    public DateTime getResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName, UTC_CALENDAR);
        return timestamp != null ? new DateTime(timestamp.getTime()) : null;
    }

    @Override
    public DateTime getResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex, UTC_CALENDAR);
        return timestamp != null ? new DateTime(timestamp.getTime()) : null;
    }

    @Override
    public DateTime getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp ts = cs.getTimestamp(columnIndex, UTC_CALENDAR);
        return ts != null ? new DateTime(ts.getTime()) : null;
    }
}
