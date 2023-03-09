package com.timeworx.storage.mapper.event;

import com.timeworx.common.entity.event.Event;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/3/8 8:23 PM
 */
public interface EventMapper {

    @Select("select `Id`, `CreatorId`, `CreatorName`, `Theme`, `Desc`, `PhotoUrl`, `StartTime`" +
            ", `EndTime`, `Duration`, `EventType`, `EventLink`, `Price`, `Limit`, `ShareLink`," +
            " `EventStatus`, `CreateTime` from Event where Id = #{eventId}")
    Event qryEventById(@Param("eventId") Long eventId);

    @Select("select count(1) from EventOrder where EventId = #{eventId} and OrderStatus in (0 , 1)")
    Integer qryOrderCountByEventId(@Param("eventId") Long eventId);

    @Insert("replace into `Event` (`Id`, `CreatorId`, `CreatorName`, `Theme`, `Desc`, `PhotoUrl`, `StartTime`, `EndTime`, `Duration`, `EventType`, `EventLink`, `Price`, `Limit`, `ShareLink`, `EventStatus`, `CreateTime` ) " +
            "VALUES (#{event.id}, #{event.creatorId}, #{event.creatorName}, #{event.theme}, #{event.desc}, #{event.photoUrl}, #{event.startTime}, #{event.endTime}, #{event.duration}, #{event.eventType}, #{event.eventLink}, #{event.price}, #{event.limit}, #{event.shareLink},  #{event.eventStatus}, #{event.createTime});")
    Integer replaceEvent(@Param("event") Event event);

    @Select("<script>" +
            " select `Id`, `CreatorId`, `CreatorName`, `Theme`, `Desc`, `PhotoUrl`, `StartTime`" +
            ", `EndTime`, `Duration`, `EventType`, `EventLink`, `Price`, `Limit`, `ShareLink`," +
            " `EventStatus`, `CreateTime` from Event where CreatorId = #{userId} and EventStatus = #{status} " +
            "<if test='pageIndex != 0'>" +
            " limit #{pageStart}, #{pageSize}" +
            "</if>" +
            "</script>")
    List<Event> qryEventList(@Param("userId") Long userId, @Param("status") Integer status, @Param("pageStart") Integer pageStart, @Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);

    @Select("select count(1) from Event where CreatorId = #{userId} and EventStatus = #{status} ")
    Long qryEventListCount(@Param("userId") Long userId, @Param("status") Integer status);
}
