package com.timeworx.storage.mapper.event;

import com.timeworx.common.entity.event.Event;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/8 8:23 PM
 */
public interface EventMapper {

    @Select("select `Id`, `CreatorId`, `CreatorName`, `Theme`, `Desc`, `PhotoUrl`, `StartTime`" +
            ", `EndTime`, `Duration`, `EventType`, `EventLink`, `Price`, `Limit`, `ShareLink`," +
            " `EventStatus` from Event where Id = #{eventId}")
    Event qryEventById(@Param("eventId") Long eventId);

    @Select("select count(1) from EventOrder where EventId = #{eventId} and Status in (0 , 1)")
    Integer qryOrderCountByEventId(Long eventId);
}
