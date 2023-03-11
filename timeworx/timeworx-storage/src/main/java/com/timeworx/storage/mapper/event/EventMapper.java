package com.timeworx.storage.mapper.event;

import com.timeworx.common.entity.event.Event;
import com.timeworx.common.entity.event.EventOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Insert("insert into EventOrder (`Id`, `EventId`, `PurchaserId`, `PurchaserName`, `OrderStatus`, `CreateTime`) values (#{order.id}, #{order.eventId}, #{order.purchaserId}, #{order.purchaserName}, #{order.orderStatus}, #{order.createTime})")
    Integer insertEventOrder(@Param("order") EventOrder eventOrder);

    @Select("select `Id`, `EventId`, `PurchaserId`, `PurchaserName`, `OrderStatus`, `CreateTime` from EventOrder where EventId = #{eventId} and purchaserId = #{userId} and OrderStatus in (0 , 1) limit 1")
    EventOrder qryUserEventOrder(@Param("eventId") Long eventId,@Param("userId") Long userId);

    @Update("update EventOrder set OrderStatus = #{status} where Id = #{orderId}")
    Integer updateEventOrderStatus(@Param("orderId") Long orderId,@Param("status") Integer status);

    @Update("update Event set EventStatus = #{status} where Id = #{eventId}")
    Integer updateEventStatus(@Param("eventId") Long eventId,@Param("status") Integer status);

    @Select("select count(1) from EventOrder where EventId = #{eventId} and OrderStatus in (0, 1)")
    Integer qryEventParticipatedNum(Long eventId);
}
